package com.playit.app.ui.util

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import com.playit.app.ui.theme.LocalReducedMotion

/**
 * Shared tapFeedback modifier enforcing Design System v1.0 100% -> 92% -> 100% spring bounce.
 *
 * Micro-interaction timing: 150-250ms with medium bouncy damping.
 * Automatically respects [LocalReducedMotion].
 */
fun Modifier.tapFeedback(
    isPressed: Boolean,
    enabled: Boolean = true,
    pressedScale: Float = 0.92f,
    restingScale: Float = 1.0f,
    reducedMotion: Boolean = false
): Modifier = composed {
    val localReducedMotion = LocalReducedMotion.current || reducedMotion
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !localReducedMotion) pressedScale else restingScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tap_feedback_scale"
    )
    this.scale(scale)
}

/**
 * Overload of tapFeedback accepting a MutableInteractionSource.
 */
fun Modifier.tapFeedback(
    interactionSource: InteractionSource,
    enabled: Boolean = true,
    pressedScale: Float = 0.92f,
    restingScale: Float = 1.0f,
    reducedMotion: Boolean = false
): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    tapFeedback(
        isPressed = isPressed,
        enabled = enabled,
        pressedScale = pressedScale,
        restingScale = restingScale,
        reducedMotion = reducedMotion
    )
}
