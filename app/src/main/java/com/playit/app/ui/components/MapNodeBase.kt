package com.playit.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.*
import com.playit.app.ui.util.tapFeedback
import kotlinx.coroutines.delay

/**
 * Task C5 / D1 — MapNodeBase
 *
 * Base composable component enforcing a strict 4-state semantic color system for winding path nodes:
 * 1. Locked: disabled gray (`Disabled` / `#CBD5E0`) with padlock icon.
 * 2. Unlocked: `LearningBlue` (`#4A90E2`), static fill.
 * 3. Current (active): `AchievementGold` (`#FFFFC107`), pulsing scale (1.0f -> 1.06f), largest node.
 * 4. Completed: `GrowthGreen` (`#4CAF50`), solid fill with star badge overlay.
 */
enum class MapNodeStatus {
    LOCKED,
    UNLOCKED,
    CURRENT,
    COMPLETED
}

@Composable
fun MapNodeBase(
    label: String,
    status: MapNodeStatus,
    starsEarned: Int = 0,
    isBlendIt: Boolean = false,
    scale: Float = 1f,
    isShaking: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Gentle shake animation offset when tapping a locked node
    val shakeOffset by animateFloatAsState(
        targetValue = if (isShaking) 8f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "map_node_shake_anim"
    )

    // Semantic 4-State Color System Assignment
    val backgroundColor = when (status) {
        MapNodeStatus.LOCKED -> Disabled
        MapNodeStatus.UNLOCKED -> LearningBlue
        MapNodeStatus.CURRENT -> AchievementGold
        MapNodeStatus.COMPLETED -> GrowthGreen
    }

    val contentColor = when (status) {
        MapNodeStatus.LOCKED -> TextSecondary
        MapNodeStatus.CURRENT -> TextPrimary
        else -> CreamWhite
    }

    val borderColor = when (status) {
        MapNodeStatus.CURRENT -> AchievementGold
        MapNodeStatus.COMPLETED -> GrowthGreen
        MapNodeStatus.UNLOCKED -> LearningBlue
        MapNodeStatus.LOCKED -> Border
    }

    val density = LocalDensity.current.density
    val lockStatusText = when (status) {
        MapNodeStatus.COMPLETED -> "completed, $starsEarned stars"
        MapNodeStatus.CURRENT -> "current active lesson"
        MapNodeStatus.UNLOCKED -> "unlocked lesson"
        MapNodeStatus.LOCKED -> "locked lesson"
    }
    val nodeDescription = "$label - $lockStatusText"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .graphicsLayer {
                translationX = shakeOffset * density
            }
            .semantics(mergeDescendants = true) {
                contentDescription = nodeDescription
            }
    ) {
        if (isBlendIt) {
            // Rectangular Challenge Node
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = backgroundColor,
                border = androidx.compose.foundation.BorderStroke(
                    width = if (status == MapNodeStatus.CURRENT) 4.dp else 2.dp,
                    color = borderColor
                ),
                shadowElevation = if (status == MapNodeStatus.CURRENT) cardElevation else 2.dp,
                modifier = Modifier
                    .height(TouchTarget.RECOMMENDED) // 56dp
                    .width(140.dp)
                    .tapFeedback(isPressed = isPressed, pressedScale = 0.90f, restingScale = scale)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when (status) {
                        MapNodeStatus.LOCKED -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Lock,
                                    contentDescription = null,
                                    tint = contentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "LOCKED",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                            }
                        }
                        else -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "BLEND IT",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor
                                )
                                if (status == MapNodeStatus.COMPLETED) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = AchievementGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Circular Letter Node (72dp min diameter)
            Box(contentAlignment = Alignment.Center) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .tapFeedback(isPressed = isPressed, pressedScale = 0.90f, restingScale = scale)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .border(
                            width = if (status == MapNodeStatus.CURRENT) 4.dp else 2.dp,
                            color = borderColor,
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                ) {
                    if (status == MapNodeStatus.LOCKED) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Text(
                            text = label.uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = contentColor
                        )
                    }

                    // Completed Star Badge Overlay
                    if (status == MapNodeStatus.COMPLETED) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(AchievementGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Completed Star Badge",
                                tint = TextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Star Rating Indicator beneath unlocked/completed letter nodes
            if (status != MapNodeStatus.LOCKED) {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { starIdx ->
                        val isEarned = starIdx < starsEarned
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
}
