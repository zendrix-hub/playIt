package com.playit.app.data.repository

import com.playit.app.data.local.dao.PlayItDao
import com.playit.app.data.local.entity.AppConfig
import com.playit.app.data.local.entity.LessonProgress
import com.playit.app.data.local.entity.Phoneme
import com.playit.app.data.local.model.PhonemeWithStatus
import kotlinx.coroutines.flow.Flow
import com.playit.app.data.local.BlendMasterDictionary

interface PlayItRepository {
    fun getAppConfig(): Flow<AppConfig?>
    suspend fun updateAppConfig(config: AppConfig)
    fun getAllPhonemes(): Flow<List<Phoneme>>
    suspend fun getLessonProgress(phonemeId: String): LessonProgress?
    suspend fun updateLessonProgress(progress: LessonProgress)
    fun getUnlockedPhonemes(): Flow<List<PhonemeWithStatus>>
    suspend fun getAvailableBlends(unlockedLetters: Set<Char>): List<String>
}

class PlayItRepositoryImpl(private val dao: PlayItDao) : PlayItRepository {
    override fun getAppConfig(): Flow<AppConfig?> = dao.getAppConfig()

    override suspend fun updateAppConfig(config: AppConfig) {
        dao.updateAppConfig(config)
    }

    override fun getAllPhonemes(): Flow<List<Phoneme>> = dao.getAllPhonemes()

    override suspend fun getLessonProgress(phonemeId: String): LessonProgress? {
        return dao.getLessonProgress(phonemeId)
    }

    override suspend fun updateLessonProgress(progress: LessonProgress) {
        dao.updateLessonProgress(progress)
    }

    override fun getUnlockedPhonemes(): Flow<List<PhonemeWithStatus>> {
        return dao.getUnlockedPhonemes()
    }
    override suspend fun getAvailableBlends(unlockedChars: Set<Char>): List<String> {
        return BlendMasterDictionary.getAvailableWords(unlockedChars)
    }
}