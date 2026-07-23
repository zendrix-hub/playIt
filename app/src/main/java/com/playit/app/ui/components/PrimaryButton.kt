package com.playit.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
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
import com.playit.app.ui.theme.buttonElevation
import com.playit.app.ui.theme.buttonShape

/**
 * PrimaryButton - Single shared Primary Button for Continue/Start/Next actions app-wide.
 *
 * Enforces:
 * - Height: 56dp (TouchTarget.RECOMMENDED)
 * - Radius: 28dp (buttonShape)
 * - Color: Learning Blue (#4A90E2) enabled / Disabled (#CBD5E0) + 0.6f opacity disabled
 * - Text: CreamWhite Bold text
 * - Elevation: 2dp soft elevation (buttonElevation)
 * - Spring-bounce tap feedback: 100% -> 92% -> 100% (micro-interaction tier ~150-250ms)
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    PrimaryButton(
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
            color = if (enabled) CreamWhite else TextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    reducedMotion: Boolean = LocalReducedMotion.current,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Spring animation for tap feedback (100% -> 92% scale) - disabled when reducedMotion is true
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !reducedMotion) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "primary_button_bounce"
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
        color = if (enabled) LearningBlue else Disabled,
        contentColor = if (enabled) CreamWhite else TextSecondary,
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

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PrimaryButton(
            text = "Continue",
            onClick = {}
        )
        PrimaryButton(
            text = "Disabled State",
            onClick = {},
            enabled = false
        )
    }
}
