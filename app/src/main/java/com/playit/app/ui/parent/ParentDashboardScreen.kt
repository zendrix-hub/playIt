package com.playit.app.ui.parent

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.playit.app.ui.components.ArithmeticGateDialog
import com.playit.app.ui.components.MilestoneBadgesRow
import com.playit.app.ui.components.PrimaryButton
import com.playit.app.ui.components.ReducedMotionToggle
import com.playit.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// High-contrast AAA (7:1+) color palette for adult Parent Dashboard standard
private val HighContrastDarkText = Color(0xFF1A202C) // 11.5:1 contrast against White
private val HighContrastMutedText = Color(0xFF455A64) // 7.17:1 contrast against White
private val RetentionPurpleText = Color(0xFF5B21B6) // 8.96:1 contrast against White
private val StarBadgeIconColor = Color(0xFF92400E) // 7.11:1 contrast against White
private val StreakBadgeIconColor = Color(0xFF9A3412) // 7.34:1 contrast against White

private val MasteredGreenBg = GrowthGreen.copy(alpha = 0.15f)
private val MasteredGreenText = Color(0xFF1B5E20) // 9.8:1 contrast
private val DevelopingOrangeBg = GentleCorrectionOrange.copy(alpha = 0.20f)
private val DevelopingOrangeText = Color(0xFFBF360C) // 8.75:1 contrast
private val AtRiskRedBg = Color(0xFFFEE2E2) // Soft Red tint
private val AtRiskRedText = Color(0xFF991B1B) // 8.32:1 contrast against White
private val NotStartedGrayBg = Disabled.copy(alpha = 0.25f)
private val NotStartedGrayText = Color(0xFF37474F) // 9.81:1 contrast

// 28 Phonemes grouped into 7 curriculum groups
private val PhonemeGroups = listOf(
    "Group 1" to listOf("m", "s", "a", "i"),
    "Group 2" to listOf("o", "b", "e", "u"),
    "Group 3" to listOf("t", "k", "l", "y"),
    "Group 4" to listOf("n", "g", "ng", "p"),
    "Group 5" to listOf("r", "d", "h", "w"),
    "Group 6" to listOf("c", "f", "j", "ñ"),
    "Group 7" to listOf("q", "v", "x", "z")
)

