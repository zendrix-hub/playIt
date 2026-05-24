package com.playit.app.ui.findit

import android.app.Application
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playit.app.ui.components.PlayItLearningScaffold

@Composable
fun FindItScreen(
    phonemeId: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: FindItViewModel = viewModel(
        factory = FindItViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            phonemeId = phonemeId
        )
    )
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // ── Adapter Logic: Mapping your existing ViewModel state to the new UI ──

    // Extract 3 options (letters) from your existing gridItems list
    val options: List<String> = remember(state.gridItems, phonemeId) {
        if (state.gridItems.size >= 3) {
            // The new UI uses single letters, so we take the first letter of the word
            state.gridItems.take(3).map { it.word.take(1).lowercase() }
        } else {
            listOf(phonemeId.lowercase(), "s", "m") // Fallback
        }
    }

    // Find the most recently tapped item's letter
    val selectedOption = remember(state.tapResults, state.gridItems) {
        val selectedOptionId = state.tapResults.entries.lastOrNull()?.key
        state.gridItems.find { it.id == selectedOptionId }?.word?.take(1)?.lowercase()
    }

    // Success is true if the ViewModel marked it complete, or if any tap was correct
    val isSuccess = state.isComplete || state.tapResults.values.any { it == true }

    FindItContent(
        targetPhoneme = phonemeId,
        options = options,
        selectedOption = selectedOption,
        isSuccess = isSuccess,
        activeHearts = state.hearts,
        onOptionSelected = { tappedLetter ->
            // Pass the selection back to your existing ViewModel logic by matching the letter
            val item = state.gridItems.find { it.word.take(1).lowercase() == tappedLetter.lowercase() }
            if (item != null) {
                viewModel.onCardTapped(item)
            }
        },
        onReplayAudioClick = {
            // TODO: Trigger your audio player to replay the sound
        },
        onBackClick = onBack,
        onNextClick = onNext
    )
}

@Composable
fun FindItContent(
    targetPhoneme: String, // The correct answer the child is listening for
    options: List<String>, // List of 3 letters (e.g., ["m", "s", "a"])
    selectedOption: String?, // Null if they haven't tapped one yet
    isSuccess: Boolean, // True if they tapped the correct targetPhoneme
    activeHearts: Int,
    onOptionSelected: (String) -> Unit,
    onReplayAudioClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Design Tokens
    val CardBg       = Color(0xFFFFFFFF)
    val TextDark     = Color(0xFF2D2D2D)
    val CorrectGreen = Color(0xFF43E97B)
    val WrongGray    = Color(0xFFE0E0E0) // Soft, non-punitive gray for incorrect taps
    val ReplayBlue   = Color(0xFF48C6EF)

    PlayItLearningScaffold(
        title = "FIND IT",
        activeHearts = activeHearts,
        isNextEnabled = isSuccess,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        centerContent = {
            // THE LINEUP: 3 Options displayed side-by-side
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    val isSelected = selectedOption == option
                    val isCorrect = option == targetPhoneme

                    // Non-punitive color logic:
                    // If selected and correct -> Green
                    // If selected and wrong -> Soft Gray
                    // Otherwise -> White
                    val targetColor = when {
                        isSelected && isCorrect -> CorrectGreen
                        isSelected && !isCorrect -> WrongGray
                        else -> CardBg
                    }

                    val animatedColor by animateColorAsState(targetValue = targetColor, label = "cardColor")

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = animatedColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 12.dp),
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(if (isSelected) 4.dp else 12.dp, RoundedCornerShape(24.dp))
                            .clickable(enabled = !isSuccess) { // Disable clicking once they get it right
                                onOptionSelected(option)
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = option.uppercase(),
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected && isCorrect) Color.White else TextDark
                            )
                        }
                    }
                }
            }
        },
        actionButton = {
            // Action Zone: Replay Audio Button
            // Ensures the child can always hear the prompt again if they forget
            Surface(
                shape = CircleShape,
                color = ReplayBlue,
                onClick = onReplayAudioClick,
                shadowElevation = 8.dp,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Hear Again",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    )
}