package com.playit.app.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Colour palette (self-contained; no theme dependency) ──────────────────────
private val CardBackground  = Color(0xFFFFFFFF)
private val LabelColor      = Color(0xFF9E9E9E)
private val ValueColor      = Color(0xFF212121)
private val DividerColor    = Color(0xFFEEEEEE)
private val HeartRed        = Color(0xFFE53935)
private val StarAmber       = Color(0xFFFFC107)
private val StreakOrange    = Color(0xFFFF6D00)

/**
 * A floating pill-card anchored to the top of [MapScreen] that surfaces the
 * three learner-health indicators at a glance.
 *
 * The card is purely presentational and stateless — pass current values
 * directly from [MapUiState].
 *
 * @param activeHearts  Current heart count (0–5) displayed with a ❤ icon.
 * @param totalStars    Cumulative stars earned across all lessons (⭐).
 * @param currentStreak Consecutive-day learning streak (🔥).
 * @param modifier      Optional [Modifier] applied to the outermost [Card];
 *                      the caller is responsible for positioning (e.g.
 *                      [Alignment.TopCenter] inside a [Box]).
 */
@Composable
fun TopStatsBar(
    activeHearts: Int,
    totalStars: Int,
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape  = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // ── Hearts ───────────────────────────────────────────────────────
            StatCell(
                emoji     = "❤",
                emojiColor = HeartRed,
                value     = activeHearts.toString(),
                label     = "Hearts"
            )

            VerticalDivider()

            // ── Stars ────────────────────────────────────────────────────────
            StatCell(
                emoji     = "★",
                emojiColor = StarAmber,
                value     = totalStars.toString(),
                label     = "Stars"
            )

            VerticalDivider()

            // ── Streak ───────────────────────────────────────────────────────
            StatCell(
                emoji      = "🔥",
                emojiColor = StreakOrange,
                value      = "${currentStreak}d",
                label      = "Streak"
            )
        }
    }
}

// ── Private helpers ───────────────────────────────────────────────────────────

/**
 * A single icon + value + label column within [TopStatsBar].
 *
 * @param emoji      The Unicode character or emoji representing the stat.
 * @param emojiColor Tint color applied to the emoji text.
 * @param value      The formatted numeric string to display prominently.
 * @param label      Short descriptor beneath the value (e.g. "Hearts").
 */
@Composable
private fun StatCell(
    emoji: String,
    emojiColor: Color,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text      = emoji,
            fontSize  = 22.sp,
            color     = emojiColor,
            textAlign = TextAlign.Center
        )
        Text(
            text       = value,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = ValueColor,
            textAlign  = TextAlign.Center
        )
        Text(
            text      = label,
            fontSize  = 11.sp,
            color     = LabelColor,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * A 1 dp hairline divider rendered between stat cells.
 * Implemented as a plain [Box] to avoid any Material component dependency
 * on a specific API level of `VerticalDivider`.
 */
@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(44.dp)
            .width(1.dp)
            .background(DividerColor)
    )
}