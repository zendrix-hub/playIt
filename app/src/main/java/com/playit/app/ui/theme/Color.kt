package com.playit.app.ui.theme

import androidx.compose.ui.graphics.Color

// Design System v2.0 Color Tokens (Single Source of Truth)
val LearningBlue = Color(0xFF4A90E2)
val LearningBlueDeep = Color(0xFF3A7BC8) // High-contrast text-safe variant (>=4.5:1 ratio)
val GrowthGreen = Color(0xFF4CAF50)
val AchievementGold = Color(0xFFFFC107)
val EnergyOrange = Color(0xFFFF9800)
val FriendlyPurple = Color(0xFF8E7DF2)
val SoftSky = Color(0xFFEAF6FF)
val CreamWhite = Color(0xFFFFFDF8)
val GentleCorrectionOrange = Color(0xFFFFB74D) // REPLACES harsh red globally
val CorrectionOrange = GentleCorrectionOrange
val TextPrimary = Color(0xFF2D3748)
val TextSecondary = Color(0xFF718096)
val Border = Color(0xFFE2E8F0)
val Disabled = Color(0xFFCBD5E0)

/**
 * Structured PlayItColor object mapping dot-notation tokens to Compose Color properties.
 */
object PlayItColor {
    val learningBlue = LearningBlue
    val learningBlueDeep = LearningBlueDeep
    val growthGreen = GrowthGreen
    val achievementGold = AchievementGold
    val energyOrange = EnergyOrange
    val friendlyPurple = FriendlyPurple
    val softSky = SoftSky
    val creamWhite = CreamWhite
    val correctionOrange = CorrectionOrange
    val textPrimary = TextPrimary
    val textSecondary = TextSecondary
    val border = Border
    val disabled = Disabled

    // Map Node Semantics (01_Foundation.md §2.1)
    val nodeLocked = Disabled
    val nodeUnlocked = LearningBlue
    val nodeCurrent = AchievementGold
    val nodeCompleted = GrowthGreen
}

// Legacy Aliases (maintained for backward compatibility with unpolished screens prior to Phase 5)
@Deprecated("Use SoftSky", ReplaceWith("SoftSky"))
val SoftSkyBlue = SoftSky

@Deprecated("Use CreamWhite", ReplaceWith("CreamWhite"))
val CleanWhite = CreamWhite

@Deprecated("Use EnergyOrange", ReplaceWith("EnergyOrange"))
val TangerineOrange = EnergyOrange

@Deprecated("Use LearningBlue", ReplaceWith("LearningBlue"))
val ActiveBlue = LearningBlue

@Deprecated("Use TextPrimary", ReplaceWith("TextPrimary"))
val DeepCharcoal = TextPrimary

@Deprecated("Use GentleCorrectionOrange", ReplaceWith("GentleCorrectionOrange"))
val GentleCoralRed = GentleCorrectionOrange

@Deprecated("Use GrowthGreen", ReplaceWith("GrowthGreen"))
val SoftMintGreen = GrowthGreen

@Deprecated("Use AchievementGold", ReplaceWith("AchievementGold"))
val SoftSunnyYellow = AchievementGold

@Deprecated("Use TextPrimary", ReplaceWith("TextPrimary"))
val DigitalBackground = TextPrimary

@Deprecated("Use Border", ReplaceWith("Border"))
val GentleGray = Border