package com.playit.app.ui.findit

import android.app.Application
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.playit.app.PlayItApplication

@Composable
fun FindItScreen(
    phonemeId: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: FindItViewModel = viewModel(
        factory = FindItViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            repository = (LocalContext.current.applicationContext as PlayItApplication).repository,
            phonemeId = phonemeId
        )
    )
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Extract 3 options (letters) from your existing gridItems list
    val options: List<String> = remember(state.gridItems, phonemeId) {
        if (state.gridItems.size >= 3) {
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
            val item = state.gridItems.find { it.word.take(1).lowercase() == tappedLetter.lowercase() }
            if (item != null) {
                viewModel.onCardTapped(item)
            }
        },
        onBackClick = onBack,
        onNextClick = onNext
    )
}

@Composable
fun FindItContent(
    targetPhoneme: String,
    options: List<String>,
    selectedOption: String?,
    isSuccess: Boolean,
    activeHearts: Int,
    onOptionSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Design Tokens
    val CardBg       = Color(0xFFFFFFFF)
    val TextDark     = Color(0xFF2D2D2D)
    val CorrectGreen = Color(0xFF43E97B)
    val WrongGray    = Color(0xFFE0E0E0)

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
                            .clickable(enabled = !isSuccess) {
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
        // FIX: The actionButton slot has been completely removed.
        // This completely cleans up the blue replay speaker floating button.
        actionButton = {}
    )
}