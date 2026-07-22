package com.playit.app.ui.parent

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.PlayItApplication
import com.playit.app.data.preferences.SessionManager
import com.playit.app.data.report.ProgressReportPdfGenerator
import com.playit.app.domain.model.FindItAttempt
import com.playit.app.domain.model.LessonProgress
import com.playit.app.domain.model.Profile
import com.playit.app.domain.model.SayItAttempt
import com.playit.app.domain.repository.PlayItRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class ParentUiState(
    val activeProfile: Profile? = null,
    val completedLessons: List<LessonProgress> = emptyList(),
    val findAttempts: List<FindItAttempt> = emptyList(),
    val sayAttempts: List<SayItAttempt> = emptyList(),
    val isGeneratingPdf: Boolean = false,
    val pdfFileUri: Uri? = null,
    val error: String? = null
)

class ParentViewModel(
    private val application: Application,
    private val repository: PlayItRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ParentUiState())
    val uiState: StateFlow<ParentUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val activeProfileId = SessionManager.activeProfileId
        if (activeProfileId == -1L) return

        viewModelScope.launch {
            val profile = repository.getProfileById(activeProfileId)
            _uiState.update { it.copy(activeProfile = profile) }

        viewModelScope.launch {
            repository.getCompletedLessonsForProfile(activeProfileId).collect { list ->
                _uiState.update { it.copy(completedLessons = list) }
            }
        }
        }

        viewModelScope.launch {
            repository.getFindItAttempts(activeProfileId).collect { list ->
                _uiState.update { it.copy(findAttempts = list) }
            }
        }

        viewModelScope.launch {
            repository.getSayItAttempts(activeProfileId).collect { list ->
                _uiState.update { it.copy(sayAttempts = list) }
            }
        }
    }

    fun exportAndShareReport(context: Context, onIntentReady: (Uri) -> Unit) {
        val current = _uiState.value
        val profile = current.activeProfile ?: return
        _uiState.update { it.copy(isGeneratingPdf = true, error = null) }

        viewModelScope.launch {
            try {
                val generator = ProgressReportPdfGenerator(application)
                val pdfFile = generator.generateReport(
                    profile = profile,
                    lessons = current.completedLessons,
                    findAttempts = current.findAttempts,
                    sayAttempts = current.sayAttempts
                )

                if (pdfFile != null && pdfFile.exists()) {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "com.playit.app.fileprovider",
                        pdfFile
                    )
                    _uiState.update { it.copy(isGeneratingPdf = false, pdfFileUri = uri) }
                    onIntentReady(uri)
                } else {
                    _uiState.update { it.copy(isGeneratingPdf = false, error = "Failed to write PDF file.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isGeneratingPdf = false, error = "Error generating report: ${e.message}") }
            }
        }
    }

    class ParentViewModelFactory(
        private val application: Application,
        private val repository: PlayItRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ParentViewModel::class.java)) {
                return ParentViewModel(application, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
