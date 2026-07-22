package com.playit.app.ui.components

import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.Border
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.GrowthGreen
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.PlayItTheme
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TouchTarget

// GUARDRAIL - BANNED PHRASES:
// The following phrases are strictly banned from all mascot copy and audio across the application:
// 1. "Wrong!"
// 2. "You lost a heart."

/**
 * All 5 canonical Mascot emotional states specified by Design System v1.0.
 */
enum class MascotState {
    Happy,
    Excited,
    Thinking,
    Encouraging,
    Celebrating
}

/**
 * Shared MascotBubble composable enforcing Design System v1.0 mascot behavior.
 *
 * Structurally enforces the text+audio Voice Rule: [audioResId] is required and non-nullable.
 * Omitting [audioResId] at a call site causes a Kotlin compilation error.
 */
@Composable
fun MascotBubble(
    state: MascotState,
    message: String,
    @RawRes audioResId: Int,
    modifier: Modifier = Modifier,
    autoPlayAudio: Boolean = true,
    onAudioCompleted: () -> Unit = {}
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    var isPlaying by remember { mutableStateOf(false) }

    // Audio playback effect respecting required audioResId
    LaunchedEffect(audioResId, autoPlayAudio) {
        if (autoPlayAudio && audioResId != 0 && !isPreview) {
            try {
                val mediaPlayer = MediaPlayer.create(context, audioResId)
                if (mediaPlayer != null) {
                    isPlaying = true
                    mediaPlayer.setOnCompletionListener {
                        isPlaying = false
                        it.release()
                        onAudioCompleted()
                    }
                    mediaPlayer.start()
                }
            } catch (e: Exception) {
                isPlaying = false
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = PlayItSpacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MascotAvatar(state = state, isPlaying = isPlaying)

        Spacer(modifier = Modifier.width(PlayItSpacing.small))

        BubbleContainer(
            state = state,
            message = message,
            isPlaying = isPlaying,
            onReplayAudio = {
                if (audioResId != 0 && !isPreview && !isPlaying) {
                    try {
                        val mediaPlayer = MediaPlayer.create(context, audioResId)
                        if (mediaPlayer != null) {
                            isPlaying = true
                            mediaPlayer.setOnCompletionListener {
                                isPlaying = false
                                it.release()
                                onAudioCompleted()
                            }
                            mediaPlayer.start()
                        }
                    } catch (e: Exception) {
                        isPlaying = false
                    }
                }
            }
        )
    }
}

@Composable
private fun MascotAvatar(
    state: MascotState,
    isPlaying: Boolean
) {
    val transition = rememberInfiniteTransition(label = "mascot_anim")

    // Distinct idle animation/pose per state
    val scale by when (state) {
        MascotState.Excited -> transition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(300, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale_excited"
        )
        MascotState.Celebrating -> transition.animateFloat(
            initialValue = 0.92f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale_celebrating"
        )
        MascotState.Encouraging -> transition.animateFloat(
            initialValue = 0.98f,
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale_encouraging"
        )
        else -> transition.animateFloat(
            initialValue = 0.99f,
            targetValue = 1.01f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale_idle"
        )
    }

    val rotation by when (state) {
        MascotState.Thinking -> transition.animateFloat(
            initialValue = -6f,
            targetValue = 6f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "rot_thinking"
        )
        else -> remember { mutableStateOf(0f) }
    }

    val (backgroundColor, icon, iconTint) = when (state) {
        MascotState.Happy -> Triple(LearningBlue, Icons.Rounded.Face, CreamWhite)
        MascotState.Excited -> Triple(EnergyOrange, Icons.Rounded.Star, CreamWhite)
        MascotState.Thinking -> Triple(FriendlyPurple, Icons.Rounded.Lightbulb, CreamWhite)
        MascotState.Encouraging -> Triple(GrowthGreen, Icons.Rounded.Favorite, CreamWhite)
        MascotState.Celebrating -> Triple(AchievementGold, Icons.Rounded.Star, TextPrimary)
    }

    val speechScaleMultiplier = if (isPlaying) 1.06f else 1.0f

    Box(
        modifier = Modifier
            .size(TouchTarget.RECOMMENDED)
            .scale(scale * speechScaleMultiplier)
            .rotate(rotation)
            .clip(CircleShape)
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Mascot state: ${state.name}",
            tint = iconTint,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun BubbleContainer(
    state: MascotState,
    message: String,
    isPlaying: Boolean,
    onReplayAudio: () -> Unit
) {
    val stateBorderColor = when (state) {
        MascotState.Happy -> LearningBlue
        MascotState.Excited -> EnergyOrange
        MascotState.Thinking -> FriendlyPurple
        MascotState.Encouraging -> GrowthGreen
        MascotState.Celebrating -> AchievementGold
    }

    Surface(
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp),
        color = CreamWhite,
        shadowElevation = 4.dp,
        modifier = Modifier
            .border(
                width = 2.dp,
                color = stateBorderColor,
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = PlayItSpacing.default, vertical = PlayItSpacing.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(PlayItSpacing.small))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isPlaying) stateBorderColor.copy(alpha = 0.2f) else Color.Transparent)
                    .clickable(onClick = onReplayAudio),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Rounded.VolumeUp,
                    contentDescription = "Replay audio message",
                    tint = if (isPlaying) stateBorderColor else TextPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ============================================================================
// PREVIEWS
// ============================================================================

@Preview(showBackground = true, backgroundColor = 0xFFEAF6FF)
@Composable
fun MascotBubbleAllStatesPreview() {
    PlayItTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MascotBubble(
                state = MascotState.Happy,
                message = "Welcome! Let's learn letters together!",
                audioResId = 1
            )
            MascotBubble(
                state = MascotState.Excited,
                message = "Awesome job! You unlocked a new letter!",
                audioResId = 2
            )
            MascotBubble(
                state = MascotState.Thinking,
                message = "Listen closely to the sound...",
                audioResId = 3
            )
            MascotBubble(
                state = MascotState.Encouraging,
                message = "Great try! Give it another shot!",
                audioResId = 4
            )
            MascotBubble(
                state = MascotState.Celebrating,
                message = "Perfect! 3 Stars earned!",
                audioResId = 5
            )
        }
    }
}
