package com.playit.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playit.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Task UI-7.04 — StarRating Component
 *
 * Finalizes the celebration-tier star-reveal animation for both completion screens:
 * - Sequenced per-star reveal: drop-in (translateY) -> bounce (overshoot spring) -> glow (scale pulse).
 * - Staggered across multiple stars so a 3-star result builds up rather than popping simultaneously.
 * - Total sequence timing completes within 600–1200ms celebration band.
 * - Respects reducedMotion (falls back to clean static/fade state without drop/bounce/glow).
 * - Shared identically by LetterCompleteScreen and BlendItCompleteScreen.
 */

@Composable
fun StarRating(
    starsEarned: Int,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    reducedMotion: Boolean = LocalReducedMotion.current
) {
    val count = starsEarned.coerceIn(1, 3)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..3) {
            val isEarned = i <= count
            val starIndex = i - 1

            SingleStarItem(
                starNumber = i,
                isEarned = isEarned,
                starIndex = starIndex,
                animate = animate,
                reducedMotion = reducedMotion
            )
        }
    }
}

@Composable
private fun SingleStarItem(
    starNumber: Int,
    isEarned: Boolean,
    starIndex: Int,
    animate: Boolean,
    reducedMotion: Boolean
) {
    val animOffsetY = remember { Animatable(if (animate && isEarned && !reducedMotion) -40f else 0f) }
    val animScale = remember { Animatable(if (animate && isEarned && !reducedMotion) 0.2f else 1f) }
    val animAlpha = remember { Animatable(if (animate && !reducedMotion) 0f else 1f) }
    val animGlowScale = remember { Animatable(1f) }

    LaunchedEffect(key1 = isEarned, key2 = animate, key3 = reducedMotion) {
        if (!animate || reducedMotion) {
            animOffsetY.snapTo(0f)
            animScale.snapTo(1f)
            animAlpha.snapTo(1f)
            animGlowScale.snapTo(1f)
            return@LaunchedEffect
        }

        if (isEarned) {
            // Stagger delay per star: Star 1 (0ms), Star 2 (200ms), Star 3 (400ms)
            delay(starIndex * 200L)

            // Fade-in concurrently with drop
            launch {
                animAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 150)
                )
            }

            // Drop-in translateY + spring bounce
            launch {
                animOffsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            // Scale-in with overshoot bounce
            launch {
                animScale.animateTo(
                    targetValue = 1.15f,
                    animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
                )
                animScale.animateTo(
                    targetValue = 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )

                // Brief radial glow scale pulse
                animGlowScale.animateTo(
                    targetValue = 1.25f,
                    animationSpec = tween(durationMillis = 100)
                )
                animGlowScale.animateTo(
                    targetValue = 1.0f,
                    animationSpec = tween(durationMillis = 120)
                )
            }
        } else {
            // Unearned star simple staggered reveal
            delay(starIndex * 150L)
            animAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200)
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset(y = animOffsetY.value.dp)
            .scale(animScale.value)
            .alpha(animAlpha.value)
    ) {
        // Soft background glow ring when earned
        if (isEarned) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .scale(animGlowScale.value)
                    .background(AchievementGold.copy(alpha = 0.25f), CircleShape)
            )
        }

        Surface(
            shape = CircleShape,
            color = if (isEarned) AchievementGold else CreamWhite.copy(alpha = 0.7f),
            shadowElevation = if (isEarned) 6.dp else 1.dp,
            modifier = Modifier.size(56.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = if (isEarned) "Earned Star $starNumber" else "Star $starNumber",
                    tint = if (isEarned) CreamWhite else EnergyOrange.copy(alpha = 0.4f),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StarRating3StarPreview() {
    PlayItTheme {
        StarRating(starsEarned = 3, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun StarRating2StarPreview() {
    PlayItTheme {
        StarRating(starsEarned = 2, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun StarRating1StarPreview() {
    PlayItTheme {
        StarRating(starsEarned = 1, modifier = Modifier.padding(16.dp))
    }
}
