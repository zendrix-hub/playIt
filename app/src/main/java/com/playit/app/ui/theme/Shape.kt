package com.playit.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * PlayIT Design System v1.0 - Shape Tokens
 *
 * Centralized shape primitives enforcing rounded corners across all components:
 * - Button Shape: 28dp radius
 * - Learning Card Shape: 24dp radius
 * - Reward Card Shape: 32dp radius
 */
val buttonShape = RoundedCornerShape(28.dp)
val learningCardShape = RoundedCornerShape(24.dp)
val rewardCardShape = RoundedCornerShape(32.dp)

val AppShapes = Shapes(
    small = learningCardShape,
    medium = buttonShape,
    large = rewardCardShape
)
