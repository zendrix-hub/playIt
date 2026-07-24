package com.playit.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.GentleCorrectionOrange
import com.playit.app.ui.theme.GrowthGreen
import com.playit.app.ui.theme.LocalReducedMotion
import com.playit.app.ui.theme.TextPrimary

/**
 * FeedbackVariant defines visual and copy states for answer feedback.
 */
enum class FeedbackVariant {
    CORRECT,
    RETRY
}

/**
 * Unified FeedbackCard component for early learner feedback across learning screens.
 *
 * Strictly adheres to Emotional Safety guidelines:
 * - CORRECT state: Growth Green fill (#4CAF50), checkmark backup icon, encouraging "Magaling! / Great job!" text.
 * - RETRY state: Gentle Correction Orange fill (#FFB74D), refresh backup icon, non-punitive "Subukan natin uli! / Let's try again!" text.
 * - ZERO RED: Red fill, buzzer visuals, and harsh punishment language are completely prohibited.
 */
@Composable
fun FeedbackCard(
    variant: FeedbackVariant,
    modifier: Modifier = Modifier,
    customMessage: String? = null
) {
    val backgroundColor = when (variant) {
        FeedbackVariant.CORRECT -> GrowthGreen
        FeedbackVariant.RETRY -> GentleCorrectionOrange
    }

    val icon = when (variant) {
        FeedbackVariant.CORRECT -> Icons.Default.Check
        FeedbackVariant.RETRY -> Icons.Default.Refresh
    }

    val iconDescription = when (variant) {
        FeedbackVariant.CORRECT -> "Correct"
        FeedbackVariant.RETRY -> "Try again"
    }

    val message = customMessage ?: when (variant) {
        FeedbackVariant.CORRECT -> "Magaling! (Great job!)"
        FeedbackVariant.RETRY -> "Subukan natin uli! (Let's try again!)"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(2.dp, CreamWhite.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .semantics { contentDescription = "$iconDescription feedback: $message" },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CreamWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = iconDescription,
                    tint = backgroundColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CreamWhite
            )
        }
    }
}

/**
 * Animated FeedbackCardWrapper that handles horizontal oscillation and scale animation
 * around target content for answer verification.
 */
@Composable
fun FeedbackCardWrapper(
    isCorrect: Boolean?,
    modifier: Modifier = Modifier,
    reducedMotion: Boolean = LocalReducedMotion.current,
    onAnimationFinished: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val soundManager = remember(context) { com.playit.app.data.audio.SoundManager.getInstance(context) }
    val scale = remember { Animatable(1f) }
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(isCorrect) {
        when (isCorrect) {
            true -> {
                soundManager.playCorrectAnswer()
                shakeOffset.snapTo(0f)
                if (!reducedMotion) {
                    scale.animateTo(1.06f, animationSpec = tween(150))
                    scale.animateTo(1.0f, animationSpec = tween(150))
                }
                onAnimationFinished()
            }
            false -> {
                soundManager.playIncorrectAnswer()
                scale.snapTo(1f)
                if (!reducedMotion) {
                    val offsets = listOf(-8f, 8f, -6f, 6f, -3f, 3f, 0f)
                    for (offset in offsets) {
                        shakeOffset.animateTo(offset, animationSpec = tween(40, easing = LinearEasing))
                    }
                }
                onAnimationFinished()
            }
            null -> {
                scale.snapTo(1f)
                shakeOffset.snapTo(0f)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .graphicsLayer { translationX = shakeOffset.value }
            .scale(scale.value)
    ) {
        content()

        AnimatedVisibility(
            visible = isCorrect != null,
            enter = fadeIn() + scaleIn(initialScale = 0.9f),
            exit = fadeOut() + scaleOut(targetScale = 0.9f)
        ) {
            if (isCorrect != null) {
                FeedbackCard(
                    variant = if (isCorrect) FeedbackVariant.CORRECT else FeedbackVariant.RETRY,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
