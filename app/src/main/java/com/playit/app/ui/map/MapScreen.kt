package com.playit.app.ui.map

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.a11y.ReducedMotionState
import com.playit.app.ui.components.MapNodeBase
import com.playit.app.ui.components.MapNodeStatus
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.SegmentedProgressBar
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
 * MapScreen — Task D1, D2, D3 & Refactoring Phase
 *
 * High-priority enhancements:
 * 1. MapNodeBase integration: Enforces 4-state semantic color system (Locked, Unlocked, Current, Completed).
 * 2. Reduced Motion Safety: ParallaxClouds safely gated behind ReducedMotionState (static background when enabled).
 * 3. Top-anchored Journey Progress Signal: Slim "X/28 letters" SegmentedProgressBar directly beneath TopStatsBar.
 * 4. Architectural Boundary: Maintains exact LaunchedEffect auto-scroll behavior to current active node.
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
    val context = androidx.compose.ui.platform.LocalContext.current
    val soundManager = remember(context) { com.playit.app.data.audio.SoundManager.getInstance(context) }

    var showParentGuard by remember { mutableStateOf(false) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var mathAnswerText by remember { mutableStateOf("") }
    var mathError by remember { mutableStateOf(false) }

    // Locked node tap feedback state
    var lockedTappedNode by remember { mutableStateOf<MapNodeState?>(null) }

    // List state for auto-scrolling to active node on launch
    val listState = rememberLazyListState()

    // CRITICAL ARCHITECTURAL BOUNDARY: Auto-scroll to active node on screen entry or node updates
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

    // Widen parent gate arithmetic factors: ensure at least one factor draws from 7..12
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

    val isReducedMotion = ReducedMotionState.current

    // Journey-level letter progress calculations
    val completedLetters = remember(nodes) {
        nodes.count { !it.isBlendIt && it.isUnlocked && it.starsEarned > 0 }
    }
    val totalLetters = remember(nodes) {
        val count = nodes.count { !it.isBlendIt }
        if (count > 0) count else 28
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CreamWhite)
            ) {
                TopStatsBar(
                    profileName = profileName,
                    totalStars = totalStars,
                    currentStreak = currentStreak,
                    onParentClick = ::triggerParentGate
                )

                // Task D3 — Slim, top-anchored "X/28 letters" Journey Progress Signal
                SegmentedProgressBar(
                    currentProgress = completedLetters,
                    totalSegments = totalLetters,
                    label = "$completedLetters/$totalLetters letters",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PlayItSpacing.default, vertical = PlayItSpacing.small)
                )
            }
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Task D2 — Parallax Clouds safely gated behind ReducedMotionState
            ParallaxClouds(
                isReducedMotion = isReducedMotion,
                modifier = Modifier.padding(paddingValues)
            )

            // Hoisted single-node breathing pulse animation
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val activePulseScale by infiniteTransition.animateFloat(
                initialValue = 0.96f,
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
                contentPadding = PaddingValues(top = PlayItSpacing.default, bottom = 90.dp),
                reverseLayout = true // Starts at bottom of screen like a true upward progression map
            ) {
                itemsIndexed(nodes) { index, node ->
                    // Alternate path alignment for winding trail feel
                    val alignment: Alignment = when (index % 3) {
                        0 -> Alignment.CenterStart
                        1 -> Alignment.Center
                        else -> Alignment.CenterEnd
                    }

                    val nodeScale = if (node.isActiveNode && !isReducedMotion) activePulseScale else 1f

                    // Determine 4-state node status for MapNodeBase
                    val nodeStatus = when {
                        !node.isUnlocked -> MapNodeStatus.LOCKED
                        node.isActiveNode -> MapNodeStatus.CURRENT
                        node.starsEarned > 0 -> MapNodeStatus.COMPLETED
                        else -> MapNodeStatus.UNLOCKED
                    }

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
                        // Task D1 — MapNodeBase 4-State Color System Integration
                        MapNodeBase(
                            label = node.label,
                            status = nodeStatus,
                            starsEarned = node.starsEarned,
                            isBlendIt = node.isBlendIt,
                            scale = nodeScale,
                            isShaking = lockedTappedNode?.id == node.id,
                            onClick = {
                                if (node.isUnlocked) {
                                    soundManager.playNodeUnlock()
                                    onNodeTapped(node)
                                } else {
                                    lockedTappedNode = node
                                }
                            }
                        )
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
                                    color = com.playit.app.ui.theme.GentleCorrectionOrange,
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

/**
 * Background Parallax Clouds — Task D2
 * Gated by reducedMotion boolean: floats when false, remains static when true.
 */
@Composable
private fun ParallaxClouds(
    isReducedMotion: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cloud_parallax")
    val cloudOffset1 by if (isReducedMotion) {
        remember { mutableStateOf(20f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = -120f,
            targetValue = 380f,
            animationSpec = infiniteRepeatable(
                animation = tween(28000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "cloud_1_offset"
        )
    }

    val cloudOffset2 by if (isReducedMotion) {
        remember { mutableStateOf(160f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 320f,
            targetValue = -180f,
            animationSpec = infiniteRepeatable(
                animation = tween(34000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "cloud_2_offset"
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CreamWhite.copy(alpha = 0.5f),
            modifier = Modifier
                .offset(x = cloudOffset1.dp, y = 90.dp)
                .size(width = 130.dp, height = 44.dp)
        ) {}

        Surface(
            shape = RoundedCornerShape(28.dp),
            color = CreamWhite.copy(alpha = 0.4f),
            modifier = Modifier
                .offset(x = cloudOffset2.dp, y = 260.dp)
                .size(width = 170.dp, height = 52.dp)
        ) {}
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