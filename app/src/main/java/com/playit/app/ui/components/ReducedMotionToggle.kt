package com.playit.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.a11y.ReducedMotionState

/**
 * ReducedMotionToggle - Parent Dashboard accessibility toggle component.
 * Directly exposes and controls the [ReducedMotionState] preference for sensory-friendly learning.
 *
 * Enforces strict 7:1+ contrast text and iconography for the adult Parent Dashboard standard.
 */
@Composable
fun ReducedMotionToggle(
    checked: Boolean = ReducedMotionState.current,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Accessibility,
                contentDescription = null,
                tint = Color(0xFF455A64), // 7.17:1 AAA contrast
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = "Reduced Motion",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A202C) // 11.5:1 AAA contrast
                )
                Text(
                    text = "Replaces bouncy and particle animations with simple fades.",
                    fontSize = 14.sp,
                    color = Color(0xFF455A64) // 7.17:1 AAA contrast
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
