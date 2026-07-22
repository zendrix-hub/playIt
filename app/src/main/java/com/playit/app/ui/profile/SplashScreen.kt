package com.playit.app.ui.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.PlayItTheme
import com.playit.app.ui.theme.SoftSky
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary
import kotlinx.coroutines.delay

/**
 * Task UI-5.01 — Polish SplashScreen to Design System v1.0
 *
 * Non-interactive launch screen featuring SoftSky background, mascot idle/breathing motion,
 * clear typography tokens, subtle loading indicator to prevent perceived freezing, and smooth completion callback.
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    val contentScale = remember { Animatable(0.8f) }
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        contentScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )
        delay(1200)
        onSplashComplete()
    }

    // Mascot breathing pulse loop during load
    val transition = rememberInfiniteTransition(label = "splash_mascot_breathing")
    val mascotPulseScale by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mascot_pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftSky),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(contentScale.value)
                .alpha(contentAlpha.value)
                .padding(PlayItSpacing.default)
        ) {
            // Mascot container with breathing animation
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(mascotPulseScale)
                    .clip(CircleShape)
                    .background(FriendlyPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Face,
                    contentDescription = "Mascot",
                    tint = SoftSky,
                    modifier = Modifier.size(72.dp)
                )
            }

            Spacer(modifier = Modifier.height(PlayItSpacing.cardPadding))

            Text(
                text = "playIT",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(PlayItSpacing.tiny))

            Text(
                text = "Learn & Play Phonics",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(PlayItSpacing.section))

            // Subtle loading indicator to provide visual feedback if initialization varies
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = LearningBlue.copy(alpha = 0.7f),
                strokeWidth = 3.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    PlayItTheme {
        SplashScreen(onSplashComplete = {})
    }
}

