package com.playit.app.ui.blendit

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.components.PlayItLearningScaffold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BlendItScreen(
    viewModel: BlendItViewModel,
    onBack: () -> Unit,
    onSessionComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Local state to drive the magnetic snap animation
    var isBlending by remember { mutableStateOf(false) }
    var hasCompleted by remember { mutableStateOf(false) }

    // Reset animation state when moving to a new word
    LaunchedEffect(uiState.currentWordIndex) {
        isBlending = false
        hasCompleted = false
    }

    // Safely extract the current target word (fallback to "sam" if empty for MVP testing)
    val currentWord = if (uiState.targetWords.isNotEmpty() && uiState.currentWordIndex < uiState.targetWords.size) {
        uiState.targetWords[uiState.currentWordIndex].toString()
    } else {
        "sam"
    }

    BlendItContent(
        word = currentWord,
        isBlending = isBlending,
        hasCompleted = hasCompleted,
        activeHearts = uiState.hearts,
        onBlendClick = {
            if (!isBlending && !hasCompleted) {
                isBlending = true
                viewModel.replayAudio() // Trigger your audio model

                // Simulate the duration of the audio playing the blend
                coroutineScope.launch {
                    delay(800L + (currentWord.length * 400L)) // e.g., ~2 seconds for "sam"
                    isBlending = false
                    hasCompleted = true
                }
            }
        },
        onBackClick = onBack,
        onNextClick = {
            // Check if this was the last word
            if (uiState.currentWordIndex >= uiState.targetWords.size - 1) {
                onSessionComplete()
            } else {
                // Trigger your existing ViewModel logic to advance the word
                viewModel.onSubmitClicked()
            }
        }
    )
}

@Composable
fun BlendItContent(
    word: String,           // The target word (e.g., "sam")
    isBlending: Boolean,    // True when the audio is actively playing the blend
    hasCompleted: Boolean,  // True when the full blend audio has finished
    activeHearts: Int,
    onBlendClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Design Tokens
    val CardBg       = Color(0xFFFFFFFF)
    val TextDark     = Color(0xFF2D2D2D)
    val ActionPurple = Color(0xFF6C63FF)
    val ActionActive = Color(0xFF43E97B)
    val Sunshine     = Color(0xFFFFD93D)

    PlayItLearningScaffold(
        title = "BLEND IT",
        activeHearts = activeHearts,
        isNextEnabled = hasCompleted,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        centerContent = {
            // THE SQUISH ANIMATION:
            // When idle, letters are spaced 24dp apart.
            // When blending, they slide together to 0dp spacing!
            val letterSpacing by animateDpAsState(
                targetValue = if (isBlending || hasCompleted) 0.dp else 24.dp,
                animationSpec = tween(durationMillis = 800),
                label = "spacing"
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(letterSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Generate a card for every letter in the target word
                word.forEach { letter ->
                    Card(
                        shape = RoundedCornerShape(16.dp), // Slightly sharper corners so they fit together like blocks
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        modifier = Modifier
                            .width(80.dp)
                            .height(120.dp)
                            .shadow(12.dp, RoundedCornerShape(16.dp))
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            // Add the sunny background highlight if it's currently blending
                            if (isBlending || hasCompleted) {
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .background(Sunshine.copy(alpha = 0.2f)))
                            }
                            Text(
                                text = letter.toString().uppercase(),
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Black,
                                color = TextDark
                            )
                        }
                    }
                }
            }
        },
        actionButton = {
            // Action Zone: The Blend Trigger
            Surface(
                shape = CircleShape,
                color = if (isBlending) ActionActive else ActionPurple,
                onClick = onBlendClick,
                shadowElevation = 8.dp,
                modifier = Modifier.size(88.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Blend Word",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    )
}