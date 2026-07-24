package com.playit.app.ui.profile

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.playit.app.ui.a11y.ReducedMotionState
import com.playit.app.ui.components.ArithmeticGateDialog
import com.playit.app.ui.components.EmptyState
import com.playit.app.ui.components.LearningCard
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItColor
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.SoftSky
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary
import com.playit.app.ui.theme.TouchTarget
import com.playit.app.ui.theme.cardElevation
import com.playit.app.ui.util.tapFeedback
import java.util.Locale

data class AvatarItem(val id: Int, val icon: ImageVector, val color: Color, val name: String)

val AvatarPresets = listOf(
    AvatarItem(1, Icons.Default.Face, PlayItColor.friendlyPurple, "Purple Face"),
    AvatarItem(2, Icons.Default.Star, PlayItColor.achievementGold, "Gold Star"),
    AvatarItem(3, Icons.Default.Favorite, PlayItColor.correctionOrange, "Orange Heart"),
    AvatarItem(4, Icons.Default.ThumbUp, PlayItColor.growthGreen, "Green Thumb"),
    AvatarItem(5, Icons.Default.Home, PlayItColor.learningBlue, "Blue House"),
    AvatarItem(6, Icons.Default.Face, PlayItColor.energyOrange, "Orange Face")
)

/**
 * Task UI-5.02 / D4 — ProfileSelectScreen Refactoring
 *
 * Design System Compliance:
 * - Profile avatar grid enforcing strict 16dp spacing (`PlayItSpacing.cardPadding`) between cards
 * - Touch targets >= 54dp minimum (72dp avatar circles within 180dp cards)
 * - Tactile spring feedback on profile selection
 * - TextToSpeech spoken confirmation for early readers
 * - Parent arithmetic verification gate on profile deletion
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileSelectScreen(
    viewModel: ProfileViewModel,
    onProfileSelected: () -> Unit,
    onNavigateToCreate: () -> Unit
) {
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()

    var showDeleteGuard by remember { mutableStateOf<Long?>(null) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var mathAnswerText by remember { mutableStateOf("") }
    var mathError by remember { mutableStateOf(false) }

    // TTS engine for spoken feedback on profile selection (pre-reader accessibility)
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        var engine: TextToSpeech? = null
        engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                try {
                    engine?.language = Locale.getDefault()
                } catch (_: Exception) {}
            }
        }
        tts = engine
        onDispose {
            engine.stop()
            engine.shutdown()
        }
    }

    val isReducedMotion = ReducedMotionState.current
    val contentAlpha = remember { Animatable(if (isReducedMotion) 1f else 0f) }
    val contentOffsetY = remember { Animatable(if (isReducedMotion) 0f else 24f) }

    LaunchedEffect(Unit) {
        if (!isReducedMotion) {
            contentAlpha.animateTo(1f, animationSpec = tween(250))
        }
    }
    LaunchedEffect(Unit) {
        if (!isReducedMotion) {
            contentOffsetY.animateTo(0f, animationSpec = tween(250))
        }
    }

    fun triggerDelete(profileId: Long) {
        val num1 = (2..9).random()
        val num2 = (2..9).random()
        mathProblem = Pair(num1, num2)
        mathAnswerText = ""
        mathError = false
        showDeleteGuard = profileId
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftSky)
            .padding(PlayItSpacing.cardPadding)
            .alpha(contentAlpha.value)
            .offset(y = contentOffsetY.value.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(PlayItSpacing.section))
            Text(
                text = "WHO IS PLAYING?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                ),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(PlayItSpacing.tiny))
            Text(
                text = "Select your profile to start learning!",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(PlayItSpacing.section))

            if (profiles.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    EmptyState(
                        title = "No Profiles Found",
                        message = "Welcome to playIT! Create your first profile to start learning phonemes!",
                        mascotState = MascotState.Happy,
                        ctaText = "Create First Profile",
                        onCtaClick = onNavigateToCreate
                    )
                }
            } else {
                // Strict 16dp spacing (`PlayItSpacing.cardPadding`) enforced horizontally & vertically
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(PlayItSpacing.cardPadding), // 16dp
                    verticalArrangement = Arrangement.spacedBy(PlayItSpacing.cardPadding),   // 16dp
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(profiles) { profile ->
                        val avatar = AvatarPresets.find { it.id == profile.avatarResId } ?: AvatarPresets[0]
                        ProfileGridCard(
                            name = profile.name,
                            avatarColor = avatar.color,
                            avatarIcon = avatar.icon,
                            stars = profile.totalStars,
                            onSelect = {
                                tts?.speak(profile.name, TextToSpeech.QUEUE_FLUSH, null, null)
                                viewModel.selectProfile(profile.profileId)
                                onProfileSelected()
                            },
                            onDeleteClick = {
                                triggerDelete(profile.profileId)
                            }
                        )
                    }

                    if (profiles.size < 6) {
                        item {
                            AddProfileCard(onClick = onNavigateToCreate)
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation with arithmetic challenge (Parent Gating)
    if (showDeleteGuard != null) {
        ArithmeticGateDialog(
            title = "Parent Verification",
            consequenceMessage = "This action is destructive and will erase all progress data for this profile. Please solve the problem to proceed:",
            mathNum1 = mathProblem.first,
            mathNum2 = mathProblem.second,
            answerInput = mathAnswerText,
            onAnswerInputChange = {
                mathAnswerText = it
                mathError = false
            },
            isError = mathError,
            confirmText = "Verify & Delete",
            cancelText = "Cancel",
            onConfirm = {
                val expected = mathProblem.first + mathProblem.second
                val inputVal = mathAnswerText.toIntOrNull()
                if (inputVal == expected) {
                    showDeleteGuard?.let { id -> viewModel.deleteProfile(id) }
                    showDeleteGuard = null
                } else {
                    mathError = true
                }
            },
            onDismiss = { showDeleteGuard = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileGridCard(
    name: String,
    avatarColor: Color,
    avatarIcon: ImageVector,
    stars: Int,
    onSelect: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    LearningCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .tapFeedback(interactionSource = interactionSource, pressedScale = 0.92f)
            .semantics {
                contentDescription = "$name's profile, $stars stars earned. Double tap to select, long press to manage."
            }
            .clip(RoundedCornerShape(24.dp))
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onSelect,
                onLongClick = onDeleteClick
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Delete icon button at top right
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(TouchTarget.MINIMUM)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Manage or Delete $name's profile",
                    tint = TextSecondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // 72dp avatar circle (exceeds TouchTarget floor)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .background(avatarColor, CircleShape)
                        .border(3.dp, CreamWhite, CircleShape)
                ) {
                    Icon(
                        imageVector = avatarIcon,
                        contentDescription = null,
                        tint = CreamWhite,
                        modifier = Modifier.size(38.dp)
                    )
                }
                Spacer(modifier = Modifier.height(PlayItSpacing.small))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AchievementGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$stars Stars",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun AddProfileCard(
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .tapFeedback(interactionSource = interactionSource, pressedScale = 0.92f)
            .semantics {
                contentDescription = "Add new profile"
            },
        shape = RoundedCornerShape(24.dp),
        color = LearningBlue,
        contentColor = CreamWhite,
        shadowElevation = cardElevation
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp)
                    .background(CreamWhite.copy(alpha = 0.2f), CircleShape)
                    .border(2.dp, CreamWhite, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = CreamWhite,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(PlayItSpacing.small))
            Text(
                text = "Add Profile",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CreamWhite
            )
        }
    }
}
