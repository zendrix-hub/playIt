package com.playit.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.*

/**
 * Task C6 / D3 — SegmentedProgressBar
 *
 * Slim, top-anchored, journey-level progress signal component.
 * Visual representation of linear progress (e.g. "X/28 letters") across total milestones.
 */
@Composable
fun SegmentedProgressBar(
    currentProgress: Int,
    totalSegments: Int = 28,
    modifier: Modifier = Modifier,
    label: String? = null,
    barHeight: Dp = 10.dp,
    fillColor: Color = LearningBlue,
    backgroundColor: Color = Border.copy(alpha = 0.4f),
    showSegmentDividers: Boolean = false
) {
    val safeTotal = totalSegments.coerceAtLeast(1)
    val safeProgress = currentProgress.coerceIn(0, safeTotal)
    val targetFraction = safeProgress.toFloat() / safeTotal.toFloat()

    val animatedFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 400),
        label = "segmented_progress_anim"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (label != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "JOURNEY PROGRESS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = TextSecondary
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = LearningBlue
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(CircleShape)
                .background(backgroundColor)
        ) {
            // Animated Progress Fill
            if (animatedFraction > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedFraction)
                        .clip(CircleShape)
                        .background(fillColor)
                )
            }

            // Optional Segment Dividers for discrete letter groups
            if (showSegmentDividers && safeTotal > 1) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(safeTotal - 1) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(CreamWhite.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}
