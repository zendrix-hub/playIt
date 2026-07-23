package com.playit.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.playit.app.ui.theme.Border
import com.playit.app.ui.theme.CreamWhite
import com.playit.app.ui.theme.GentleCorrectionOrange
import com.playit.app.ui.theme.LearningBlue
import com.playit.app.ui.theme.PlayItSpacing
import com.playit.app.ui.theme.TextPrimary
import com.playit.app.ui.theme.TextSecondary
import com.playit.app.ui.theme.TouchTarget

/**
 * Task UI-10.01 — Polish dialogs (destructive-action arithmetic gate)
 *
 * Polished, Design-System v1.0 compliant arithmetic gate dialog for destructive adult actions.
 * Features LearningCard-family container styling, GentleCorrectionOrange warning severity indicator,
 * explicit consequence messaging, 48dp+ numeric input field, and equally prominent Cancel/Confirm actions.
 */
@Composable
fun ArithmeticGateDialog(
    consequenceMessage: String,
    mathNum1: Int,
    mathNum2: Int,
    answerInput: String,
    onAnswerInputChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Parent Verification",
    operator: String = "+",
    isError: Boolean = false,
    errorMessage: String = "Incorrect answer. Please try again.",
    confirmText: String = "Verify & Proceed",
    cancelText: String = "Cancel"
) {
    Dialog(onDismissRequest = onDismiss) {
        LearningCard(
            modifier = modifier
                .fillMaxWidth()
                .padding(PlayItSpacing.small)
                .semantics(mergeDescendants = true) {
                    contentDescription = "$title. $consequenceMessage What is $mathNum1 $operator $mathNum2?"
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PlayItSpacing.cardPadding)
            ) {
                // Header Warning Icon (GentleCorrectionOrange, never harsh red)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(56.dp)
                        .background(GentleCorrectionOrange.copy(alpha = 0.2f), CircleShape)
                        .border(2.dp, GentleCorrectionOrange.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning icon",
                        tint = GentleCorrectionOrange,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(PlayItSpacing.default))

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(PlayItSpacing.small))

                // Consequence message
                Text(
                    text = consequenceMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    ),
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = PlayItSpacing.tiny)
                )

                Spacer(modifier = Modifier.height(PlayItSpacing.default))

                // Math Challenge Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GentleCorrectionOrange.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GentleCorrectionOrange.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(PlayItSpacing.default)
                    ) {
                        Text(
                            text = "Solve math problem to proceed:",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(PlayItSpacing.tiny))
                        Text(
                            text = "What is $mathNum1 $operator $mathNum2 ?",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp
                            ),
                            color = LearningBlue,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(PlayItSpacing.default))

                // Numeric Input Field (min 48dp height requirement met by construction)
                OutlinedTextField(
                    value = answerInput,
                    onValueChange = onAnswerInputChange,
                    label = { Text("Your Answer") },
                    singleLine = true,
                    isError = isError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirm() }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = TouchTarget.MINIMUM)
                )

                if (isError) {
                    Spacer(modifier = Modifier.height(PlayItSpacing.tiny))
                    Text(
                        text = errorMessage,
                        color = GentleCorrectionOrange,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(PlayItSpacing.cardPadding))

                // Action Buttons: Equally prominent Cancel and Confirm
                Row(
                    horizontalArrangement = Arrangement.spacedBy(PlayItSpacing.default),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Cancel button (SecondaryButton styling)
                    SecondaryButton(
                        text = cancelText,
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(TouchTarget.RECOMMENDED)
                    )

                    // Confirm button (PrimaryButton styling)
                    PrimaryButton(
                        text = confirmText,
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(TouchTarget.RECOMMENDED)
                    )
                }
            }
        }
    }
}
