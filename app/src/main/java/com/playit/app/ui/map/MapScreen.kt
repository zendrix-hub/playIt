package com.playit.app.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.TangerineOrange

// Data class representing a node on the map
data class MapNodeState(
    val id: Int,
    val label: String,
    val isUnlocked: Boolean,
    val starsEarned: Int,
    val isBlendIt: Boolean = false
)

@Composable
fun MapScreen(
    profileName: String = "Player 1",
    totalStars: Int = 0,
    currentStreak: Int = 0,
    nodes: List<MapNodeState>,
    onNodeTapped: (MapNodeState) -> Unit // Now securely passes the whole node
) {
    Scaffold(
        topBar = {
            TopStatsBar(
                profileName = profileName,
                totalStars = totalStars,
                currentStreak = currentStreak
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(vertical = 32.dp),
            reverseLayout = true // Starts at the bottom of the screen like a true progression map
        ) {
            itemsIndexed(nodes) { index, node ->
                // Uses explicit 2D alignments to satisfy the Box scope
                val alignment: Alignment = when (index % 3) {
                    0 -> Alignment.CenterStart
                    1 -> Alignment.Center
                    else -> Alignment.CenterEnd
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = alignment
                ) {
                    if (node.isBlendIt) {
                        BlendItChallengeNode(node = node, onClick = { onNodeTapped(node) })
                    } else {
                        LetterNode(node = node, onClick = { onNodeTapped(node) })
                    }
                }

                // Draws a subtle connector line between nodes (except for the last one)
                if (index < nodes.size - 1) {
                    PathConnector(alignment = alignment)
                }
            }
        }
    }
}

@Composable
fun TopStatsBar(profileName: String, totalStars: Int, currentStreak: Int) {
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
            Text(
                text = profileName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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
fun LetterNode(node: MapNodeState, onClick: () -> Unit) {
    val backgroundColor = if (node.isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (node.isUnlocked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
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
                Icon(Icons.Rounded.Lock, contentDescription = "Locked", tint = contentColor)
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
fun BlendItChallengeNode(node: MapNodeState, onClick: () -> Unit) {
    val backgroundColor = if (node.isUnlocked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(64.dp)
            .width(120.dp)
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(4.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
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
            Icon(Icons.Rounded.Lock, contentDescription = "Locked", tint = MaterialTheme.colorScheme.onSurfaceVariant)
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