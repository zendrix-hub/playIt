package com.playit.app.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * PlayIT Design System v2.0 - Touch Target Constants
 *
 * Standardized minimum touch target dimensions for accessibility and early learners:
 * - MINIMUM: 54dp (CCI evidence-based floor for 5-7 year olds, upgraded from 48dp)
 * - RECOMMENDED: 56dp (Recommended standard for child UI components & primary buttons)
 * - IMPORTANT: 64dp (Enlarged target for critical primary actions or letter tiles)
 * - MIN_SPACING: 16dp (Minimum dead space between adjacent interactable targets)
 */
object TouchTarget {
    val MINIMUM: Dp = 54.dp
    val RECOMMENDED: Dp = 56.dp
    val IMPORTANT: Dp = 64.dp
    val MIN_SPACING: Dp = 16.dp
}

