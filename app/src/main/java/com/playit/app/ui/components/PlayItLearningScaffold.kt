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

// --- Child-Friendly Design Tokens ---
val SkyBlueLight = Color(0xFFE1F5FE)
val SkyBlueDark = Color(0xFF81D4FA)
val CardWhite = Color(0xFFFFFFFF)
val ActionGreen = Color(0xFF43E97B)
val DisabledGray = Color(0xFFB0BEC5)
val TextDark = Color(0xFF2D2D2D)
val HeartRed = Color(0xFFFF4B6E)

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
                    Surface(shape = RoundedCornerShape(50), color = CardWhite.copy(alpha = 0.5f)) {
                        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            repeat(activeHearts) {
                                Icon(Icons.Filled.Favorite, contentDescription = null, tint = HeartRed, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
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