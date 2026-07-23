package com.playit.app.ui.blendit

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
import com.playit.app.ui.theme.FriendlyPurple
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.PlayItTheme
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary

private val SoftPurpleGradientTop = Color(0xFFECE7FF)
private val SoftPurpleGradientBottom = Color(0xFFD8D0FF)

/**
 * Task UI-5.10 — Polish BlendItCompleteScreen
 *
 * Full compliance for the group-tier celebration screen:
 * - RewardCard with Gold base and Friendly Purple accents
 * - Session summary (words correct, hearts used) at Body 18sp minimum
 * - Single clear "Back to Map" CTA (no competing Retry CTA)
 * - 0-star/session-ended-early outcome copy verified warm, not a dead end
 * - Visually distinct from LetterCompleteScreen via Friendly Purple accents
 */
@Composable
fun BlendItCompleteScreen(
    phonemeId: String = "GROUP_1",
    starsEarned: Int = 3,
    wordsCorrect: Int = 3,
    totalWords: Int = 3,
    heartsUsed: Int = 0,
    onBackToMap: () -> Unit = {}
) {
    val isZeroStar = starsEarned <= 0

    // Pulse animation for auto-focused single CTA
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_blend_cta")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blend_cta_scale"
    )

    val mascotState = if (isZeroStar) MascotState.Encouraging else MascotState.Celebrating

    val mascotMessage = if (isZeroStar) {
        "🌟 Incredible effort! Word blending takes practice, and you're getting stronger every time!"
    } else {
        when (starsEarned) {
            3 -> "Word Challenge Mastered! You earned 3 stars! Outstanding blending!"
            2 -> "Word Challenge Complete! You earned 2 stars! Great job blending words!"
            else -> "Word Challenge Done! You earned 1 star! Wonderful effort blending words!"
        }
    }

    val purpleBgGradient = Brush.verticalGradient(
        listOf(SoftPurpleGradientTop, SoftPurpleGradientBottom)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleBgGradient)
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

            // Spoken celebration / encouragement announcement via MascotBubble
            MascotBubble(
                state = mascotState,
                message = mascotMessage,
                audioResId = 0
            )

            // Reward Card Container with Friendly Purple Badge Accent Header
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
                    // Group Milestone Badge Pill
                    Surface(
                        color = FriendlyPurple,
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 2.dp
                    ) {
                        val groupLabel = if (phonemeId.isNotBlank()) " • ${phonemeId.replace("GROUP_", "GROUP ")}" else ""
                        Text(
                            text = "🔮 WORD CHALLENGE MILESTONE$groupLabel",
                            color = Color.White,
                            fontSize = 14.sp,

                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    // Headline
                    Text(
                        text = if (isZeroStar) "Great Practice Attempt!" else "Word Challenge Complete!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    // Star Rating Row (only if > 0 stars earned)
                    if (!isZeroStar) {
                        StarRating(
                            starsEarned = starsEarned,
                            modifier = Modifier.padding(vertical = PlayItSpacing.small)
                        )
                    }

                    // Session Summary (words correct, hearts used) - Body 18sp minimum
                    Surface(
                        color = CreamWhite,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = PlayItSpacing.small)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "📊 SESSION SUMMARY",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = FriendlyPurple,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Words Blended: $wordsCorrect / $totalWords",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Hearts Remaining: ${maxOf(0, 5 - heartsUsed)} / 5",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Encouraging / Milestone Teaser Note
                    Text(
                        text = if (isZeroStar) {
                            "💪 Keep practicing! Every attempt builds your reading skills!"
                        } else {
                            "🎉 Group milestone achieved! Next letter path unlocked!"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))

            // Single Clear "Back to Map" CTA (No competing Retry CTA)
            PrimaryButton(
                text = "Back to Map",
                onClick = onBackToMap,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(pulseScale)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun BlendItCompleteScreen3StarPreview() {
    PlayItTheme {
        BlendItCompleteScreen(
            phonemeId = "GROUP_1",
            starsEarned = 3,
            wordsCorrect = 3,
            heartsUsed = 0
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BlendItCompleteScreen0StarPreview() {
    PlayItTheme {
        BlendItCompleteScreen(
            phonemeId = "GROUP_1",
            starsEarned = 0,
            wordsCorrect = 1,
            heartsUsed = 5
        )
    }
}
