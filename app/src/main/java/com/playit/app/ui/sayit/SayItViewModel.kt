package com.playit.app.ui.sayit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.data.repository.PlayItRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import java.io.File

data class SayItUiState(
    val isModelLoading: Boolean = true,
    val isModelReady: Boolean   = false,
    val isRecording: Boolean    = false,
    val isSuccess: Boolean      = false,
    val partialText: String     = "",
    val resultText: String      = "",
    val errorMessage: String?   = null,
    val activeHearts: Int       = 5,
    val consecutiveCorrect: Int = 0
)

class SayItViewModel(
    private val application: Application,
    private val repository: PlayItRepository, // Injected Repository
    private val phonemeId: String
) : ViewModel(), RecognitionListener {

    private val _uiState = MutableStateFlow(SayItUiState())
    val uiState: StateFlow<SayItUiState> = _uiState.asStateFlow()

    private var voskModel: Model? = null
    private var speechService: SpeechService? = null
    private var resultHandled = false

    init {
        initModel()
    }

    fun initModel() {
        _uiState.update { it.copy(isModelLoading = true, isModelReady = false, errorMessage = null) }

        viewModelScope.launch {
            try {
                val model = withContext(Dispatchers.IO) {
                    val destDir = File(application.filesDir, "model")
                    if (!destDir.exists() || destDir.list().isNullOrEmpty()) {
                        copyAssetFolder(srcName = "model", destDir = destDir)
                    }
                    Model(destDir.absolutePath)
                }
                voskModel = model
                _uiState.update { it.copy(isModelLoading = false, isModelReady = true) }
            } catch (e: Exception) {
                voskModel = null
                _uiState.update {
                    it.copy(
                        isModelLoading = false,
                        isModelReady   = false,
                        errorMessage   = "Model load failed: ${e.message}"
                    )
                }
            }
        }
    }

    private fun copyAssetFolder(srcName: String, destDir: File) {
        destDir.mkdirs()
        val assets   = application.assets
        val children = assets.list(srcName) ?: emptyArray()

        if (children.isEmpty()) {
            val destFile = destDir
            assets.open(srcName).use { input ->
                destFile.outputStream().use { output -> input.copyTo(output) }
            }
        } else {
            for (child in children) {
                val childSrc  = "$srcName/$child"
                val childDest = File(destDir, child)
                if (childDest.exists()) continue
                val grandchildren = assets.list(childSrc) ?: emptyArray()
                if (grandchildren.isEmpty()) {
                    assets.open(childSrc).use { input ->
                        childDest.outputStream().use { output -> input.copyTo(output) }
                    }
                } else {
                    copyAssetFolder(childSrc, childDest)
                }
            }
        }
    }

    fun onRecordButtonClicked() {
        if (_uiState.value.isRecording) stopRecording()
        else startRecording()
    }

    private fun startRecording() {
        val model = voskModel ?: return
        try {
            val recognizer = Recognizer(model, 16000.0f)
            speechService?.shutdown()
            speechService = SpeechService(recognizer, 16000.0f)
            resultHandled = false
            speechService!!.startListening(this)
            _uiState.update {
                it.copy(
                    isRecording  = true,
                    partialText  = "",
                    resultText   = "",
                    errorMessage = null,
                    isSuccess    = false
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Could not start mic: ${e.message}") }
        }
    }

    private fun stopRecording() {
        speechService?.stop()
        _uiState.update { it.copy(isRecording = false) }
    }

    override fun onPartialResult(hypothesis: String) {
        val text = parseVoskJson(hypothesis, "partial")
        if (text.isNotEmpty()) _uiState.update { it.copy(partialText = text) }
    }

    override fun onResult(hypothesis: String) {
        if (resultHandled) return
        val text = parseVoskJson(hypothesis, "text")
        if (text.isNotEmpty()) {
            resultHandled = true
            _uiState.update { it.copy(resultText = text, partialText = "", isRecording = false) }
            evaluateSpeech(text)
        }
    }

    override fun onFinalResult(hypothesis: String) {
        if (resultHandled) return
        val text = parseVoskJson(hypothesis, "text")
        resultHandled = true
        _uiState.update { it.copy(resultText = text, partialText = "", isRecording = false) }
        if (text.isNotEmpty()) evaluateSpeech(text)
    }

    override fun onError(exception: Exception) {
        _uiState.update { it.copy(isRecording = false, errorMessage = exception.message) }
    }

    override fun onTimeout() { stopRecording() }

    private fun evaluateSpeech(heardText: String) {
        val target = phonemeId.lowercase()

        // Phonetic map for Vosk interpreting young children
        val pronunciationMap = mapOf(
            "m" to listOf("m", "em", "um", "am"),
            "s" to listOf("s", "es", "suh", "is"),
            "a" to listOf("a", "ah", "uh", "apple")
        )

        val validSounds = pronunciationMap[target] ?: listOf(target)
        val isMatch = validSounds.any { heardText.lowercase().contains(it) }

        // Failsafe: Stop listening immediately upon parsing a definitive result
        // to prevent the AudioRecord thread from throwing buffer exceptions.
        stopRecording()

        _uiState.update { current ->
            if (isMatch) {
                val newConsecutive = current.consecutiveCorrect + 1
                val heartBonus     = if (newConsecutive % 3 == 0) 1 else 0

                viewModelScope.launch {
                    repository.updateLessonProgress(
                        com.playit.app.data.local.entity.LessonProgress(
                            phonemeId = phonemeId,
                            isCompleted = false,   // Not fully complete until Find It is done
                            starsEarned = 0
                        )
                    )
                }

                current.copy(
                    isSuccess = true,
                    consecutiveCorrect = newConsecutive,
                    activeHearts       = minOf(5, current.activeHearts + heartBonus)
                )
            } else {
                val newHearts = current.activeHearts - 1

                // If hearts empty out, reset back to 3 confidence-building hearts
                if (newHearts <= 0) {
                    current.copy(
                        activeHearts = 3,
                        consecutiveCorrect = 0,
                        partialText = "",
                        resultText = "Try again! 🎧"
                    )
                } else {
                    current.copy(
                        activeHearts = newHearts,
                        consecutiveCorrect = 0,
                        partialText = "",
                        resultText = "Not quite! Try again 🎧"
                    )
                }
            }
        }
    }

    private fun parseVoskJson(json: String, key: String) =
        try { JSONObject(json).getString(key).trim() } catch (e: Exception) { "" }

    override fun onCleared() {
        super.onCleared()
        speechService?.stop()
        speechService?.shutdown()
        speechService = null
        voskModel = null
    }
}

class SayItViewModelFactory(
    private val application: Application,
    private val repository: PlayItRepository,
    private val phonemeId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SayItViewModel::class.java))
            return SayItViewModel(application, repository, phonemeId) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}