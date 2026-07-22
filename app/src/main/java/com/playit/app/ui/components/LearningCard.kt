package com.playit.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.cardElevation
import com.playit.app.ui.theme.learningCardShape

/**
 * LearningCard - Shared card container for letter/phoneme/feedback cards.
 *
 * Enforces:
 * - Radius: 24dp (learningCardShape)
 * - Elevation: 4dp (cardElevation)
 * - Padding: 24dp (PlayItSpacing.cardPadding)
 * - Color: Cream White (#FFFDF8)
 */
@Composable
fun LearningCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    if (onClick != null) {
        Surface(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            shape = learningCardShape,
            color = CreamWhite,
            shadowElevation = cardElevation
        ) {
            Box(
                modifier = Modifier.padding(PlayItSpacing.cardPadding),
                content = content
            )
        }
    } else {
        Surface(
            modifier = modifier,
            shape = learningCardShape,
            color = CreamWhite,
            shadowElevation = cardElevation
        ) {
            Box(
                modifier = Modifier.padding(PlayItSpacing.cardPadding),
                content = content
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LearningCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        LearningCard {
            Text(
                text = "Aa",
                color = TextPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
