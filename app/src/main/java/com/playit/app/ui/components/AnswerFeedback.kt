package com.playit.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playit.app.ui.theme.GentleCorrectionOrange
import com.playit.app.ui.theme.LocalReducedMotion
import com.playit.app.ui.theme.PlayItTheme

/**
 * Reusable, spec-exact answer feedback animation component per Design System v1.0.
 *
 * Wraps activity content to provide uniform feedback animations:
 * - [isCorrect] == true: subtle scale-up animation + small CelebrationEffect confetti burst
 * - [isCorrect] == false: gentle horizontal shake oscillation + Gentle Correction Orange border accent (no flashing/strobe)
 * - [isCorrect] == null: idle default state
 *
 * @param isCorrect State of answer feedback (true = correct, false = incorrect, null = idle/unanswered).
 * @param modifier Composable layout modifier.
 * @param reducedMotion When true, disables motion scale/shake physics and confetti particles.
 * @param onAnimationFinished Callback fired when animation sequence finishes.
 * @param content Target composable element (e.g. card, letter tile, phoneme container).
 */

@Composable
fun AnswerFeedback(
    isCorrect: Boolean?,
    modifier: Modifier = Modifier,
    reducedMotion: Boolean = LocalReducedMotion.current,
    onAnimationFinished: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(isCorrect) {
        when (isCorrect) {
            true -> {
                shakeOffset.snapTo(0f)
                if (!reducedMotion) {
                    scale.animateTo(1.08f, animationSpec = tween(150, easing = FastOutSlowInEasing))
                    scale.animateTo(1.0f, animationSpec = tween(150, easing = FastOutSlowInEasing))
                }
                onAnimationFinished()
            }
            false -> {
                scale.snapTo(1f)
                if (!reducedMotion) {
                    val offsets = listOf(-10f, 10f, -8f, 8f, -4f, 4f, 0f)
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

    val boxModifier = if (isCorrect == false) {
        modifier
            .graphicsLayer { translationX = shakeOffset.value }
            .scale(scale.value)
            .border(BorderStroke(3.dp, GentleCorrectionOrange), RoundedCornerShape(24.dp))
    } else {
        modifier
            .graphicsLayer { translationX = shakeOffset.value }
            .scale(scale.value)
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        content()

        if (isCorrect == true) {
            CelebrationEffect(
                visible = true,
                preset = CelebrationPreset.SMALL,
                reducedMotion = reducedMotion
            )
        }
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(name = "AnswerFeedback - Idle", showBackground = true)
@Composable
fun AnswerFeedbackIdlePreview() {
    PlayItTheme {
        AnswerFeedback(isCorrect = null) {
            Box(modifier = Modifier.scale(1f))
        }
    }
}

@Preview(name = "AnswerFeedback - Correct", showBackground = true)
@Composable
fun AnswerFeedbackCorrectPreview() {
    PlayItTheme {
        AnswerFeedback(isCorrect = true) {
            Box(modifier = Modifier.scale(1f))
        }
    }
}

@Preview(name = "AnswerFeedback - Incorrect", showBackground = true)
@Composable
fun AnswerFeedbackIncorrectPreview() {
    PlayItTheme {
        AnswerFeedback(isCorrect = false) {
            Box(modifier = Modifier.scale(1f))
        }
    }
}
