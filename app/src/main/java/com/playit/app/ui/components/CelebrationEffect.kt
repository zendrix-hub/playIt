package com.playit.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.GrowthGreen
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Intensity presets for celebration confetti bursts per Design System v1.0.
 */
enum class CelebrationPreset {
    SMALL, // Micro/standard timing (350ms), lower particle count for correct-answer moments
    LARGE  // Celebration timing (900ms), higher particle count for level/group complete
}

private data class Particle(
    val angle: Double,
    val distance: Float,
    val size: Size,
    val color: Color,
    val rotationSpeed: Float,
    val initialRotation: Float
)

/**
 * Reusable, reduced-motion-aware celebration confetti burst component.
 *
 * - SMALL preset: micro/standard timing (~350ms) for correct answers inside sublevels
 * - LARGE preset: celebration timing (~900ms) for level and group completions
 * - reducedMotion = true: skips moving particle physics, using a non-flashing soft fade overlay
 *
 * @param visible Controls whether the burst triggers.
 * @param preset [CelebrationPreset.SMALL] or [CelebrationPreset.LARGE].
 * @param reducedMotion When true, renders a fade-only fallback with no moving particles.
 * @param modifier Composable layout modifier.
 * @param onFinished Callback fired when animation finishes.
 */
@Composable
fun CelebrationEffect(
    visible: Boolean,
    preset: CelebrationPreset = CelebrationPreset.SMALL,
    reducedMotion: Boolean = false,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {}
) {
    if (!visible) return

    val durationMs = when (preset) {
        CelebrationPreset.SMALL -> 350
        CelebrationPreset.LARGE -> 900
    }

    val particleCount = when (preset) {
        CelebrationPreset.SMALL -> 20
        CelebrationPreset.LARGE -> 50
    }

    val progress = remember(visible, preset) { Animatable(0f) }

    LaunchedEffect(visible, preset) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMs, easing = FastOutSlowInEasing)
        )
        onFinished()
    }

    val currentProgress = progress.value

    if (reducedMotion) {
        // Reduced-motion mode: gentle fade-in / fade-out overlay without moving particles or flashing
        val alpha = if (currentProgress < 0.5f) {
            currentProgress * 2f * 0.15f
        } else {
            (1f - currentProgress) * 2f * 0.15f
        }
        Canvas(modifier = modifier.fillMaxSize()) {
            drawRect(
                color = AchievementGold.copy(alpha = alpha),
                size = size
            )
        }
    } else {
        // Full animated confetti burst using curated brand palette
        val colors = remember {
            listOf(LearningBlue, GrowthGreen, AchievementGold, EnergyOrange, FriendlyPurple)
        }

        val particles = remember(preset) {
            val random = Random(42)
            List(particleCount) {
                val angle = random.nextDouble(0.0, 2.0 * Math.PI)
                val maxDist = if (preset == CelebrationPreset.SMALL) 160f + random.nextFloat() * 120f else 380f + random.nextFloat() * 260f
                val width = if (preset == CelebrationPreset.SMALL) 8f + random.nextFloat() * 6f else 12f + random.nextFloat() * 10f
                val height = if (preset == CelebrationPreset.SMALL) 12f + random.nextFloat() * 8f else 18f + random.nextFloat() * 12f
                Particle(
                    angle = angle,
                    distance = maxDist,
                    size = Size(width, height),
                    color = colors[random.nextInt(colors.size)],
                    rotationSpeed = (random.nextFloat() - 0.5f) * 720f,
                    initialRotation = random.nextFloat() * 360f
                )
            }
        }

        Canvas(modifier = modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f

            val overallAlpha = (1f - currentProgress).coerceIn(0f, 1f)

            particles.forEach { p ->
                val currentDist = p.distance * currentProgress
                val x = centerX + (cos(p.angle) * currentDist).toFloat()
                val y = centerY + (sin(p.angle) * currentDist).toFloat() + (currentProgress * currentProgress * 90f) // gentle gravity fall
                val rotation = p.initialRotation + (p.rotationSpeed * currentProgress)

                translate(left = x - p.size.width / 2f, top = y - p.size.height / 2f) {
                    rotate(degrees = rotation, pivot = Offset(p.size.width / 2f, p.size.height / 2f)) {
                        drawRect(
                            color = p.color.copy(alpha = overallAlpha),
                            size = p.size
                        )
                    }
                }
            }
        }
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(name = "Celebration Effect - Small Preset", showBackground = true)
@Composable
fun CelebrationEffectSmallPreview() {
    PlayItTheme {
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(CreamWhite),
            contentAlignment = Alignment.Center
        ) {
            CelebrationEffect(
                visible = true,
                preset = CelebrationPreset.SMALL,
                reducedMotion = false
            )
        }
    }
}

@Preview(name = "Celebration Effect - Large Preset", showBackground = true)
@Composable
fun CelebrationEffectLargePreview() {
    PlayItTheme {
        Box(
            modifier = Modifier
                .size(400.dp)
                .background(CreamWhite),
            contentAlignment = Alignment.Center
        ) {
            CelebrationEffect(
                visible = true,
                preset = CelebrationPreset.LARGE,
                reducedMotion = false
            )
        }
    }
}

@Preview(name = "Celebration Effect - Reduced Motion", showBackground = true)
@Composable
fun CelebrationEffectReducedMotionPreview() {
    PlayItTheme {
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(CreamWhite),
            contentAlignment = Alignment.Center
        ) {
            CelebrationEffect(
                visible = true,
                preset = CelebrationPreset.LARGE,
                reducedMotion = true
            )
        }
    }
}
