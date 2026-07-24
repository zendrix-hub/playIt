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
    PlayItButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = PlayItButtonVariant.SECONDARY,
        enabled = enabled,
        leadingIcon = leadingIcon
    )
}

/**
 * Icon-only variant for SecondaryButton (e.g., Replay Audio button).
 * Ensures the touch target floor is always at least 54dp (TouchTarget.MINIMUM) or 56dp by default.
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
    PlayItButton(
        onClick = onClick,
        modifier = modifier,
        variant = PlayItButtonVariant.SECONDARY,
        enabled = enabled,
        reducedMotion = reducedMotion,
        content = content
    )
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
