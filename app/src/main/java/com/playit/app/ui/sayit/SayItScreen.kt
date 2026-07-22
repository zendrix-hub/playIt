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
import com.playit.app.ui.components.PlayItLearningScaffold

// ─── Design Tokens ────────────────────────────────────────────────────────────

private val RecordActive  = Color(0xFFFF4B6E)   // pink/red — listening
private val RecordIdle    = Color(0xFF6C63FF)   // purple — idle
private val SuccessGreen  = Color(0xFF4CAF50)
private val ErrorRed      = Color(0xFFFF4B6E)
private val CardBg        = Color(0xFFFFFFFF)
private val Sunshine      = Color(0xFFFFD93D)
private val TextDark      = Color(0xFF2D2D2D)

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
        phonemeId    = phonemeId,
        isLoading    = uiState.isModelLoading,
        isRecording  = uiState.isRecording,
        isSuccess    = uiState.isSuccess,
        activeHearts = uiState.activeHearts,
        partialText  = uiState.partialText,
        resultText   = uiState.resultText,
        errorMessage = uiState.errorMessage,
        isTooNoisy   = uiState.isTooNoisy,
        onBackClick  = onBack,
        onRecordClick = ::handleMicClick,
        onNextClick  = onNext
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
        title        = "SAY IT",
        activeHearts = activeHearts,
        isNextEnabled = isSuccess,
        onBackClick  = onBackClick,
        onNextClick  = onNextClick,
        centerContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // ── Ambient Noise Alert Banner ────────────────────────────────
                if (isTooNoisy) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Sunshine.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Sunshine),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "⚠️ It sounds a bit noisy here. Move to a quieter spot for best results.",
                            color = TextDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }

                // ── Phoneme Card ─────────────────────────────────────────────
                val cardBorderColor = when {
                    isSuccess                                   -> SuccessGreen
                    resultText.isNotEmpty() && !isSuccess       -> ErrorRed
                    else                                        -> Color.Transparent
                }

                Card(
                    shape    = RoundedCornerShape(40.dp),
                    colors   = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                    border   = if (cardBorderColor != Color.Transparent)
                        androidx.compose.foundation.BorderStroke(4.dp, cardBorderColor)
                    else null,
                    modifier = Modifier.size(220.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(Sunshine.copy(alpha = 0.35f))
                        )
                        Text(
                            text       = phonemeId.uppercase(),
                            fontSize   = 110.sp,
                            fontWeight = FontWeight.Black,
                            color      = TextDark
                        )
                    }
                }

                // ── Status Text ──────────────────────────────────────────────
                val statusText = when {
                    isLoading                   -> "Loading speech model..."
                    isSuccess                   -> "🎉 Great job!"
                    resultText.isNotEmpty()     -> "I heard: \"$resultText\" — try again!"
                    partialText.isNotEmpty()    -> partialText
                    isRecording                 -> "Listening..."
                    else                        -> "Tap the mic and say the sound"
                }

                val statusColor = when {
                    isSuccess                   -> SuccessGreen
                    resultText.isNotEmpty()
                            && !isSuccess           -> ErrorRed
                    isRecording                 -> RecordActive
                    else                        -> Color.White
                }

                Text(
                    text       = statusText,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color      = statusColor,
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.padding(horizontal = 16.dp)
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
                            color = ErrorRed.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text     = errorMessage,
                                color    = ErrorRed,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        actionButton = {
            // ── Mic Button ───────────────────────────────────────────────────
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue  = if (isRecording) 1.25f else 1f,
                animationSpec = infiniteRepeatable(
                    animation  = tween(400),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "micScale"
            )

            val micEnabled  = !isLoading && !isSuccess
            val micColor    = when {
                !micEnabled  -> Color.Gray
                isRecording  -> RecordActive
                else         -> RecordIdle
            }

            Surface(
                shape          = CircleShape,
                color          = micColor,
                onClick        = { if (micEnabled) onRecordClick() },
                shadowElevation = 8.dp,
                modifier       = Modifier
                    .size(88.dp)
                    .scale(if (isRecording) scale else 1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector     = if (isRecording) Icons.Filled.Mic else Icons.Filled.MicOff,
                        contentDescription = if (isRecording) "Stop recording" else "Start recording",
                        tint            = Color.White,
                        modifier        = Modifier.size(48.dp)
                    )
                }
            }
        }
    )
}