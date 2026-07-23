package com.playit.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.PlayItTheme
import com.playit.app.ui.theme.TextSecondary

/**
 * Shared branded loading indicator for playIT.
 * Replaces generic spinners with a friendly pulsing brand motif (star + glow aura).
 * Includes accessible spoken label for screen readers.
 */
@Composable
fun LoadingIndicator(
    message: String = "Loading...",
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    color: Color = LearningBlue,
    showMessage: Boolean = false,
    inline: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "loadingIndicatorTransition")

    // Pulsing scale for friendly breathing motion
    val scalePulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scalePulse"
    )

    // Gentle continuous rotation
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val content = @Composable {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            // Soft background aura
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scalePulse)
            ) {}

            // Branded icon motif (Star)
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = AchievementGold,
                modifier = Modifier
                    .size(size * 0.6f)
                    .rotate(rotation)
                    .scale(scalePulse)
            )
        }
    }

    val semanticsModifier = modifier.semantics(mergeDescendants = true) {
        contentDescription = message
    }

    if (showMessage && message.isNotBlank()) {
        if (inline) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(PlayItSpacing.small),
                modifier = semanticsModifier
            ) {
                content()
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = TextSecondary,
                    fontSize = 16.sp
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PlayItSpacing.small),
                modifier = semanticsModifier
            ) {
                content()
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = TextSecondary,
                    fontSize = 16.sp
                )
            }
        }
    } else {
        Box(modifier = semanticsModifier) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    PlayItTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoadingIndicator(message = "Loading sound...")
            LoadingIndicator(
                message = "Generating PDF document...",
                showMessage = true,
                inline = true
            )
            LoadingIndicator(
                message = "Loading app resources...",
                showMessage = true,
                inline = false
            )
        }
    }
}
