package com.playit.app.ui.completion

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.components.MascotBubble
import com.playit.app.ui.components.MascotState
import com.playit.app.ui.components.PrimaryButton
import com.playit.app.ui.components.RewardCard
import com.playit.app.ui.components.StarRating
import com.playit.app.ui.theme.AchievementGold
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.EnergyOrange
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.PlayItTheme
import com.playit.app.ui.theme.SoftSky
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary

/**
 * Task UI-5.08 — Polish LetterCompleteScreen
 *
 * Full compliance for the letter completion celebration screen:
 * - RewardCard container styling (UI-4.05)
 * - Exactly one primary CTA ("Continue" to Map) with subtle idle pulse
 * - Star count announced verbally via MascotBubble
 * - Non-punitive 1-star celebratory tone
 * - Map progress teaser & example word recap
 * - Reusable StarRating display stubbed for Phase 7 sequencing
 */
@Composable
fun LetterCompleteScreen(
    phonemeId: String = "m",
    starsEarned: Int = 3,
    onContinueToMap: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val soundManager = remember(context) { com.playit.app.data.audio.SoundManager.getInstance(context) }

    LaunchedEffect(Unit) {
        soundManager.playLevelComplete()
    }

    val letterUpper = phonemeId.uppercase()
    val exampleWord = getExampleWordForPhoneme(phonemeId)

    // Pulse animation for auto-focused single CTA
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_cta")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cta_scale"
    )

    // Construct non-punitive spoken celebration message
    val starMessage = when (starsEarned) {
        3 -> "You earned 3 stars! Outstanding performance!"
        2 -> "You earned 2 stars! Great job!"
        else -> "You earned 1 star! Wonderful effort!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftSky)
            .padding(PlayItSpacing.default),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = PlayItSpacing.default),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PlayItSpacing.section)
        ) {

            // Spoken celebration announcement via MascotBubble
            MascotBubble(
                state = MascotState.Celebrating,
                message = starMessage,
                audioResId = 0
            )

            // Reward Card Container
            RewardCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = PlayItSpacing.default),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(PlayItSpacing.default)
                ) {
                    // Headline
                    Text(
                        text = "Letter $letterUpper Mastered!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    // Star Rating Row
                    StarRating(
                        starsEarned = starsEarned,
                        modifier = Modifier.padding(vertical = PlayItSpacing.small)
                    )

                    // Example Word Recap
                    if (exampleWord.isNotEmpty()) {
                        Surface(
                            color = CreamWhite,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(horizontal = PlayItSpacing.default)
                        ) {
                            Text(
                                text = "✨ '$letterUpper' is for $exampleWord!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // Map Progress Teaser
                    Text(
                        text = "🚀 Next letter unlocked on your Map!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))

            // Exactly ONE Primary CTA ("Continue" to Map) with subtle idle pulse
            PrimaryButton(
                text = "Continue to Map",
                onClick = onContinueToMap,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(pulseScale)
            )
        }
    }
}



private fun getExampleWordForPhoneme(phonemeId: String): String {
    return when (phonemeId.lowercase()) {
        "m" -> "Monkey"
        "a" -> "Apple"
        "t" -> "Tiger"
        "s" -> "Sun"
        "p" -> "Panda"
        "f" -> "Fish"
        "i" -> "Igloo"
        "n" -> "Nest"
        else -> phonemeId.uppercase()
    }
}

@Preview(showBackground = true)
@Composable
fun LetterCompleteScreen3StarPreview() {
    PlayItTheme {
        LetterCompleteScreen(phonemeId = "m", starsEarned = 3)
    }
}

@Preview(showBackground = true)
@Composable
fun LetterCompleteScreen1StarPreview() {
    PlayItTheme {
        LetterCompleteScreen(phonemeId = "a", starsEarned = 1)
    }
}
