package com.playit.app.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * PlayIT Design System v1.0 - Elevation Tokens
 *
 * Centralized elevation primitives for consistent depth and shadows across UI elements:
 * - Card Elevation: 4dp (spec-mandated exact value)
 * - Button Elevation: 2dp (intentionally subtle, soft shadow; do not increase without a Design System update)
 */
val cardElevation: Dp = 4.dp

// Intentionally subtle elevation for buttons to avoid harsh default shadows; do not increase without a Design System update.
val buttonElevation: Dp = 2.dp
