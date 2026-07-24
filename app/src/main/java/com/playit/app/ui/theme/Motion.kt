package com.playit.app.ui.theme

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * PlayIT Design System v2.0 - Motion & Animation Tokens
 *
 * Centralized motion specification for child-friendly micro-animations:
 * - Durations: Fast (150ms), Standard (300ms), Slow (500ms)
 * - Springs: Bouncy spring for playful tactile feedback, Stiff spring for quick transitions
 * - Easing: Standard Material 3 easing curves
 */
object PlayItMotion {
    const val durationFast: Int = 150
    const val durationStandard: Int = 300
    const val durationSlow: Int = 500

    val easeInOut: Easing = FastOutSlowInEasing
    val easeOut: Easing = LinearOutSlowInEasing
    val easeIn: Easing = FastOutLinearInEasing

    fun <T> bouncySpring(
        dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        stiffness: Float = Spring.StiffnessLow
    ): SpringSpec<T> = spring(
        dampingRatio = dampingRatio,
        stiffness = stiffness
    )

    fun <T> stiffSpring(
        dampingRatio: Float = Spring.DampingRatioNoBouncy,
        stiffness: Float = Spring.StiffnessMedium
    ): SpringSpec<T> = spring(
        dampingRatio = dampingRatio,
        stiffness = stiffness
    )
}
