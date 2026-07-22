package com.playit.app.ui.map

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.TangerineOrange
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

// Data class representing a node on the map
data class MapNodeState(
    val id: Int,
    val label: String,
    val isUnlocked: Boolean,
    val starsEarned: Int,
    val isBlendIt: Boolean = false,
    val isActiveNode: Boolean = false // Added for breathing animations
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    profileName: String = "Player 1",
    totalStars: Int = 0,
    currentStreak: Int = 0,
    nodes: List<MapNodeState>,
    onNodeTapped: (MapNodeState) -> Unit,
    onParentClick: () -> Unit // Settings route triggers parent dashboard
) {
    var showParentGuard by remember { mutableStateOf(false) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var mathAnswerText by remember { mutableStateOf("") }
    var mathError by remember { mutableStateOf(false) }

    fun triggerParentGate() {
        val num1 = (2..9).random()
        val num2 = (2..9).random()
        mathProblem = Pair(num1, num2)
        mathAnswerText = ""
        mathError = false
        showParentGuard = true
    }

    Scaffold(
        topBar = {
            TopStatsBar(
                profileName = profileName,
                totalStars = totalStars,
                currentStreak = currentStreak,
                onParentClick = ::triggerParentGate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val activePulseScale by infiniteTransition.animateFloat(
                initialValue = 0.95f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = LinearOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "activePulse"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(vertical = 32.dp),
                reverseLayout = true // Starts at the bottom of the screen like a true progression map
            ) {
                itemsIndexed(nodes) { index, node ->
                    val alignment: Alignment = when (index % 3) {
                        0 -> Alignment.CenterStart
                        1 -> Alignment.Center
                        else -> Alignment.CenterEnd
                    }

                    val nodeScale = if (node.isActiveNode) activePulseScale else 1f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = alignment
                    ) {
                        if (node.isBlendIt) {
                            BlendItChallengeNode(node = node, scale = nodeScale, onClick = { onNodeTapped(node) })
                        } else {
                            LetterNode(node = node, scale = nodeScale, onClick = { onNodeTapped(node) })
                        }
                    }

                    // Draws a subtle connector line between nodes (except for the last one)
                    if (index < nodes.size - 1) {
                        PathConnector(alignment = alignment)
                    }
                }
            }

            // ── Parent Validation Challenge Gate Dialog ──────────────────────
            if (showParentGuard) {
                AlertDialog(
                    onDismissRequest = { showParentGuard = false },
                    title = { Text("Parent Verification Gate", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Please solve this simple arithmetic puzzle to access parent portal:")
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "${mathProblem.first} x ${mathProblem.second} = ?",
                                fontWeight = FontWeight.Black,
                                fontSize = 28.sp,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = mathAnswerText,
                                onValueChange = { mathAnswerText = it },
                                label = { Text("Answer") },
                                isError = mathError,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (mathError) {
                                Text(
                                    text = "Incorrect result, please try again!",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp,
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
                            }
                        ) { Text("Verify") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showParentGuard = false }) { Text("Cancel") }
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
    Card(
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onParentClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Parent Dashboard",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = profileName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Stars", tint = TangerineOrange)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = totalStars.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "🔥 $currentStreak",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun LetterNode(
    node: MapNodeState,
    scale: Float = 1f,
    onClick: () -> Unit
) {
    val backgroundColor = if (node.isUnlocked) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (node.isUnlocked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    val lockStatusText = if (node.isUnlocked) {
        "unlocked, ${node.starsEarned} stars"
    } else {
        "locked"
    }
    val nodeDescription = "${node.label} - $lockStatusText"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics(mergeDescendants = true) {
            contentDescription = nodeDescription
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .scale(scale)
                .background(backgroundColor, CircleShape)
                .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                .clickable(enabled = node.isUnlocked) { onClick() }
        ) {
            if (node.isUnlocked) {
                Text(
                    text = node.label.uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = contentColor
                )
            } else {
                Icon(Icons.Rounded.Lock, contentDescription = null, tint = contentColor)
            }
        }

        // Star indicator under the node
        if (node.isUnlocked) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < node.starsEarned) TangerineOrange else Color.LightGray,
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
    onClick: () -> Unit
) {
    val backgroundColor = if (node.isUnlocked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant

    val nodeDescription = "${node.label} - ${if (node.isUnlocked) "unlocked" else "locked"}"

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(64.dp)
            .width(120.dp)
            .scale(scale)
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(4.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .semantics(mergeDescendants = true) {
                contentDescription = nodeDescription
            }
            .clickable(enabled = node.isUnlocked) { onClick() }
    ) {
        if (node.isUnlocked) {
            Text(
                text = "BLEND IT",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Icon(Icons.Rounded.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun PathConnector(alignment: Alignment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .width(8.dp)
                .fillMaxHeight()
                .alpha(0.2f)
                .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(4.dp))
        )
    }
}