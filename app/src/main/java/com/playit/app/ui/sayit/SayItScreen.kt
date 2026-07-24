package com.playit.app.ui.sayit

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playit.app.PlayItApplication
import com.playit.app.ui.components.FeedbackCardWrapper
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.MicListeningVisualizer
import com.playit.app.ui.components.PlayItLearningScaffold
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.Disabled
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.GentleCorrectionOrange
import com.playit.app.ui.theme.GrowthGreen
import com.playit.app.ui.theme.TextPrimary

// ─── SayItScreen (entry point, handles permissions & state) ─────────────────

@Composable
fun SayItScreen(
    phonemeId: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: SayItViewModel = viewModel(
        factory = SayItViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            repository = (LocalContext.current.applicationContext as PlayItApplication).repository,
            phonemeId   = phonemeId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context  = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.checkAmbientNoise()
            viewModel.onRecordButtonClicked()
        }
    }

    fun handleMicClick() {
        if (uiState.isModelLoading || uiState.isUiLocked) return // Multi-touch lockout & loading check
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) viewModel.onRecordButtonClicked()
        else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    // Trigger noise check when screen appears and permission is active
    LaunchedEffect(Unit) {
        viewModel.checkAmbientNoise()
    }

    SayItContent(
        phonemeId     = phonemeId,
        isLoading     = uiState.isModelLoading,
        isRecording   = uiState.isRecording,
        isSuccess     = uiState.isSuccess,
        activeHearts  = uiState.activeHearts,
        partialText   = uiState.partialText,
        resultText    = uiState.resultText,
        errorMessage  = uiState.errorMessage,
        isTooNoisy    = uiState.isTooNoisy,
        amplitude     = uiState.amplitude,
        isUiLocked    = uiState.isUiLocked,
        onBackClick   = { if (!uiState.isUiLocked) onBack() },
        onRecordClick = ::handleMicClick,
        onNextClick   = { if (!uiState.isUiLocked) onNext() }
    )
}

// ─── SayItContent (pure UI, exactly 3 interactable elements) ──────────────────

@Composable
fun SayItContent(
    phonemeId: String,
    isLoading: Boolean,
    isRecording: Boolean,
    isSuccess: Boolean,
    activeHearts: Int,
    partialText: String,
    resultText: String,
    errorMessage: String?,
    isTooNoisy: Boolean,
    amplitude: Float,
    isUiLocked: Boolean,
    onBackClick: () -> Unit,
    onRecordClick: () -> Unit,
    onNextClick: () -> Unit
) {
    PlayItLearningScaffold(
        title         = "SAY IT",
        activeHearts  = activeHearts,
        isNextEnabled = isSuccess && !isUiLocked,
        onBackClick   = onBackClick,
        onNextClick   = onNextClick,
        centerContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {

                // ── Ambient Noise Alert Banner ────────────────────────────────
                if (isTooNoisy) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GentleCorrectionOrange.copy(alpha = 0.15f),
                        border = BorderStroke(1.5.dp, GentleCorrectionOrange),
                        modifier = Modifier.fillMaxWidth(0.95f)
                    ) {
                        Text(
                            text = "🎧 It's a little noisy here — let's find a quiet spot for best results!",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }

                // ── Unified FeedbackCard & Phoneme Display Card ───────────────
                val cardBorderColor = when {
                    isSuccess                             -> GrowthGreen
                    resultText.isNotEmpty() && !isSuccess -> GentleCorrectionOrange
                    else                                  -> Color.Transparent
                }

                val answerFeedbackState = when {
                    isSuccess                             -> true
                    resultText.isNotEmpty() && !isSuccess -> false
                    else                                  -> null
                }

                FeedbackCardWrapper(
                    isCorrect = answerFeedbackState
                ) {
                    Card(
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = CreamWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        border = if (cardBorderColor != Color.Transparent)
                            BorderStroke(4.dp, cardBorderColor)
                        else null,
                        modifier = Modifier.size(200.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .background(AchievementGold.copy(alpha = 0.25f))
                            )
                            Text(
                                text       = phonemeId.uppercase(),
                                fontSize   = 100.sp,
                                fontWeight = FontWeight.Black,
                                color      = TextPrimary
                            )
                        }
                    }
                }

                // ── Recording State / Status Text ────────────────────────────
                val statusText = when {
                    isLoading                -> "Loading speech model..."
                    isSuccess                -> "Great job!"
                    resultText.isNotEmpty()  -> "I heard: \"$resultText\" — tap to try again!"
                    partialText.isNotEmpty() -> partialText
                    isRecording              -> "Listening..."
                    else                     -> "Tap the mic and say the sound out loud"
                }

                val statusColor = when {
                    isSuccess                -> GrowthGreen
                    resultText.isNotEmpty() && !isSuccess -> GentleCorrectionOrange
                    isRecording              -> FriendlyPurple
                    else                     -> TextPrimary
                }

                Text(
                    text       = statusText,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = statusColor,
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.padding(horizontal = 16.dp)
                )

                // ── Mascot Guidance / Encouragement ─────────────────────────
                val mascotState = when {
                    isSuccess                             -> MascotState.Happy
                    resultText.isNotEmpty() && !isSuccess -> MascotState.Encouraging
                    isTooNoisy                            -> MascotState.Thinking
                    isRecording                           -> MascotState.Happy
                    else                                  -> MascotState.Happy
                }

                val mascotMessage = when {
                    isSuccess                             -> "Awesome pronunciation! Tap NEXT to continue."
                    resultText.isNotEmpty() && !isSuccess -> "Good effort! Let's try saying it together. Tap the mic to try again!"
                    isTooNoisy                            -> "It's a little noisy right now. Let me find a quiet spot so I can hear you clearly!"
                    isRecording                           -> "I'm listening! Say the sound clearly."
                    else                                  -> "Tap the mic button and say the sound out loud!"
                }

                MascotBubble(
                    state         = mascotState,
                    message       = mascotMessage,
                    audioResId    = 0,
                    autoPlayAudio = false
                )

                // ── Error State Chip ──────────────────────────────────────────
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter   = fadeIn(),
                    exit    = fadeOut()
                ) {
                    if (errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GentleCorrectionOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text     = errorMessage,
                                color    = GentleCorrectionOrange,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        actionButton = {
            // ── Amplitude-Reactive Mic Listening Visualizer ─────────────────
            val micEnabled = !isLoading && !isSuccess && !isUiLocked
            val micColor   = when {
                !micEnabled -> Disabled
                isRecording -> EnergyOrange
                else        -> FriendlyPurple
            }

            MicListeningVisualizer(
                isRecording = isRecording,
                amplitude = amplitude,
                visualizerSize = 140.dp
            ) {
                Surface(
                    shape           = CircleShape,
                    color           = micColor,
                    onClick         = { if (micEnabled) onRecordClick() },
                    shadowElevation = 8.dp,
                    modifier        = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector        = if (isRecording) Icons.Filled.Mic else Icons.Filled.MicOff,
                            contentDescription = if (isRecording) "Stop recording" else "Start recording",
                            tint               = CreamWhite,
                            modifier           = Modifier.size(44.dp)
                        )
                    }
                }
            }
        }
    )
}