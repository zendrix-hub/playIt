package com.playit.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.playit.app.ui.theme.*

// --- Child-Friendly Design Tokens ---
val SkyBlueLight = SoftSky
val SkyBlueDark = LearningBlue.copy(alpha = 0.4f)
val CardWhite = CreamWhite
val ActionGreen = GrowthGreen
val DisabledGray = Disabled
val TextDark = TextPrimary
val HeartRed = GentleCorrectionOrange

@Composable
fun PlayItLearningScaffold(
    title: String,
    activeHearts: Int? = 3,
    isNextEnabled: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    centerContent: @Composable BoxScope.() -> Unit,
    actionButton: @Composable () -> Unit
) {
    // Soft, calming background to reduce cognitive load
    val bgBrush = Brush.verticalGradient(listOf(SkyBlueLight, SkyBlueDark))

    Box(modifier = Modifier.fillMaxSize().background(bgBrush)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ZONE 1: THE SAFE ZONE (Top Bar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark, modifier = Modifier.size(32.dp))
                }

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextDark,
                    letterSpacing = 2.sp
                )

                if (activeHearts != null) {
                    HeartDisplay(activeHearts = activeHearts)
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // ZONE 2: THE FOCUS ZONE (Center)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().weight(2f)
            ) {
                centerContent()
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // ZONE 3: THE ACTION ZONE (Bottom)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
            ) {
                // The main interaction driver (Play, Mic, or Blend)
                actionButton()

                Spacer(modifier = Modifier.height(24.dp))

                // The predictable NEXT button
                val nextColor by animateColorAsState(
                    targetValue = if (isNextEnabled) ActionGreen else DisabledGray,
                    label = "nextColor"
                )

                Button(
                    onClick = onNextClick,
                    enabled = isNextEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = nextColor,
                        disabledContainerColor = DisabledGray
                    ),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(64.dp)
                ) {
                    Text("NEXT", fontSize = 24.sp, fontWeight = FontWeight.Black, color = CardWhite)
                }
            }
        }
    }
}