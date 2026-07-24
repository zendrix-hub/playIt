package com.playit.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * PlayIT Design System v2.0 - Shape Tokens
 *
 * Centralized shape primitives enforcing rounded corners across all components:
 * - Small Card Shape: 16dp radius
 * - Learning Card Shape: 24dp radius
 * - Button Shape: 28dp radius
 * - Reward Card Shape: 32dp radius
 */
val smallCardShape = RoundedCornerShape(16.dp)
val learningCardShape = RoundedCornerShape(24.dp)
val buttonShape = RoundedCornerShape(28.dp)
val rewardCardShape = RoundedCornerShape(32.dp)

/**
 * Structured PlayItShape object mapping dot-notation shape tokens.
 */
object PlayItShape {
    val smallCard = smallCardShape
    val learningCard = learningCardShape
    val button = buttonShape
    val rewardCard = rewardCardShape
}

val AppShapes = Shapes(
    small = smallCardShape,
    medium = learningCardShape,
    large = rewardCardShape
)

