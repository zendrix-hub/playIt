package com.playit.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.playit.app.R

// ACCESSIBILITY GUARDRAIL: Font sizes MUST NEVER be below 16sp across the entire application.

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val nunitoFont = GoogleFont("Nunito")
private val poppinsFont = GoogleFont("Poppins")

/**
 * Primary FontFamily: Nunito as primary with Poppins fallback chain and system SansSerif fallback.
 */
val PlayItFontFamily = FontFamily(
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.ExtraBold),
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.ExtraBold),
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.Normal)
)

// 5-Tier Named TextStyles (Design System v1.0)
val DisplayLarge = TextStyle(
    fontFamily = PlayItFontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 40.sp,
    lineHeight = 48.sp
)

val Heading = TextStyle(
    fontFamily = PlayItFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 36.sp
)

val Subheading = TextStyle(
    fontFamily = PlayItFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    lineHeight = 28.sp
)

val Body = TextStyle(
    fontFamily = PlayItFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    lineHeight = 24.sp
)

val Caption = TextStyle(
    fontFamily = PlayItFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 20.sp
)

// Material 3 Typography Mapping
val AppTypography = Typography(
    displayLarge = DisplayLarge,
    headlineMedium = Heading,
    titleMedium = Subheading,
    bodyLarge = Body,
    bodyMedium = Caption,
    labelLarge = Caption
)
