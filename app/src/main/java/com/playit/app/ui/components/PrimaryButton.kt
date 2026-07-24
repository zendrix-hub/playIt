package com.playit.app.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.playit.app.ui.util.tapFeedback

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
    PlayItButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = PlayItButtonVariant.PRIMARY,
        enabled = enabled,
        leadingIcon = leadingIcon
    )
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    reducedMotion: Boolean = LocalReducedMotion.current,
    content: @Composable RowScope.() -> Unit
) {
    PlayItButton(
        onClick = onClick,
        modifier = modifier,
        variant = PlayItButtonVariant.PRIMARY,
        enabled = enabled,
        reducedMotion = reducedMotion,
        content = content
    )
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
