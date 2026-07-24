package com.playit.app.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * PlayIT Design System v2.0 - Elevation & Surface Tokens
 *
 * Replaces heavy drop shadows with Tonal Elevation per CCI research (01_Foundation.md §4):
 * - Level 0: 0dp (Flat background)
 * - Level 1: Tonal tint (+4%) for Learning & Picture Cards
 * - Level 2: Tonal tint (+8%) for Modals
 * - Level 3: 2dp soft shadow retained on Primary CTA only
 */
val cardElevation: Dp = 4.dp
val buttonElevation: Dp = 2.dp

/**
 * Structured PlayItElevation object providing direct dot-notation token access.
 */
object PlayItElevation {
    val level0: Dp = 0.dp
    val level1: Dp = 0.dp // Tonal tint +4%
    val level2: Dp = 0.dp // Tonal tint +8%
    val level3: Dp = buttonElevation // 2dp soft shadow on CTA
    val card: Dp = cardElevation
    val button: Dp = buttonElevation
}

