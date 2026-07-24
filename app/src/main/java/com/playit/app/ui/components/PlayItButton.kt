package com.playit.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.Border
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.Disabled
import com.playit.app.ui.theme.GrowthGreen
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.LocalReducedMotion
import com.playit.app.ui.theme.PlayItTheme
import com.playit.app.ui.theme.SoftSky
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary
import com.playit.app.ui.theme.TouchTarget
import com.playit.app.ui.theme.buttonElevation
import com.playit.app.ui.theme.buttonShape
import com.playit.app.ui.util.tapFeedback

/**
 * PlayItButtonVariant defines the functional styling tiers for PlayItButton.
 */
enum class PlayItButtonVariant {
    PRIMARY,
    SECONDARY,
    SUCCESS
}

/**
 * Unified PlayItButton component serving as the master CTA primitive across all 12 screens.
 *
 * Enforces:
 * - Height: 56dp (TouchTarget.RECOMMENDED) by default (54dp minimum floor)
 * - Corner radius: 28dp (buttonShape)
 * - Tactile spring-bounce tap feedback: 100% -> 92% -> 100%
 * - Accessible high-contrast colors across Primary, Secondary, and Success variants
 */
@Composable
fun PlayItButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PlayItButtonVariant = PlayItButtonVariant.PRIMARY,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    PlayItButton(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        enabled = enabled
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        val textColor = when {
            !enabled -> TextSecondary
            variant == PlayItButtonVariant.SECONDARY -> LearningBlue
            else -> CreamWhite
        }

        Text(
            text = text,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PlayItButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PlayItButtonVariant = PlayItButtonVariant.PRIMARY,
    enabled: Boolean = true,
    reducedMotion: Boolean = LocalReducedMotion.current,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val containerColor = when {
        !enabled -> Disabled
        variant == PlayItButtonVariant.PRIMARY -> LearningBlue
        variant == PlayItButtonVariant.SECONDARY -> SoftSky
        else -> GrowthGreen
    }

    val contentColor = when {
        !enabled -> TextSecondary
        variant == PlayItButtonVariant.SECONDARY -> LearningBlue
        else -> CreamWhite
    }

    val borderStroke = if (variant == PlayItButtonVariant.SECONDARY && enabled) {
        BorderStroke(2.dp, Border)
    } else {
        null
    }

    val shadowElevation = if (enabled && variant != PlayItButtonVariant.SECONDARY) {
        buttonElevation
    } else {
        0.dp
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(TouchTarget.RECOMMENDED)
            .tapFeedback(
                interactionSource = interactionSource,
                enabled = enabled,
                pressedScale = 0.92f,
                reducedMotion = reducedMotion
            )
            .alpha(if (enabled) 1.0f else 0.6f),
        shape = buttonShape,
        color = containerColor,
        contentColor = contentColor,
        border = borderStroke,
        shadowElevation = shadowElevation,
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

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(name = "PlayItButton Variants", showBackground = true)
@Composable
fun PlayItButtonVariantsPreview() {
    PlayItTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlayItButton(text = "Primary Action", onClick = {}, variant = PlayItButtonVariant.PRIMARY)
            PlayItButton(text = "Secondary Action", onClick = {}, variant = PlayItButtonVariant.SECONDARY)
            PlayItButton(text = "Success Action", onClick = {}, variant = PlayItButtonVariant.SUCCESS)
            PlayItButton(text = "Disabled State", onClick = {}, enabled = false)
        }
    }
}