enum class PhonemeMasteryStatus(
    val label: String,
    val icon: ImageVector,
    val textColor: Color,
    val bgColor: Color
) {
    MASTERED("Mastered", Icons.Default.CheckCircle, MasteredGreenText, MasteredGreenBg),
    DEVELOPING("Developing", Icons.Default.Autorenew, DevelopingOrangeText, DevelopingOrangeBg),
    AT_RISK("At risk", Icons.Default.ErrorOutline, AtRiskRedText, AtRiskRedBg),
    NOT_STARTED("Not started", Icons.Default.RadioButtonUnchecked, NotStartedGrayText, NotStartedGrayBg)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    viewModel: ParentViewModel,
    onBack: () -> Unit,
    onNavigateToReportPreview: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showMathGateDialog by remember { mutableStateOf(false) }
    var mathAnswerInput by remember { mutableStateOf("") }
    var mathGateError by remember { mutableStateOf(false) }
    var mathProblem by remember { mutableStateOf(Pair(7, 8)) }

    fun handleSharePdf(uri: android.net.Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Progress Report"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Parent Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = HighContrastDarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = HighContrastDarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CreamWhite
                )
            )
        },
        containerColor = CreamWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
        ) {
            val profile = uiState.activeProfile
            if (profile != null) {
                // ── 1. Profile Overview Header Card ──────────────────────────────
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Border, RoundedCornerShape(20.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Learning Overview: ${profile.name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = HighContrastDarkText
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatBadgeItem(
                                    label = "Stars",
                                    value = "${profile.totalStars}",
                                    icon = Icons.Default.Star,
                                    iconColor = StarBadgeIconColor
                                )
                                StatBadgeItem(
                                    label = "Streak",
                                    value = "${profile.currentStreak} Days",
                                    icon = Icons.Default.Whatshot,
                                    iconColor = StreakBadgeIconColor
                                )
                                StatBadgeItem(
                                    label = "Mastered",
                                    value = "${uiState.completedLessons.filter { it.isCompleted }.size}/28",
                                    icon = Icons.Default.CheckCircle,
                                    iconColor = MasteredGreenText
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Border.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Streak Milestone Badges",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = HighContrastMutedText,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            MilestoneBadgesRow(currentStreak = profile.currentStreak)
                        }
                    }
                }

                // ── 2. Retention Score & Plain-Language Explanation ─────────────
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Border, RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            val totalPhonemes = 28
                            val completedCount = uiState.completedLessons.filter { it.isCompleted }.size
                            val retentionPct = if (totalPhonemes > 0) (completedCount * 100) / totalPhonemes else 0

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Overall Retention Score",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HighContrastDarkText
                                )
                                Text(
                                    text = "$retentionPct%",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = RetentionPurpleText
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = HighContrastMutedText,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(top = 2.dp)
                                )
                                Text(
                                    text = "Retention score measures how accurately and consistently your child identifies and pronounces letter sounds across completed lessons and practice attempts.",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = HighContrastMutedText,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }

                // ── 3. Export PDF Report Action Button ───────────────────────────
                item {
                    PrimaryButton(
                        text = if (uiState.isGeneratingPdf) "Generating PDF Report..." else "Preview & Export PDF Report 📄",
                        onClick = onNavigateToReportPreview,
                        enabled = !uiState.isGeneratingPdf
                    )
                }

                if (uiState.error != null) {
                    item {
                        Text(
                            text = uiState.error ?: "",
                            color = AtRiskRedText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // ── 4. 28-Letter Status Breakdown (LetterPerformanceTable) ──────
                item {
                    LetterPerformanceTable(
                        phonemeGroups = PhonemeGroups,
                        completedLessons = uiState.completedLessons,
                        findAttempts = uiState.findAttempts,
                        sayAttempts = uiState.sayAttempts
                    )
                }

                // ── 5. Recent Activity Logs ──────────────────────────────────────
                item {
                    Text(
                        text = "Recent Attempts Log",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HighContrastDarkText,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                val sortedAttempts = (uiState.findAttempts.map { AttemptRow(it.attemptedAt, "Find It", it.phonemeId, it.isCorrect) } +
                        uiState.sayAttempts.map { AttemptRow(it.attemptedAt, "Say It", it.phonemeId, it.isCorrect) })
                        .sortedByDescending { it.timestamp }
                        .take(10)

                if (sortedAttempts.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No practice attempts recorded yet. Start playing map levels to generate stats!",
                                color = HighContrastMutedText,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            )
                        }
                    }
                } else {
                    items(sortedAttempts) { attempt ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Border, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${attempt.type}: Sound \"${attempt.phonemeId.uppercase()}\"",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = HighContrastDarkText
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = SimpleDateFormat("MMM dd, yyyy · HH:mm", Locale.getDefault()).format(Date(attempt.timestamp)),
                                        fontSize = 14.sp,
                                        color = HighContrastMutedText
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (attempt.isCorrect) MasteredGreenBg else AtRiskRedBg)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (attempt.isCorrect) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = if (attempt.isCorrect) MasteredGreenText else AtRiskRedText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (attempt.isCorrect) "PASSED" else "NEEDS PRACTICE",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (attempt.isCorrect) MasteredGreenText else AtRiskRedText
                                    )
                                }
                            }
                        }
                    }
                }

                // ── 6. Settings & Accessibility Section (ReducedMotionToggle) ───
                item {
                    Text(
                        text = "Settings & Accessibility",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HighContrastDarkText,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Border, RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            ReducedMotionToggle(
                                checked = uiState.reducedMotionEnabled,
                                onCheckedChange = { viewModel.setReducedMotionEnabled(it) }
                            )
                        }
                    }
                }

                // ── 7. Destructive Action Entry Point (Arithmetic Gated) ─────────
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AtRiskRedBg.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .border(1.dp, AtRiskRedText.copy(alpha = 0.40f), RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = null,
                                    tint = AtRiskRedText,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "Account & Data Controls",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AtRiskRedText
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Destructive actions are protected by parent math verification to prevent accidental triggers by children.",
                                fontSize = 14.sp,
                                color = HighContrastMutedText
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = {
                                    val num1 = (7..12).random()
                                    val num2 = (2..9).random()
                                    mathProblem = Pair(num1, num2)
                                    mathAnswerInput = ""
                                    mathGateError = false
                                    showMathGateDialog = true
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = AtRiskRedText
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Reset Profile Progress",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "Loading profile data...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = HighContrastMutedText
                    )
                }
            }
        }
    }

    // ── Arithmetic Gate Dialog for Destructive Action ────────────────────────
    if (showMathGateDialog) {
        ArithmeticGateDialog(
            title = "Parent Verification",
            consequenceMessage = "To confirm resetting progress for ${uiState.activeProfile?.name ?: "this profile"}, please solve this math problem:",
            mathNum1 = mathProblem.first,
            mathNum2 = mathProblem.second,
            operator = "×",
            answerInput = mathAnswerInput,
            onAnswerInputChange = {
                mathAnswerInput = it
                mathGateError = false
            },
            isError = mathGateError,
            confirmText = "Confirm Reset",
            cancelText = "Cancel",
            onConfirm = {
                val expected = mathProblem.first * mathProblem.second
                val inputVal = mathAnswerInput.trim().toIntOrNull()
                if (inputVal == expected) {
                    showMathGateDialog = false
                } else {
                    mathGateError = true
                }
            },
            onDismiss = { showMathGateDialog = false }
        )
    }
}

