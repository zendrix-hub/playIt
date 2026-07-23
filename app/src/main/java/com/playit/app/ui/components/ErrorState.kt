package com.playit.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.GentleCorrectionOrange
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary
import com.playit.app.ui.theme.TouchTarget

/**
 * Task UI-9.03 — Build error-state component
 *
 * Shared, on-brand ErrorState composable for graceful asset-loading failure or network/error handling.
 * Uses a friendly, non-alarming icon and supportive message framed as the app's problem,
 * paired with a retry or alternative action CTA — never a dead end.
 */
@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    title: String = "Oops! Something went wrong",
    icon: ImageVector = Icons.Default.Info,
    retryText: String = "Try Again",
    onRetryClick: (() -> Unit)? = null
) {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.92f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(250))
    }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1f, animationSpec = tween(250))
    }

    LearningCard(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alphaAnim.value)
            .scale(scaleAnim.value)
            .semantics(mergeDescendants = true) {
                contentDescription = "$title. $message"
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlayItSpacing.cardPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(GentleCorrectionOrange.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GentleCorrectionOrange,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(PlayItSpacing.default))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(PlayItSpacing.tiny))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp
                ),
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            if (onRetryClick != null) {
                Spacer(modifier = Modifier.height(PlayItSpacing.default))

                SecondaryButton(
                    text = retryText,
                    onClick = onRetryClick,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .height(TouchTarget.RECOMMENDED)
                        .padding(horizontal = PlayItSpacing.default)
                )
            }
        }
    }
}
