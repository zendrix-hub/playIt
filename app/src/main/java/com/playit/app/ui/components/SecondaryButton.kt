package com.playit.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.Disabled
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.LocalReducedMotion
import com.playit.app.ui.theme.TextSecondary
import com.playit.app.ui.theme.TouchTarget
import com.playit.app.ui.theme.buttonShape
import com.playit.app.ui.util.tapFeedback

/**
 * SecondaryButton - Shared Secondary Button (Cream White bg, 2dp Learning Blue border).
 * Primarily used for secondary actions like "Replay Audio".
 *
 * Enforces:
 * - Minimum touch target floor: 48dp (TouchTarget.MINIMUM), recommended 56dp (TouchTarget.RECOMMENDED)
 * - Radius: 28dp (buttonShape)
 * - Background: Cream White (#FFFDF8)
 * - Border: 2dp Learning Blue (#4A90E2) enabled / Disabled (#CBD5E0) disabled
 * - Text/Icon: Learning Blue (#4A90E2) enabled / TextSecondary disabled
 * - Spring-bounce tap feedback: 100% -> 92% -> 100% matching PrimaryButton
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    SecondaryButton(
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
            color = if (enabled) LearningBlue else TextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Icon-only variant for SecondaryButton (e.g., Replay Audio button).
 * Ensures the touch target floor is always at least 48dp (TouchTarget.MINIMUM) or 56dp by default.
 */
@Composable
fun SecondaryIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    SecondaryButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = TouchTarget.RECOMMENDED),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) LearningBlue else TextSecondary
        )
    }
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    reducedMotion: Boolean = LocalReducedMotion.current,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .defaultMinSize(minWidth = TouchTarget.MINIMUM)
            .height(TouchTarget.RECOMMENDED)
            .tapFeedback(
                interactionSource = interactionSource,
                enabled = enabled,
                pressedScale = 0.92f,
                reducedMotion = reducedMotion
            )
            .alpha(if (enabled) 1.0f else 0.6f),
        shape = buttonShape,
        color = CreamWhite,
        border = BorderStroke(
            width = 2.dp,
            color = if (enabled) LearningBlue else Disabled
        ),
        contentColor = if (enabled) LearningBlue else TextSecondary,
        shadowElevation = 0.dp,
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

@Preview(showBackground = true)
@Composable
fun SecondaryButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SecondaryButton(
            text = "Replay Audio",
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    tint = LearningBlue
                )
            }
        )
        SecondaryIconButton(
            icon = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = "Replay sound",
            onClick = {}
        )
        SecondaryButton(
            text = "Disabled State",
            onClick = {},
            enabled = false
        )
    }
}