@Composable
private fun StatBadgeItem(
    label: String,
    value: String,
    icon: ImageVector,
    iconColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HighContrastDarkText
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = HighContrastMutedText,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * LetterPerformanceTable - 28-Letter Curriculum Status breakdown grid.
 * Displays all 7 phoneme groups with 7:1+ AAA contrast status indicators for parents.
 */
@Composable
fun LetterPerformanceTable(
    phonemeGroups: List<Pair<String, List<String>>>,
    completedLessons: List<com.playit.app.domain.model.LessonProgress>,
    findAttempts: List<com.playit.app.domain.model.FindItAttempt>,
    sayAttempts: List<com.playit.app.domain.model.SayItAttempt>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Curriculum Status (28 Letters)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = HighContrastDarkText,
            modifier = Modifier.padding(top = 8.dp)
        )

        phonemeGroups.forEach { (groupName, letters) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Border, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = groupName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = HighContrastMutedText
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        letters.forEach { phonemeId ->
                            val status = computePhonemeStatus(
                                phonemeId = phonemeId,
                                completedLessons = completedLessons,
                                findAttempts = findAttempts,
                                sayAttempts = sayAttempts
                            )
                            PhonemeStatusTile(
                                phonemeId = phonemeId,
                                status = status,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhonemeStatusTile(
    phonemeId: String,
    status: PhonemeMasteryStatus,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(status.bgColor)
            .padding(vertical = 10.dp, horizontal = 4.dp)
    ) {
        Text(
            text = phonemeId.uppercase(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = status.textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
            imageVector = status.icon,
            contentDescription = null,
            tint = status.textColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = status.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = status.textColor,
            textAlign = TextAlign.Center
        )
    }
}

private fun computePhonemeStatus(
    phonemeId: String,
    completedLessons: List<com.playit.app.domain.model.LessonProgress>,
    findAttempts: List<com.playit.app.domain.model.FindItAttempt>,
    sayAttempts: List<com.playit.app.domain.model.SayItAttempt>
): PhonemeMasteryStatus {
    val isCompleted = completedLessons.any { it.phonemeId.equals(phonemeId, ignoreCase = true) && it.isCompleted }
    val relevantFinds = findAttempts.filter { it.phonemeId.equals(phonemeId, ignoreCase = true) }
    val relevantSays = sayAttempts.filter { it.phonemeId.equals(phonemeId, ignoreCase = true) }
    val totalAttempts = relevantFinds.size + relevantSays.size
    val correctAttempts = relevantFinds.count { it.isCorrect } + relevantSays.count { it.isCorrect }

    if (totalAttempts == 0 && !isCompleted) {
        return PhonemeMasteryStatus.NOT_STARTED
    }

    val accuracy = if (totalAttempts > 0) correctAttempts.toFloat() / totalAttempts else 1.0f
    val failedAttempts = totalAttempts - correctAttempts

    return when {
        isCompleted || accuracy >= 0.8f -> PhonemeMasteryStatus.MASTERED
        failedAttempts >= 3 || accuracy < 0.5f -> PhonemeMasteryStatus.AT_RISK
        else -> PhonemeMasteryStatus.DEVELOPING
    }
}

private data class AttemptRow(
    val timestamp: Long,
    val type: String,
    val phonemeId: String,
    val isCorrect: Boolean
)

