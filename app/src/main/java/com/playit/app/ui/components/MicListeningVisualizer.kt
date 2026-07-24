package com.playit.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.GrowthGreen
import com.playit.app.ui.theme.LocalReducedMotion

/**
 * Amplitude-reactive mic-listening visualizer tied to real-time PCM audio input stream.
 *
 * Renders dynamic concentric wave rings and amplitude bars that react smoothly
 * to voice pitch and volume during speech recognition sessions.
 *
 * @param isRecording Active microphone state.
 * @param amplitude Real-time normalized PCM RMS amplitude (0.0f..1.0f).
 * @param modifier Composable layout modifier.
 * @param visualizerSize Target diameter for the outer listening canvas.
 * @param primaryColor Main reactive wave ring color.
 * @param secondaryColor Secondary outer ring color.
 * @param content Inner action composable (e.g. microphone button).
 */
@Composable
fun MicListeningVisualizer(
    isRecording: Boolean,
    amplitude: Float,
    modifier: Modifier = Modifier,
    visualizerSize: Dp = 140.dp,
    primaryColor: Color = EnergyOrange,
    secondaryColor: Color = FriendlyPurple,
    reducedMotion: Boolean = LocalReducedMotion.current,
    content: @Composable () -> Unit
) {
    // Smoothly interpolate amplitude for 60fps fluid UI animation
    val animatedAmplitude by animateFloatAsState(
        targetValue = if (isRecording) amplitude.coerceIn(0.05f, 1.0f) else 0f,
        animationSpec = tween(durationMillis = 80, easing = LinearEasing),
        label = "animatedAmplitude"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "micPulse")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulsePhase"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(visualizerSize)
    ) {
        if (isRecording) {
            // Concentric Energy Outer Wave Ring
            val ringScale = if (reducedMotion) 1.15f else 1.0f + (animatedAmplitude * 0.45f)
            val ringAlpha = if (reducedMotion) 0.25f else (0.5f - (animatedAmplitude * 0.25f)).coerceIn(0.1f, 0.6f)

            Box(
                modifier = Modifier
                    .size(visualizerSize * 0.85f)
                    .scale(ringScale)
                    .alpha(ringAlpha)
                    .clip(CircleShape)
                    .background(primaryColor)
            )

            // Secondary Outer Wave Ring
            val secondaryScale = if (reducedMotion) 1.25f else 1.1f + ((animatedAmplitude + (pulsePhase * 0.2f)) * 0.35f)
            Box(
                modifier = Modifier
                    .size(visualizerSize * 0.7f)
                    .scale(secondaryScale)
                    .alpha(ringAlpha * 0.5f)
                    .clip(CircleShape)
                    .background(secondaryColor)
            )

            // Amplitude PCM Waveform Bar Chart Canvas
            Canvas(
                modifier = Modifier.size(visualizerSize)
            ) {
                val barCount = 7
                val barWidth = 6.dp.toPx()
                val spacing = 8.dp.toPx()
                val totalWidth = (barCount * barWidth) + ((barCount - 1) * spacing)
                val startX = (size.width - totalWidth) / 2f
                val centerY = size.height / 2f
                val maxHeight = (size.height * 0.35f)

                val barHeightsMultiplier = floatArrayOf(0.4f, 0.75f, 0.95f, 1.0f, 0.95f, 0.75f, 0.4f)

                for (i in 0 until barCount) {
                    val barX = startX + i * (barWidth + spacing)
                    val heightFactor = barHeightsMultiplier[i]
                    val currentBarHeight = 12.dp.toPx() + (maxHeight * animatedAmplitude * heightFactor)

                    val top = centerY - (currentBarHeight / 2f)
                    val barColor = if (i % 2 == 0) primaryColor else secondaryColor

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(barX, top),
                        size = Size(barWidth, currentBarHeight),
                        cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f),
                        alpha = 0.85f
                    )
                }
            }
        }

        // Inner Mic Button / Custom Action Content
        content()
    }
}
