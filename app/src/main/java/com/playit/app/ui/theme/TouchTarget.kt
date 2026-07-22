package com.playit.app.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * PlayIT Design System v1.0 - Touch Target Constants
 *
 * Standardized minimum touch target dimensions for accessibility and early learners:
 * - MINIMUM: 48dp (WCAG AAA minimum touch target)
 * - RECOMMENDED: 56dp (Recommended standard for child UI components & primary buttons)
 * - IMPORTANT: 64dp (Enlarged target for critical primary actions or letter tiles)
 */
object TouchTarget {
    val MINIMUM: Dp = 48.dp
    val RECOMMENDED: Dp = 56.dp
    val IMPORTANT: Dp = 64.dp
}
