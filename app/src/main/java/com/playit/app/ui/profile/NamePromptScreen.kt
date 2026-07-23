package com.playit.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.components.LearningCard
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.PrimaryButton
import com.playit.app.ui.theme.*
import com.playit.app.ui.util.tapFeedback

/**
 * Task UI-5.03 — Polish NamePromptScreen to Design System v1.0
 *
 * Implements:
 * - Full token/component compliance for name + avatar entry form
 * - Keyboard-overlap resilience via verticalScroll + imePadding
 * - PrimaryButton (UI-4.01) for Continue ("LET'S PLAY!")
 * - Avatar picker with 72dp tappable tiles (>= 64dp TouchTarget.IMPORTANT)
 * - Real-time avatar preview next to the name input field
 * - High-contrast labels (>= 4.5:1) and minimum 16sp font sizes
 * - MascotBubble (UI-4.07) dynamic reaction (Happy -> Excited)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamePromptScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onProfileCreated: () -> Unit
) {
    var nameText by remember { mutableStateOf("") }
    var selectedAvatarId by remember { mutableStateOf(1) }
    val scrollState = rememberScrollState()

    val bgBrush = Brush.verticalGradient(
        colors = listOf(SoftSky, CreamWhite)
    )

    val selectedAvatar = AvatarPresets.find { it.id == selectedAvatarId } ?: AvatarPresets[0]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CREATE PROFILE",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(TouchTarget.MINIMUM)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = PlayItSpacing.cardPadding, vertical = PlayItSpacing.default)
        ) {
            // Mascot guidance bubble with dynamic excited state
            val mascotState = if (nameText.trim().isNotEmpty()) MascotState.Excited else MascotState.Happy
            val mascotMessage = if (nameText.trim().isNotEmpty()) {
                "Awesome name, ${nameText.trim()}! Ready to start?"
            } else {
                "Pick your favorite hero avatar and tell me your name!"
            }

            MascotBubble(
                state = mascotState,
                message = mascotMessage,
                audioResId = 0,
                autoPlayAudio = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(PlayItSpacing.default))

            // Avatar selection section
            LearningCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PlayItSpacing.default)
                ) {
                    Text(
                        text = "Choose your character:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(PlayItSpacing.default))

                    // Avatar grid (2 rows of 3) using simple Rows for optimal scroll & keyboard behavior
                    val avatarRows = AvatarPresets.chunked(3)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(PlayItSpacing.default),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        avatarRows.forEach { rowItems ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                rowItems.forEach { avatar ->
                                    val isSelected = selectedAvatarId == avatar.id
                                    val interactionSource = remember { MutableInteractionSource() }

                                    val borderModifier = if (isSelected) {
                                        Modifier.border(4.dp, LearningBlue, CircleShape)
                                    } else {
                                        Modifier.border(2.dp, Border, CircleShape)
                                    }

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(72.dp) // Exceeds TouchTarget.IMPORTANT (64dp)
                                            .tapFeedback(
                                                interactionSource = interactionSource,
                                                pressedScale = 0.92f,
                                                restingScale = if (isSelected) 1.05f else 1.0f
                                            )
                                            .clip(CircleShape)
                                            .background(avatar.color)
                                            .then(borderModifier)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = { selectedAvatarId = avatar.id }
                                            )
                                    ) {
                                        Icon(
                                            imageVector = avatar.icon,
                                            contentDescription = avatar.name,
                                            tint = Color.White,
                                            modifier = Modifier.size(38.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(PlayItSpacing.default))

            // Name Input section with Real-time Avatar Preview
            LearningCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PlayItSpacing.default)
                ) {
                    Text(
                        text = "What is your name?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(PlayItSpacing.small))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(PlayItSpacing.default),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Real-time selected avatar preview
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(selectedAvatar.color)
                                .border(3.dp, LearningBlue, CircleShape)
                        ) {
                            Icon(
                                imageVector = selectedAvatar.icon,
                                contentDescription = "Selected Avatar: ${selectedAvatar.name}",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        OutlinedTextField(
                            value = nameText,
                            onValueChange = { if (it.length <= 15) nameText = it },
                            placeholder = {
                                Text(
                                    text = "Enter your name",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = LearningBlue,
                                unfocusedBorderColor = Border
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(PlayItSpacing.section))

            // PrimaryButton for Continue ("LET'S PLAY!")
            PrimaryButton(
                text = "LET'S PLAY!",
                enabled = nameText.trim().isNotEmpty(),
                onClick = {
                    viewModel.createProfile(nameText.trim(), selectedAvatarId) { _ ->
                        onProfileCreated()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = PlayItSpacing.cardPadding)
            )
        }
    }
}
