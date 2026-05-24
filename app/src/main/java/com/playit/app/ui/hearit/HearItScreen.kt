package com.playit.app.ui.hearit

import android.app.Application
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playit.app.ui.components.PlayItLearningScaffold

// ─── Design Tokens ────────────────────────────────────────────────────────────

private val CardBg      = Color(0xFFFFFFFF)
private val Sunshine    = Color(0xFFFFD93D)
private val TextDark    = Color(0xFF2D2D2D)
private val PlayIdle    = Color(0xFF6C63FF)   // purple — not playing
private val PlayActive  = Color(0xFF43C6AC)   // teal — audio playing

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
        activeHearts  = 0,              // Hear It has no hearts — display none
        isNextEnabled = isNextEnabled,
        onBackClick   = onBackClick,
        onNextClick   = onNextClick,
        centerContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // ── Phoneme Card ─────────────────────────────────────────────
                Card(
                    shape     = RoundedCornerShape(40.dp),
                    colors    = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                    modifier  = Modifier.size(220.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier         = Modifier.fillMaxSize()
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

                // ── Status text ──────────────────────────────────────────────
                Text(
                    text       = if (isPlaying) "Playing..." else "Tap to hear the sound",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color      = if (isPlaying) PlayActive else Color.White
                )

                // ── Error chip ───────────────────────────────────────────────
                if (errorMessage != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFF4B6E).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text     = errorMessage,
                            color    = Color(0xFFFF4B6E),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        },
        actionButton = {
            // ── Speaker Button with pulse while playing ───────────────────
            val infiniteTransition = rememberInfiniteTransition(label = "speakerPulse")
            val scale by infiniteTransition.animateFloat(
                initialValue  = 1f,
                targetValue   = if (isPlaying) 1.2f else 1f,
                animationSpec = infiniteRepeatable(
                    animation  = tween(500),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "speakerScale"
            )

            Surface(
                shape           = CircleShape,
                color           = if (isPlaying) PlayActive else PlayIdle,
                onClick         = onPlayClick,
                shadowElevation = 8.dp,
                modifier        = Modifier
                    .size(88.dp)
                    .scale(scale)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector        = Icons.Filled.VolumeUp,
                        contentDescription = "Play phoneme sound",
                        tint               = Color.White,
                        modifier           = Modifier.size(48.dp)
                    )
                }
            }
        }
    )
}