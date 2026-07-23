package com.playit.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.Disabled
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.LocalReducedMotion
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary
import com.playit.app.ui.theme.TouchTarget
import com.playit.app.ui.theme.buttonElevation
import com.playit.app.ui.theme.buttonShape

/**
 * SuccessButton - Shared Success Button (Achievement Gold bg, dark gray text).
 * Reserved exclusively for "Claim Reward" and celebration actions.
 *
 * Enforces:
 * - Height: 56dp (TouchTarget.RECOMMENDED)
 * - Radius: 28dp (buttonShape)
 * - Color: Achievement Gold (#FFC107) enabled / Disabled (#CBD5E0) + 0.6f opacity disabled
 * - Text: TextPrimary (#2D3748) Bold text
 * - Elevation: 2dp soft elevation (buttonElevation)
 * - Spring-bounce tap feedback: 100% -> 90% -> 100% (micro-to-standard tier ~150-250ms)
 */
@Composable
fun SuccessButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    SuccessButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = if (enabled) TextPrimary else TextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SuccessButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    reducedMotion: Boolean = LocalReducedMotion.current,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Spring animation for tap feedback (100% -> 90% scale for celebration feedback) - disabled when reducedMotion is true
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !reducedMotion) 0.90f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "success_button_bounce"
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(TouchTarget.RECOMMENDED)
            .scale(scale)
            .alpha(if (enabled) 1.0f else 0.6f),
        shape = buttonShape,
        color = if (enabled) AchievementGold else Disabled,
        contentColor = if (enabled) TextPrimary else TextSecondary,
        shadowElevation = if (enabled) buttonElevation else 0.dp,
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Preview(name = "Cream White Background", showBackground = true)
@Composable
fun SuccessButtonCreamWhitePreview() {
    Box(
        modifier = Modifier
            .background(CreamWhite)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SuccessButton(
                text = "Claim Reward",
                onClick = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = TextPrimary
                    )
                }
            )
            SuccessButton(
                text = "Claim Reward (Disabled)",
                onClick = {},
                enabled = false
            )
        }
    }
}

@Preview(name = "Gold Gradient Background", showBackground = true)
@Composable
fun SuccessButtonGoldGradientPreview() {
    val goldGradient = Brush.verticalGradient(
        colors = listOf(
            AchievementGold,
            EnergyOrange
        )
    )
    Box(
        modifier = Modifier
            .background(goldGradient)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SuccessButton(
                text = "Claim Star",
                onClick = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = TextPrimary
                    )
                }
            )
        }
    }
}
