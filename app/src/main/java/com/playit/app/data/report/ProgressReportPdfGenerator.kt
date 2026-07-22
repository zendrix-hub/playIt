package com.playit.app.data.report

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.playit.app.domain.model.FindItAttempt
import com.playit.app.domain.model.LessonProgress
import com.playit.app.domain.model.Profile
import com.playit.app.domain.model.SayItAttempt
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Native Android PDF Generator that draws clean learning report layout onto A4 canvas.
 */
class ProgressReportPdfGenerator(private val context: Context) {

    fun generateReport(
        profile: Profile,
        lessons: List<LessonProgress>,
        findAttempts: List<FindItAttempt>,
        sayAttempts: List<SayItAttempt>
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size (595x842 points)
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paintTitle = Paint().apply {
            color = Color.parseColor("#6C63FF")
            textSize = 28f
            isFakeBoldText = true
        }

        val paintHeader = Paint().apply {
            color = Color.parseColor("#2D2D2D")
            textSize = 14f
            isFakeBoldText = true
        }

        val paintBody = Paint().apply {
            color = Color.parseColor("#4A4A4A")
            textSize = 12f
        }

        val paintBodyBold = Paint().apply {
            color = Color.parseColor("#2D2D2D")
            textSize = 12f
            isFakeBoldText = true
        }

        val paintLine = Paint().apply {
            color = Color.parseColor("#E0E0E0")
            strokeWidth = 1f
        }

        val paintCorrect = Paint().apply {
            color = Color.parseColor("#4CAF50")
            textSize = 12f
            isFakeBoldText = true
        }

        val paintIncorrect = Paint().apply {
            color = Color.parseColor("#FF4B6E")
            textSize = 12f
            isFakeBoldText = true
        }

        var y = 50f

        // ── Title ────────────────────────────────────────────────────────────
        canvas.drawText("playIT Progress Report", 40f, y, paintTitle)
        y += 15f
        canvas.drawLine(40f, y, 555f, y, paintLine)
        y += 30f

        // ── Profile Box ──────────────────────────────────────────────────────
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        canvas.drawText("Profile Name: ", 40f, y, paintBodyBold)
        canvas.drawText(profile.name, 150f, y, paintBody)
        y += 20f
        canvas.drawText("Total Stars: ", 40f, y, paintBodyBold)
        canvas.drawText("${profile.totalStars} ⭐", 150f, y, paintBody)
        y += 20f
        canvas.drawText("Current Streak: ", 40f, y, paintBodyBold)
        canvas.drawText("${profile.currentStreak} 🔥 days", 150f, y, paintBody)
        y += 20f
        canvas.drawText("Date Generated: ", 40f, y, paintBodyBold)
        canvas.drawText(dateFormat.format(Date()), 150f, y, paintBody)
        y += 35f

        canvas.drawLine(40f, y, 555f, y, paintLine)
        y += 30f

        // ── Completed Milestones ──────────────────────────────────────────────
        canvas.drawText("Completed Lessons & Progress", 40f, y, paintHeader)
        y += 25f

        val completedLessons = lessons.filter { it.isCompleted }
        if (completedLessons.isEmpty()) {
            canvas.drawText("No lessons completed yet.", 40f, y, paintBody)
            y += 20f
        } else {
            val chunked = completedLessons.chunked(4)
            for (chunk in chunked) {
                var x = 40f
                for (lesson in chunk) {
                    canvas.drawText("• ${lesson.phonemeId.uppercase()} (${lesson.starsEarned}⭐)", x, y, paintBody)
                    x += 120f
                }
                y += 20f
            }
        }
        y += 25f

        canvas.drawLine(40f, y, 555f, y, paintLine)
        y += 30f

        // ── Recent Activity Logs ──────────────────────────────────────────────
        canvas.drawText("Recent Attempts Log", 40f, y, paintHeader)
        y += 25f

        // Table Header
        canvas.drawText("Date/Time", 40f, y, paintBodyBold)
        canvas.drawText("Type", 180f, y, paintBodyBold)
        canvas.drawText("Target", 280f, y, paintBodyBold)
        canvas.drawText("Spelled/Heard", 380f, y, paintBodyBold)
        canvas.drawText("Result", 480f, y, paintBodyBold)
        y += 10f
        canvas.drawLine(40f, y, 555f, y, paintLine)
        y += 20f

        // Combine logs
        val allLogs = mutableListOf<String>()
        val logDateTimeFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())

        val sortedFind = findAttempts.sortedByDescending { it.attemptedAt }.take(10)
        val sortedSay = sayAttempts.sortedByDescending { it.attemptedAt }.take(10)

        // Render logs
        val attempts = (sortedFind.map { LogRow(it.attemptedAt, "Find It", it.phonemeId, it.selectedPhonemeId, it.isCorrect) } +
                sortedSay.map { LogRow(it.attemptedAt, "Say It", it.phonemeId, "-", it.isCorrect) })
                .sortedByDescending { it.timestamp }
                .take(12)

        if (attempts.isEmpty()) {
            canvas.drawText("No attempts logged yet.", 40f, y, paintBody)
        } else {
            for (att in attempts) {
                if (y > 800) break // Avoid page overflow
                canvas.drawText(logDateTimeFormat.format(Date(att.timestamp)), 40f, y, paintBody)
                canvas.drawText(att.type, 180f, y, paintBody)
                canvas.drawText(att.target.uppercase(), 280f, y, paintBody)
                canvas.drawText(att.input.uppercase(), 380f, y, paintBody)
                if (att.isCorrect) {
                    canvas.drawText("CORRECT", 480f, y, paintCorrect)
                } else {
                    canvas.drawText("INCORRECT", 480f, y, paintIncorrect)
                }
                y += 22f
            }
        }

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "playit_progress_report_${profile.name.replace(" ", "_")}.pdf")
        try {
            val fos = FileOutputStream(file)
            pdfDocument.writeTo(fos)
            fos.close()
            pdfDocument.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            return null
        }
    }

    private data class LogRow(
        val timestamp: Long,
        val type: String,
        val target: String,
        val input: String,
        val isCorrect: Boolean
    )
}
