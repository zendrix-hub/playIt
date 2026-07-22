package com.playit.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val HeartDisabled = Color(0xFFCBD5E0)
private val ContainerBg = Color(0xFFFFFFFF)

/**
 * Shared HeartDisplay component used across learning screens (Say It, Find It, Blend It).
 * Renders active/remaining hearts using both color and shape distinctions (filled vs. outlined).
 */
@Composable
fun HeartDisplay(
    activeHearts: Int,
    maxHearts: Int = 3,
    modifier: Modifier = Modifier
) {
    val clampedActive = activeHearts.coerceIn(0, maxHearts)

    Surface(
        modifier = modifier.semantics {
            contentDescription = "$clampedActive of $maxHearts hearts remaining"
        },
        shape = RoundedCornerShape(50),
        color = ContainerBg.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 1..maxHearts) {
                if (i <= clampedActive) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = HeartRed,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = HeartDisabled,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeartDisplayFullPreview() {
    HeartDisplay(activeHearts = 3)
}

@Preview(showBackground = true)
@Composable
fun HeartDisplayPartialPreview() {
    HeartDisplay(activeHearts = 2)
}

@Preview(showBackground = true)
@Composable
fun HeartDisplayEmptyPreview() {
    HeartDisplay(activeHearts = 0)
}
