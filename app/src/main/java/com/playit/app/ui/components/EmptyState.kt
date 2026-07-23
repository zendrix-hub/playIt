package com.playit.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary

/**
 * Task UI-9.03 — Build empty-state component
 *
 * Shared, on-brand EmptyState composable used for zero-profiles on first launch
 * and any dashboard or list context without data. Features mascot guidance,
 * friendly copy, and a primary CTA slot with a smooth fade-tier entrance.
 */
@Composable
fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    mascotState: MascotState = MascotState.Encouraging,
    ctaText: String? = null,
    onCtaClick: (() -> Unit)? = null,
    ctaContent: (@Composable () -> Unit)? = null
) {
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(300))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(PlayItSpacing.cardPadding)
            .alpha(alphaAnim.value)
            .semantics(mergeDescendants = true) {
                contentDescription = "$title. $message"
            }
    ) {
        MascotBubble(
            state = mascotState,
            message = message,
            audioResId = 0,
            autoPlayAudio = false,
            modifier = Modifier.padding(bottom = PlayItSpacing.default)
        )

        Spacer(modifier = Modifier.height(PlayItSpacing.small))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(PlayItSpacing.tiny))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp
            ),
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = PlayItSpacing.default)
        )

        if (ctaText != null && onCtaClick != null) {
            Spacer(modifier = Modifier.height(PlayItSpacing.section))
            PrimaryButton(
                text = ctaText,
                onClick = onCtaClick,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            )
        } else if (ctaContent != null) {
            Spacer(modifier = Modifier.height(PlayItSpacing.section))
            ctaContent()
        }
    }
}
