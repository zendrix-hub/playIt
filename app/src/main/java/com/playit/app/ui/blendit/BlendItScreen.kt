package com.playit.app.ui.blendit

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.playit.app.domain.model.LetterCard
import com.playit.app.ui.components.AnswerFeedback
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.PlayItLearningScaffold
import com.playit.app.ui.components.SecondaryButton
import kotlinx.coroutines.delay

import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.Border
import com.playit.app.ui.theme.Disabled
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.GentleCorrectionOrange
import com.playit.app.ui.theme.TextPrimary

// ─── Design Tokens (Design System v1.0) ───
private val SoftPurpleGradientTop = Color(0xFFECE7FF)
private val SoftPurpleGradientBottom = Color(0xFFD8D0FF)
private val GentleOrange          = GentleCorrectionOrange // Gentle Correction Orange (never harsh red)
private val GentleOrangeBg        = Color(0xFFFFF3E0)
private val BorderColor           = Border
private val DisabledColor         = Disabled

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

    var wrongAttempts by remember(uiState.currentWordIndex) { mutableIntStateOf(0) }

    // Track wrong attempts for auto-hint feature
    LaunchedEffect(uiState.isError) {
        if (uiState.isError) {
            wrongAttempts++
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
        wrongAttempts = wrongAttempts,
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
    wrongAttempts: Int = 0,
    onLetterTapped: (LetterCard) -> Unit,
    onResetClick: () -> Unit,
    onBlendClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // ─── Animation Drivers ───
    val isSnapped = hasCompleted || isBlending
    val isAutoHintActive = wrongAttempts >= 2 && !hasCompleted && spelledLetters.size < word.length

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

    // Pulse animation for Auto-hint visual assist
    val infiniteTransition = rememberInfiniteTransition(label = "hintPulse")
    val hintPulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hintPulseScale"
    )

    // Friendly Purple Gradient Background
    val purpleBgGradient = Brush.verticalGradient(
        listOf(SoftPurpleGradientTop, SoftPurpleGradientBottom)
    )

    val mascotState = when {
        hasCompleted -> MascotState.Celebrating
        activeHearts == 0 -> MascotState.Encouraging
        isError -> MascotState.Encouraging
        isAutoHintActive -> MascotState.Thinking
        wrongAttempts > 0 -> MascotState.Encouraging
        else -> MascotState.Happy
    }

    val mascotMessage = when {
        hasCompleted -> "Awesome word blending! Tap NEXT to continue."
        activeHearts == 0 -> "Great effort! You're getting closer every time. Let me give you a hand!"
        isError -> "Good try! Tap RESET or try placing the letters again."
        isAutoHintActive -> "Need a hint? Tap the glowing letter to help spell the word!"
        wrongAttempts > 0 -> "Keep going! Tap RESET if you want to start over."
        else -> "Tap the letters in order to spell the word!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleBgGradient)
    ) {
        PlayItLearningScaffold(
            title = "WORD CHALLENGE",
            activeHearts = activeHearts,
            isNextEnabled = hasCompleted,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
            centerContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    // ── 0. Mascot Guidance Bubble ──
                    MascotBubble(
                        state = mascotState,
                        message = mascotMessage,
                        audioResId = 0
                    )

                    // ── 0b. Permanently Visible Audio Control ──
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        SecondaryButton(
                            text = "Listen Word 🔊",
                            onClick = onBlendClick,
                            modifier = Modifier.height(48.dp)
                        )
                    }

                    // ── 1. Target Spelling Slots ──
                    val feedbackState = when {
                        hasCompleted -> true
                        isError -> false
                        else -> null
                    }

                    AnswerFeedback(isCorrect = feedbackState) {
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
                                            containerColor = if (isError) GentleOrangeBg else Color.White
                                        ),
                                        border = if (isError) BorderStroke(3.dp, GentleOrange) else BorderStroke(1.dp, BorderColor),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                        modifier = Modifier
                                            .width(76.dp)
                                            .height(104.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                            if (isSnapped) {
                                                Box(modifier = Modifier.fillMaxSize().background(AchievementGold.copy(alpha = 0.2f)))
                                            }
                                            Text(
                                                text = spelledCard.char.uppercase(),
                                                fontSize = 48.sp,
                                                fontWeight = FontWeight.Black,
                                                color = TextPrimary
                                            )
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .width(76.dp)
                                            .height(104.dp)
                                            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                                            .border(BorderStroke(2.dp, FriendlyPurple.copy(alpha = 0.4f)), RoundedCornerShape(20.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "?",
                                            color = FriendlyPurple.copy(alpha = 0.6f),
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ── 2. Scrambled Selection Pile (with Auto-hint assist glow) ──
                    val nextExpectedChar = word.getOrNull(spelledLetters.size)?.toString()

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        scrambledLetters.forEach { card ->
                            val isUsed = card.isUsed
                            val isNextTarget = isAutoHintActive && !isUsed && card.char.equals(nextExpectedChar, ignoreCase = true)

                            val cardModifier = if (isNextTarget) {
                                Modifier
                                    .scale(hintPulseScale)
                                    .width(76.dp)
                                    .height(104.dp)
                                    .shadow(12.dp, RoundedCornerShape(20.dp))
                                    .border(BorderStroke(3.dp, AchievementGold), RoundedCornerShape(20.dp))
                            } else {
                                Modifier
                                    .width(76.dp)
                                    .height(104.dp)
                                    .shadow(if (isUsed) 0.dp else 4.dp, RoundedCornerShape(20.dp))
                                    .border(
                                        if (isUsed) BorderStroke(1.dp, Color.Transparent) else BorderStroke(1.dp, BorderColor),
                                        RoundedCornerShape(20.dp)
                                    )
                            }

                            Card(
                                onClick = { onLetterTapped(card) },
                                enabled = !isUsed,
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isUsed) DisabledColor.copy(alpha = 0.3f) else Color.White,
                                    disabledContainerColor = DisabledColor.copy(alpha = 0.3f)
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = if (isUsed) 0.dp else 6.dp,
                                    disabledElevation = 0.dp
                                ),
                                modifier = cardModifier
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = card.char.uppercase(),
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isUsed) DisabledColor else TextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Reset Action Button
                    if (!hasCompleted && spelledLetters.isNotEmpty()) {
                        Button(
                            onClick = onResetClick,
                            colors = ButtonDefaults.buttonColors(containerColor = FriendlyPurple),
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier
                                .height(44.dp)
                                .padding(horizontal = 16.dp)
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
                        color = FriendlyPurple,
                        onClick = onBlendClick,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Blend word audio",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}