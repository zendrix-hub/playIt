package com.playit.app.ui.findit

import android.app.Application
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playit.app.PlayItApplication
import com.playit.app.ui.components.LearningCard
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.PlayItLearningScaffold
import com.playit.app.ui.theme.*

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

    // Extract options (letters) from existing gridItems list
    val options: List<String> = remember(state.gridItems, phonemeId) {
        if (state.gridItems.isNotEmpty()) {
            state.gridItems.map { it.word.take(1).lowercase() }
        } else {
            listOf(phonemeId.lowercase(), "s", "m") // Fallback
        }
    }

    // Success is true if ViewModel marked complete or if any tap result is true
    val isSuccess = state.isComplete || state.tapResults.values.any { it == true }
    val hasWrongTap = state.tapResults.values.any { it == false }

    FindItContent(
        targetPhoneme = phonemeId,
        options = options,
        tapResults = state.tapResults,
        isSuccess = isSuccess,
        hasWrongTap = hasWrongTap,
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
    tapResults: Map<String, Boolean>,
    isSuccess: Boolean,
    hasWrongTap: Boolean,
    activeHearts: Int,
    onOptionSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    // Mascot state selection based on session outcome
    val mascotState = when {
        isSuccess -> MascotState.Happy
        hasWrongTap -> MascotState.Encouraging
        else -> MascotState.Thinking
    }

    val mascotMessage = when {
        isSuccess -> "Great job! You found the letter ${targetPhoneme.uppercase()}!"
        hasWrongTap -> "Keep going! Try another card to find ${targetPhoneme.uppercase()}."
        else -> "Find the letter ${targetPhoneme.uppercase()}!"
    }

    PlayItLearningScaffold(
        title = "FIND IT",
        activeHearts = activeHearts,
        isNextEnabled = isSuccess,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        centerContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PlayItSpacing.default),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PlayItSpacing.default)
            ) {
                // Mascot prompt bubble
                MascotBubble(
                    state = mascotState,
                    message = mascotMessage,
                    audioResId = 0
                )

                // Progress Indicator
                Surface(
                    color = SoftSky,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Target:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Text(
                            text = targetPhoneme.uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LearningBlue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSuccess) "1 of 1 found" else "0 of 1 found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }

                // Options Row (Grid of options with 16dp spacing, generous 100dp touch targets)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(PlayItSpacing.default),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    options.forEach { option ->
                        val tapResult = tapResults[option]
                        val isTapped = tapResult != null
                        val isCorrect = tapResult == true
                        val isWrong = tapResult == false

                        val cardBgColor = when {
                            isCorrect -> GrowthGreen
                            isWrong -> Disabled
                            else -> CreamWhite
                        }

                        val animatedBgColor by animateColorAsState(
                            targetValue = cardBgColor,
                            label = "cardBgColor"
                        )

                        // Accessibility description
                        val cdState = when {
                            isCorrect -> "correct"
                            isWrong -> "incorrect"
                            else -> "available"
                        }
                        val accessibilityDesc = "Letter ${option.uppercase()}, $cdState"

                        LearningCard(
                            onClick = if (!isSuccess && !isTapped) {
                                { onOptionSelected(option) }
                            } else null,
                            enabled = !isSuccess && !isTapped,
                            modifier = Modifier
                                .size(100.dp)
                                .alpha(if (isWrong) 0.6f else 1.0f)
                                .semantics { contentDescription = accessibilityDesc }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(animatedBgColor)
                            ) {
                                Text(
                                    text = option.uppercase(),
                                    fontSize = 54.sp,
                                    fontWeight = FontWeight.Black,
                                    color = when {
                                        isCorrect -> Color.White
                                        isWrong -> TextSecondary
                                        else -> TextPrimary
                                    }
                                )

                                // Non-color icon indicators for accessibility (grayscale legibility)
                                if (isCorrect) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(24.dp)
                                            .background(Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Correct",
                                            tint = GrowthGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                } else if (isWrong) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(24.dp)
                                            .background(TextSecondary, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Incorrect",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        actionButton = {}
    )
}