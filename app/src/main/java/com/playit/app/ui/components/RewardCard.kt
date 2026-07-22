package com.playit.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.cardElevation
import com.playit.app.ui.theme.rewardCardShape

/**
 * RewardCard - Shared reward container for completion/star screens.
 *
 * Enforces:
 * - Radius: 32dp (rewardCardShape)
 * - Elevation: 4dp (cardElevation)
 * - Padding: 24dp (PlayItSpacing.cardPadding)
 * - Background: Achievement Gold Gradient (AchievementGold to EnergyOrange)
 */
@Composable
fun RewardCard(
    modifier: Modifier = Modifier,
    gradient: Brush = Brush.verticalGradient(
        colors = listOf(
            AchievementGold,
            EnergyOrange
        )
    ),
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = rewardCardShape,
        color = Color.Transparent,
        shadowElevation = cardElevation
    ) {
        Box(
            modifier = Modifier
                .background(brush = gradient, shape = rewardCardShape)
                .padding(PlayItSpacing.cardPadding),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RewardCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        RewardCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Magaling!",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Natapos mo ang leksyon!",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
