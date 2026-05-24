package com.playit.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. Define the Light Theme Mapping
private val LightColorScheme = lightColorScheme(
    primary = TangerineOrange,
    onPrimary = CleanWhite,
    secondary = ActiveBlue,
    background = SoftSkyBlue, // Now safely resolves to the restored color
    surface = CleanWhite,
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal,
    error = GentleCoralRed
)

// 2. Define the Dark Theme Mapping
private val DarkColorScheme = darkColorScheme(
    primary = TangerineOrange,
    onPrimary = DeepCharcoal,
    secondary = ActiveBlue,
    background = DigitalBackground,
    surface = DeepCharcoal,
    onBackground = CleanWhite,
    onSurface = CleanWhite,
    error = GentleCoralRed
)

@Composable
fun PlayItTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to false to strictly enforce the child-friendly UX color palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Force the light theme by default for testing the child-friendly UI
        // Change back to `darkTheme -> DarkColorScheme` if you want dynamic system switching
        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}