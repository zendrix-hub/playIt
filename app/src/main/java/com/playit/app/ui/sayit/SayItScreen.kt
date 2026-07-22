package com.playit.app.ui.sayit

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.PlayItLearningScaffold
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.Disabled
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.GrowthGreen
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary

// ─── SayItScreen (entry point, handles permissions) ──────────────────────────

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
        if (uiState.isModelLoading) return   // don't allow tap while model loads
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) viewModel.onRecordButtonClicked()
        else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    // Trigger noise check when screen appears and we have permission
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
        onBackClick   = onBack,
        onRecordClick = ::handleMicClick,
        onNextClick   = onNext
    )
}

// ─── SayItContent (pure UI, easily previewable) ───────────────────────────────

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
    onBackClick: () -> Unit,
    onRecordClick: () -> Unit,
    onNextClick: () -> Unit
) {
    PlayItLearningScaffold(
        title         = "SAY IT",
        activeHearts  = activeHearts,
        isNextEnabled = isSuccess,
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
                        color = EnergyOrange.copy(alpha = 0.15f),
                        border = BorderStroke(1.5.dp, EnergyOrange),
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

                // ── Phoneme Card ─────────────────────────────────────────────
                val cardBorderColor = when {
                    isSuccess                             -> GrowthGreen
                    resultText.isNotEmpty() && !isSuccess -> EnergyOrange
                    else                                  -> Color.Transparent
                }

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
                    resultText.isNotEmpty() && !isSuccess -> EnergyOrange
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

                // ── Error Snackbar-style chip ────────────────────────────────
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter   = fadeIn(),
                    exit    = fadeOut()
                ) {
                    if (errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EnergyOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text     = errorMessage,
                                color    = EnergyOrange,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        actionButton = {
            // ── Mic Button with Pulsing Ring ─────────────────────────────────
            val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue  = if (isRecording) 1.35f else 1f,
                animationSpec = infiniteRepeatable(
                    animation  = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue  = if (isRecording) 0.1f else 0f,
                animationSpec = infiniteRepeatable(
                    animation  = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlpha"
            )

            val micEnabled = !isLoading && !isSuccess
            val micColor   = when {
                !micEnabled -> Disabled
                isRecording -> EnergyOrange
                else        -> FriendlyPurple
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                // Pulsing outer ring during active recording
                if (isRecording) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .scale(pulseScale)
                            .alpha(pulseAlpha)
                            .clip(CircleShape)
                            .background(EnergyOrange)
                    )
                }

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