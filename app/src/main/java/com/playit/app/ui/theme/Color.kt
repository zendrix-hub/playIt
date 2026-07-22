package com.playit.app.ui.theme

import androidx.compose.ui.graphics.Color

// Design System v1.0 Color Tokens
val LearningBlue = Color(0xFF4A90E2)
val GrowthGreen = Color(0xFF4CAF50)
val AchievementGold = Color(0xFFFFC107)
val EnergyOrange = Color(0xFFFF9800)
val FriendlyPurple = Color(0xFF8E7DF2)
val SoftSky = Color(0xFFEAF6FF)
val CreamWhite = Color(0xFFFFFDF8)
val GentleCorrectionOrange = Color(0xFFFFB74D)
val TextPrimary = Color(0xFF2D3748)
val TextSecondary = Color(0xFF718096)
val Border = Color(0xFFE2E8F0)
val Disabled = Color(0xFFCBD5E0)

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