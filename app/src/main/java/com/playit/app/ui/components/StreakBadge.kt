package com.playit.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.*

/**
 * Task UI-6.01 — Build streak/badge UI system
 *
 * Shared composables for streak counter and milestone badges (5/10/15/20 days).
 * Used consistently across MapScreen top bar and ParentDashboardScreen.
 */
@Composable
fun StreakFlame(
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = "$currentStreak day streak"
        }
    ) {
        Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = null,
            tint = EnergyOrange,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "${currentStreak}d",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = EnergyOrange
        )
    }
}

@Composable
fun StreakBadge(
    milestoneDays: Int,
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    val isUnlocked = currentStreak >= milestoneDays
    val description = "$milestoneDays day streak badge, ${if (isUnlocked) "unlocked" else "locked"}"

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isUnlocked) SoftSky else CreamWhite,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isUnlocked) 2.dp else 1.dp,
            color = if (isUnlocked) EnergyOrange else Border
        ),
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = description
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) EnergyOrange else Disabled),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = CreamWhite,
                        modifier = Modifier.size(12.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Text(
                text = "${milestoneDays}d",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) EnergyOrange else TextSecondary
            )
        }
    )
}

@Composable
fun MilestoneBadgesRow(
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    val milestones = listOf(5, 10, 15, 20)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        milestones.forEach { milestone ->
            StreakBadge(
                milestoneDays = milestone,
                currentStreak = currentStreak
            )
        }
    }
}
