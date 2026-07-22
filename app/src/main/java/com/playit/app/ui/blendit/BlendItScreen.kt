package com.playit.app.ui.blendit

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.playit.app.domain.usecase.LetterCard
import com.playit.app.ui.components.PlayItLearningScaffold
import kotlinx.coroutines.delay

// ─── Design Tokens ───

private val RecordActive  = Color(0xFFFF4B6E)
private val RecordIdle    = Color(0xFF6C63FF)
private val Sunshine      = Color(0xFFFFD93D)
private val TextDark      = Color(0xFF2D2D2D)
private val ErrorBg       = Color(0xFFFFECEF)
private val ErrorBorder   = Color(0xFFFF4B6E)

@Composable
fun BlendItScreen(
    viewModel: BlendItViewModel,
    onBack: () -> Unit,
    onSessionComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentWord = if (uiState.targetWords.isNotEmpty() && uiState.currentWordIndex < uiState.targetWords.size) {
        uiState.targetWords[uiState.currentWordIndex]
    } else "sam"

    // Reset spelling automatically if error state is triggered
    LaunchedEffect(uiState.isError) {
        if (uiState.isError) {
            delay(1200)
            viewModel.onResetSpelling()
        }
    }

    BlendItContent(
        word = currentWord,
        scrambledLetters = uiState.scrambledLetters,
        spelledLetters = uiState.spelledLetters,
        isError = uiState.isError,
        isBlending = uiState.isBlending,
        hasCompleted = uiState.hasCompleted,
        activeHearts = uiState.hearts,
        onLetterTapped = viewModel::onLetterTapped,
        onResetClick = viewModel::onResetSpelling,
        onBlendClick = {
            if (!uiState.isBlending) {
                viewModel.startBlending()
            }
        },
        onBackClick = onBack,
        onNextClick = {
            if (!uiState.isBlending) {
                if (uiState.currentWordIndex >= uiState.targetWords.size - 1) {
                    viewModel.completeSession()
                    onSessionComplete()
                } else {
                    viewModel.onSubmitClicked()
                }
            }
        }
    )
}

@Composable
fun BlendItContent(
    word: String,
    scrambledLetters: List<LetterCard>,
    spelledLetters: List<LetterCard>,
    isError: Boolean,
    isBlending: Boolean,
    hasCompleted: Boolean,
    activeHearts: Int,
    onLetterTapped: (LetterCard) -> Unit,
    onResetClick: () -> Unit,
    onBlendClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // ─── Animation Drivers ───
    val isSnapped = hasCompleted || isBlending

    val letterSpacing by animateDpAsState(
        targetValue = if (isSnapped) 0.dp else 16.dp,
        animationSpec = tween(600),
        label = "magneticSnap"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (isSnapped) 4.dp else 20.dp,
        animationSpec = tween(600),
        label = "cornerMorph"
    )

    PlayItLearningScaffold(
        title = "BLEND IT",
        activeHearts = activeHearts,
        isNextEnabled = hasCompleted,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        centerContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                // ── 1. Target Spelling Slots ──
                Row(
                    horizontalArrangement = Arrangement.spacedBy(letterSpacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until word.length) {
                        val spelledCard = spelledLetters.getOrNull(i)
                        if (spelledCard != null) {
                            Card(
                                shape = RoundedCornerShape(cornerRadius),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isError) ErrorBg else Color.White
                                ),
                                border = if (isError) BorderStroke(2.dp, ErrorBorder) else null,
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                modifier = Modifier
                                    .width(76.dp)
                                    .height(110.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    if (isSnapped) {
                                        Box(modifier = Modifier.fillMaxSize().background(Sunshine.copy(alpha = 0.15f)))
                                    }
                                    Text(
                                        text = spelledCard.char.uppercase(),
                                        fontSize = 54.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextDark
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(76.dp)
                                    .height(110.dp)
                                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                    .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.2f)), RoundedCornerShape(20.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "?",
                                    color = Color.White.copy(alpha = 0.3f),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Instruction Label
                Text(
                    text = if (hasCompleted) "🎉 Correct! Press next to advance" else "Tap letters below in the correct order:",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                // ── 2. Scrambled Selection Pile ──
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    scrambledLetters.forEach { card ->
                        val isUsed = card.isUsed
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUsed) Color.DarkGray.copy(alpha = 0.3f) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isUsed) 0.dp else 8.dp),
                            modifier = Modifier
                                .width(76.dp)
                                .height(110.dp)
                                .shadow(if (isUsed) 0.dp else 4.dp, RoundedCornerShape(20.dp))
                                .clickable(enabled = !isUsed) { onLetterTapped(card) }
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = card.char.uppercase(),
                                    fontSize = 54.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isUsed) Color.LightGray.copy(alpha = 0.3f) else TextDark
                                )
                            }
                        }
                    }
                }

                // Reset Action Button
                if (!hasCompleted && spelledLetters.isNotEmpty()) {
                    Button(
                        onClick = onResetClick,
                        colors = ButtonDefaults.buttonColors(containerColor = RecordActive),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Reset Spelling 🔄", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        actionButton = {
            if (hasCompleted) {
                Surface(
                    shape = CircleShape,
                    color = if (isBlending) Color(0xFF43E97B) else RecordIdle,
                    onClick = onBlendClick,
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(88.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Blend",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    )
}