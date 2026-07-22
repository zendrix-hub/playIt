package com.playit.app.ui.hearit

import android.app.Application
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playit.app.ui.components.LearningCard
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.PlayItLearningScaffold
import com.playit.app.ui.components.SecondaryButton
import com.playit.app.ui.theme.GentleCorrectionOrange
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.TextPrimary

// ─── HearItScreen (entry point) ──────────────────────────────────────────────

@Composable
fun HearItScreen(
    phonemeId: String,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: HearItViewModel = viewModel(
        factory = HearItViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            phonemeId   = phonemeId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Auto-play audio when screen first appears
    LaunchedEffect(phonemeId) {
        viewModel.playAudio()
    }

    HearItContent(
        phonemeId   = phonemeId,
        isPlaying   = uiState.isPlaying,
        isNextEnabled = uiState.hasPlayedOnce,   // spec: Next unlocks after first play
        errorMessage = uiState.errorMessage,
        onBackClick  = onBackClick,
        onPlayClick  = { viewModel.playAudio() },
        onNextClick  = onNextClick
    )
}

// ─── HearItContent (pure UI) ─────────────────────────────────────────────────

@Composable
fun HearItContent(
    phonemeId: String,
    isPlaying: Boolean,
    isNextEnabled: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit,
    onNextClick: () -> Unit
) {
    PlayItLearningScaffold(
        title         = "HEAR IT",
        activeHearts  = null,             // Hear It has no hearts — display none (ENG-2.12)
        isNextEnabled = isNextEnabled,
        onBackClick   = onBackClick,
        onNextClick   = onNextClick,
        centerContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PlayItSpacing.default),
                modifier            = Modifier.padding(horizontal = PlayItSpacing.default)
            ) {

                // ── Phoneme Card (Visual Hero using LearningCard) ─────────────
                LearningCard(
                    onClick  = onPlayClick,
                    modifier = Modifier.size(240.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier         = Modifier.fillMaxSize()
                    ) {
                        // Subtle pulsing background aura when playing
                        val transition = rememberInfiniteTransition(label = "heroPulse")
                        val auraScale by transition.animateFloat(
                            initialValue  = 1f,
                            targetValue   = if (isPlaying) 1.15f else 1f,
                            animationSpec = infiniteRepeatable(
                                animation  = tween(600),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "auraScale"
                        )
                        Surface(
                            shape    = CircleShape,
                            color    = LearningBlue.copy(alpha = 0.15f),
                            modifier = Modifier
                                .size(160.dp)
                                .scale(auraScale)
                        ) {}

                        Text(
                            text       = phonemeId.uppercase(),
                            fontSize   = 100.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = TextPrimary
                        )
                    }
                }

                // ── Mascot Guidance / Visible Next-Gating Reason ───────────────
                if (!isNextEnabled) {
                    MascotBubble(
                        state         = MascotState.Thinking,
                        message       = "Tap the letter or replay button to hear its sound first!",
                        audioResId    = 0,
                        autoPlayAudio = false
                    )
                } else if (isPlaying) {
                    MascotBubble(
                        state         = MascotState.Excited,
                        message       = "Listen carefully to the sound!",
                        audioResId    = 0,
                        autoPlayAudio = false
                    )
                } else {
                    MascotBubble(
                        state         = MascotState.Happy,
                        message       = "Great listening! Tap NEXT when you're ready.",
                        audioResId    = 0,
                        autoPlayAudio = false
                    )
                }

                // ── Error feedback chip ──────────────────────────────────────
                if (errorMessage != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GentleCorrectionOrange.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text     = errorMessage,
                            color    = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        },
        actionButton = {
            // ── Replay Control using SecondaryButton ─────────────────────
            SecondaryButton(
                text        = if (isPlaying) "Playing..." else "Replay Sound",
                onClick     = onPlayClick,
                leadingIcon = {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Replay phoneme sound",
                        tint               = LearningBlue,
                        modifier           = Modifier.size(28.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(56.dp)
            )
        }
    )
}