package com.playit.app.ui.map

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.components.LearningCard
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.StreakFlame
import com.playit.app.ui.theme.*
import kotlinx.coroutines.delay

// Data class representing a node on the map
data class MapNodeState(
    val id: Int,
    val label: String,
    val isUnlocked: Boolean,
    val starsEarned: Int,
    val isBlendIt: Boolean = false,
    val isActiveNode: Boolean = false // Breathing animation state for active node
)

/**
 * Task UI-5.04 — Polish MapScreen to Design System v1.0
 *
 * Implements:
 * - Full compliance for 35-node progression map
 * - Automatic default scroll/focus to current active node on entry
 * - Distinct Unlocked / Active / Completed / Locked states without color dependence
 * - Gentle shake & MascotBubble feedback on locked-node taps
 * - Persistent TopStatsBar header displaying stars, streak, profile, and parent gate
 * - Visually chunked group boundaries (7 groups of 4 letters + BlendIt challenge)
 * - Touch targets >= 48dp (72dp letter nodes, 140x56dp BlendIt nodes)
 * - Single-node animation performance optimization (hoisted to MapScreen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    profileName: String = "Player 1",
    totalStars: Int = 0,
    currentStreak: Int = 0,
    nodes: List<MapNodeState>,
    onNodeTapped: (MapNodeState) -> Unit,
    onParentClick: () -> Unit
) {
    var showParentGuard by remember { mutableStateOf(false) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var mathAnswerText by remember { mutableStateOf("") }
    var mathError by remember { mutableStateOf(false) }

    // Locked node tap feedback state
    var lockedTappedNode by remember { mutableStateOf<MapNodeState?>(null) }

    // List state for auto-scrolling to active node on launch
    val listState = rememberLazyListState()

    // Auto-scroll to active node on screen entry or node state updates
    LaunchedEffect(nodes) {
        if (nodes.isNotEmpty()) {
            val activeIndex = nodes.indexOfFirst { it.isActiveNode }
            if (activeIndex != -1) {
                listState.scrollToItem(activeIndex)
            } else {
                val lastUnlockedIndex = nodes.indexOfLast { it.isUnlocked }
                if (lastUnlockedIndex != -1) {
                    listState.scrollToItem(lastUnlockedIndex)
                }
            }
        }
    }

    // Auto-dismiss locked node feedback message after 3.5s
    LaunchedEffect(lockedTappedNode) {
        if (lockedTappedNode != null) {
            delay(3500)
            lockedTappedNode = null
        }
    }

    // Widen parent gate arithmetic factors: ensure at least one factor draws from 7..12 (ENG-2.18)
    fun triggerParentGate() {
        val num1 = (7..12).random()
        val num2 = (2..9).random()
        mathProblem = Pair(num1, num2)
        mathAnswerText = ""
        mathError = false
        showParentGuard = true
    }

    val bgBrush = Brush.verticalGradient(
        colors = listOf(SoftSky, CreamWhite)
    )

    Scaffold(
        topBar = {
            TopStatsBar(
                profileName = profileName,
                totalStars = totalStars,
                currentStreak = currentStreak,
                onParentClick = ::triggerParentGate
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Hoisted single-node breathing pulse animation (ENG-2.02 / UI-7.03)
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val activePulseScale by infiniteTransition.animateFloat(
                initialValue = 0.95f,
                targetValue = 1.06f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = LinearOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "activePulseScale"
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = PlayItSpacing.cardPadding),
                contentPadding = PaddingValues(top = PlayItSpacing.default, bottom = 80.dp),
                reverseLayout = true // Starts at bottom of screen like a true upward progression map
            ) {
                itemsIndexed(nodes) { index, node ->
                    // Alternate path alignment for winding trail feel
                    val alignment: Alignment = when (index % 3) {
                        0 -> Alignment.CenterStart
                        1 -> Alignment.Center
                        else -> Alignment.CenterEnd
                    }

                    val nodeScale = if (node.isActiveNode) activePulseScale else 1f

                    // Render Group Header Banner at the start of each 5-node group
                    if (index % 5 == 0) {
                        val groupNumber = (index / 5) + 1
                        GroupChunkBanner(groupNumber = groupNumber)
                        Spacer(modifier = Modifier.height(PlayItSpacing.small))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = PlayItSpacing.tiny),
                        contentAlignment = alignment
                    ) {
                        if (node.isBlendIt) {
                            BlendItChallengeNode(
                                node = node,
                                scale = nodeScale,
                                isShaking = lockedTappedNode?.id == node.id,
                                onClick = {
                                    if (node.isUnlocked) {
                                        onNodeTapped(node)
                                    } else {
                                        lockedTappedNode = node
                                    }
                                }
                            )
                        } else {
                            LetterNode(
                                node = node,
                                scale = nodeScale,
                                isShaking = lockedTappedNode?.id == node.id,
                                onClick = {
                                    if (node.isUnlocked) {
                                        onNodeTapped(node)
                                    } else {
                                        lockedTappedNode = node
                                    }
                                }
                            )
                        }
                    }

                    // Connector line between nodes
                    if (index < nodes.size - 1) {
                        PathConnector(alignment = alignment)
                    }
                }
            }

            // Locked node tap feedback banner (Mascot thinking line)
            if (lockedTappedNode != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(paddingValues)
                        .padding(PlayItSpacing.default)
                ) {
                    MascotBubble(
                        state = MascotState.Thinking,
                        message = "Complete previous letters to unlock '${lockedTappedNode?.label?.uppercase()}'!",
                        audioResId = 0,
                        autoPlayAudio = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Parent Verification Challenge Dialog
            if (showParentGuard) {
                AlertDialog(
                    onDismissRequest = { showParentGuard = false },
                    title = {
                        Text(
                            text = "Parent Verification Gate",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = "Please solve this arithmetic puzzle to access the parent dashboard:",
                                color = TextSecondary,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(PlayItSpacing.default))
                            Text(
                                text = "${mathProblem.first} x ${mathProblem.second} = ?",
                                fontWeight = FontWeight.Black,
                                fontSize = 28.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = LearningBlue
                            )
                            Spacer(modifier = Modifier.height(PlayItSpacing.default))
                            OutlinedTextField(
                                value = mathAnswerText,
                                onValueChange = { mathAnswerText = it },
                                label = { Text("Answer") },
                                isError = mathError,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (mathError) {
                                Text(
                                    text = "Incorrect result, please try again!",
                                    color = Color(0xFFE53935),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val ans = mathAnswerText.toIntOrNull()
                                if (ans == mathProblem.first * mathProblem.second) {
                                    showParentGuard = false
                                    onParentClick()
                                } else {
                                    mathError = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LearningBlue)
                        ) {
                            Text("Verify", color = CreamWhite, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showParentGuard = false }) {
                            Text("Cancel", color = TextSecondary)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TopStatsBar(
    profileName: String,
    totalStars: Int,
    currentStreak: Int,
    onParentClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        color = CreamWhite,
        shadowElevation = cardElevation,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PlayItSpacing.default, vertical = PlayItSpacing.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onParentClick,
                    modifier = Modifier.size(TouchTarget.MINIMUM)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Parent Dashboard",
                        tint = LearningBlue
                    )
                }
                Spacer(modifier = Modifier.width(PlayItSpacing.tiny))
                Text(
                    text = profileName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Stars",
                    tint = AchievementGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = totalStars.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(PlayItSpacing.default))
                StreakFlame(currentStreak = currentStreak)
            }
        }
    }
}

@Composable
fun GroupChunkBanner(groupNumber: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PlayItSpacing.small)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = LearningBlue.copy(alpha = 0.12f),
            border = androidx.compose.foundation.BorderStroke(1.dp, LearningBlue.copy(alpha = 0.3f)),
            modifier = Modifier.padding(horizontal = PlayItSpacing.default)
        ) {
            Text(
                text = "GROUP $groupNumber — MARUNGKO",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LearningBlue,
                modifier = Modifier.padding(horizontal = PlayItSpacing.default, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun LetterNode(
    node: MapNodeState,
    scale: Float = 1f,
    isShaking: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Gate unlock animation so it fires ONLY on actual state transition (locked -> unlocked), never on revisit
    var previousUnlocked by remember { mutableStateOf(node.isUnlocked) }
    var isUnlocking by remember { mutableStateOf(false) }

    LaunchedEffect(node.isUnlocked) {
        if (!previousUnlocked && node.isUnlocked) {
            isUnlocking = true
            delay(800)
            isUnlocking = false
        }
        previousUnlocked = node.isUnlocked
    }

    val unlockGlowScale by animateFloatAsState(
        targetValue = if (isUnlocking) 1.5f else 1.0f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "letter_unlock_glow_scale"
    )
    val unlockGlowAlpha by animateFloatAsState(
        targetValue = if (isUnlocking) 0f else 0.8f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "letter_unlock_glow_alpha"
    )

    // Interactive spring scale feedback
    val tapScale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "letter_node_tap_scale"
    )

    // Gentle shake animation offset when tapping locked node
    val shakeOffset by animateFloatAsState(
        targetValue = if (isShaking) 8f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "shake_anim"
    )

    val isCompleted = node.isUnlocked && node.starsEarned > 0

    val backgroundColor = when {
        isCompleted -> GrowthGreen
        node.isUnlocked -> LearningBlue
        else -> Disabled
    }

    val contentColor = when {
        node.isUnlocked -> CreamWhite
        else -> TextSecondary
    }

    val lockStatusText = when {
        isCompleted -> "completed, ${node.starsEarned} stars"
        node.isUnlocked -> "unlocked, current lesson"
        else -> "locked"
    }
    val nodeDescription = "${node.label} - $lockStatusText"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(x = shakeOffset.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = nodeDescription
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            // One-shot magical-chime unlock glow burst overlay
            if (isUnlocking) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(unlockGlowScale)
                        .alpha(unlockGlowAlpha)
                        .clip(CircleShape)
                        .background(AchievementGold)
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp) // Exceeds TouchTarget.IMPORTANT (64dp)
                    .scale(tapScale)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .border(
                        width = if (node.isActiveNode) 4.dp else 2.dp,
                        color = if (node.isActiveNode) AchievementGold else Border,
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
            ) {
                if (node.isUnlocked) {
                    Text(
                        text = node.label.uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = contentColor
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Completion checkmark badge overlay
                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(AchievementGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = TextPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // 1 to 3 Star rating indicator under unlocked letter node
        if (node.isUnlocked) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { starIdx ->
                    val isEarned = starIdx < node.starsEarned
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isEarned) AchievementGold else Border,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BlendItChallengeNode(
    node: MapNodeState,
    scale: Float = 1f,
    isShaking: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Gate unlock animation so it fires ONLY on actual state transition (locked -> unlocked), never on revisit
    var previousUnlocked by remember { mutableStateOf(node.isUnlocked) }
    var isUnlocking by remember { mutableStateOf(false) }

    LaunchedEffect(node.isUnlocked) {
        if (!previousUnlocked && node.isUnlocked) {
            isUnlocking = true
            delay(800)
            isUnlocking = false
        }
        previousUnlocked = node.isUnlocked
    }

    val unlockGlowScale by animateFloatAsState(
        targetValue = if (isUnlocking) 1.4f else 1.0f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "blend_unlock_glow_scale"
    )
    val unlockGlowAlpha by animateFloatAsState(
        targetValue = if (isUnlocking) 0f else 0.8f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "blend_unlock_glow_alpha"
    )

    val tapScale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "blend_node_tap_scale"
    )

    val shakeOffset by animateFloatAsState(
        targetValue = if (isShaking) 8f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "blend_shake_anim"
    )

    val isCompleted = node.isUnlocked && node.starsEarned > 0

    val backgroundColor = when {
        isCompleted -> GrowthGreen
        node.isUnlocked -> FriendlyPurple
        else -> Disabled
    }

    val nodeDescription = "${node.label} - ${if (isCompleted) "completed" else if (node.isUnlocked) "unlocked" else "locked"}"

    Box(contentAlignment = Alignment.Center) {
        if (isUnlocking) {
            Box(
                modifier = Modifier
                    .height(TouchTarget.RECOMMENDED)
                    .width(140.dp)
                    .scale(unlockGlowScale)
                    .alpha(unlockGlowAlpha)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AchievementGold)
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            border = androidx.compose.foundation.BorderStroke(
                width = if (node.isActiveNode) 4.dp else 2.dp,
                color = if (node.isActiveNode) AchievementGold else Border
            ),
            shadowElevation = cardElevation,
            modifier = Modifier
                .offset(x = shakeOffset.dp)
                .height(TouchTarget.RECOMMENDED) // 56dp
                .width(140.dp)
                .scale(tapScale)
                .semantics(mergeDescendants = true) {
                    contentDescription = nodeDescription
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (node.isUnlocked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BLEND IT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CreamWhite
                        )
                        if (isCompleted) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = AchievementGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LOCKED",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PathConnector(alignment: Alignment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .width(6.dp)
                .fillMaxHeight()
                .alpha(0.3f)
                .background(LearningBlue, RoundedCornerShape(3.dp))
        )
    }
}