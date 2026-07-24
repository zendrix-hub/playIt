package com.playit.app.ui.report

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.components.LearningCard
import com.playit.app.ui.components.LoadingIndicator
import com.playit.app.ui.components.PrimaryButton
import com.playit.app.ui.components.SecondaryButton
import com.playit.app.ui.parent.ParentViewModel
import com.playit.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPreviewScreen(
    viewModel: ParentViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        val speechListener = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Initialized
            }
        }
        tts = speechListener
        onDispose {
            speechListener.stop()
            speechListener.shutdown()
        }
    }

    fun speakConfirmation(message: String) {
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "REPORT_SPEAK_ID")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    val handleSharePdf = { uri: Uri ->
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        speakConfirmation("Progress report ready for sharing.")
        context.startActivity(Intent.createChooser(shareIntent, "Share Progress Report"))
    }

    val handleViewPdf = { uri: Uri ->
        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        speakConfirmation("Opening progress report PDF.")
        try {
            context.startActivity(viewIntent)
        } catch (e: Exception) {
            handleSharePdf(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Progress Report Preview",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.semantics { contentDescription = "Back to Parent Dashboard" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftSky)
            )
        },
        containerColor = CreamWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(PlayItSpacing.default),
            verticalArrangement = Arrangement.spacedBy(PlayItSpacing.cardPadding)
        ) {
            // Explainer Card
            LearningCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(PlayItSpacing.default),
                    verticalArrangement = Arrangement.spacedBy(PlayItSpacing.small)
                ) {
                    Text(
                        text = "Child Learning Progress Report",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LearningBlueDeep
                    )
                    Text(
                        text = "Comprehensive summary of letter sound mastery, accuracy scores, practice streaks, and recommended focus areas for ${uiState.activeProfile?.name ?: "your child"}.",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }

            // Report Preview Box / Container
            LearningCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Border, learningCardShape)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PlayItSpacing.cardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(PlayItSpacing.default)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(SoftSky),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "PDF Document Icon",
                            tint = LearningBlueDeep,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "playIT Progress Report — ${uiState.activeProfile?.name ?: "Child"}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    if (uiState.isGeneratingPdf) {
                        LoadingIndicator(
                            message = "Generating PDF document...",
                            showMessage = true,
                            inline = true,
                            size = 24.dp,
                            color = LearningBlueDeep
                        )
                    } else if (uiState.pdfFileUri != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(PlayItSpacing.tiny)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GrowthGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "PDF document ready for save & export",
                                fontSize = 16.sp,
                                color = GrowthGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        Text(
                            text = "Tap export below to generate and preview the report file.",
                            fontSize = 16.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "",
                            fontSize = 16.sp,
                            color = GentleCorrectionOrange,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(PlayItSpacing.default)
            ) {
                PrimaryButton(
                    text = if (uiState.isGeneratingPdf) "Generating PDF..." else "Share & Export PDF Report 📄",
                    onClick = {
                        val uri = uiState.pdfFileUri
                        if (uri != null) {
                            handleSharePdf(uri)
                        } else {
                            viewModel.exportAndShareReport(context, handleSharePdf)
                        }
                    },
                    enabled = !uiState.isGeneratingPdf,
                    modifier = Modifier.fillMaxWidth()
                )

                SecondaryButton(
                    text = "Open PDF Document 👁️",
                    onClick = {
                        val uri = uiState.pdfFileUri
                        if (uri != null) {
                            handleViewPdf(uri)
                        } else {
                            viewModel.exportAndShareReport(context, handleViewPdf)
                        }
                    },
                    enabled = !uiState.isGeneratingPdf,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
