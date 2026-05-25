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

    val currentWord = if (uiState.targetWords.isNotEmpty() && uiState.currentWordIndex < uiState.targetWords.size) {
        uiState.targetWords[uiState.currentWordIndex]
    } else "sam"

    BlendItContent(
        word = currentWord,
        isBlending = uiState.isBlending,
        hasCompleted = uiState.hasCompleted,
        activeHearts = uiState.hearts,
        onBlendClick = {
            if (!uiState.isBlending && !uiState.hasCompleted) {
                viewModel.startBlending()
            }
        },
        onBackClick = onBack,
        onNextClick = {
            // Only allow navigation if we aren't currently playing audio
            if (!uiState.isBlending) {
                if (uiState.currentWordIndex >= uiState.targetWords.size - 1) {
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
    isBlending: Boolean,
    hasCompleted: Boolean,
    activeHearts: Int,
    onBlendClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // ─── Animation Drivers ───
    val isSnapped = isBlending || hasCompleted

    // 1. Magnetic Spacing: Cards slide together
    val letterSpacing by animateDpAsState(
        targetValue = if (isSnapped) 0.dp else 24.dp,
        animationSpec = tween(800),
        label = "magneticSnap"
    )

    // 2. Corner Morphing: Sharpens the blocks to look like a single pill
    val cornerRadius by animateDpAsState(
        targetValue = if (isSnapped) 4.dp else 24.dp,
        animationSpec = tween(800),
        label = "cornerMorph"
    )

    PlayItLearningScaffold(
        title = "BLEND IT",
        activeHearts = activeHearts,
        isNextEnabled = hasCompleted,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        centerContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(letterSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                word.forEach { letter ->
                    Card(
                        // Apply the dynamic corner radius
                        shape = RoundedCornerShape(cornerRadius),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        modifier = Modifier
                            .width(85.dp)
                            .height(130.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            // Highlight the background during the blend
                            if (isSnapped) {
                                Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFFD93D).copy(alpha = 0.15f)))
                            }
                            Text(
                                text = letter.toString().uppercase(),
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF2D2D2D)
                            )
                        }
                    }
                }
            }
        },
        actionButton = {
            Surface(
                shape = CircleShape,
                color = if (isBlending) Color(0xFF43E97B) else Color(0xFF6C63FF),
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
    )
}