package com.playit.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.playit.app.data.local.entity.AppConfig
import com.playit.app.data.local.entity.LessonProgress
import com.playit.app.data.local.entity.Phoneme
import com.playit.app.data.local.model.PhonemeWithStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayItDao {
    @Query("SELECT * FROM app_config WHERE id = 1")
    fun getAppConfig(): Flow<AppConfig?>

    @Upsert
    suspend fun updateAppConfig(config: AppConfig)

    @Query("SELECT * FROM phoneme ORDER BY sequenceOrder ASC")
    fun getAllPhonemes(): Flow<List<Phoneme>>

    @Query("SELECT * FROM lesson_progress WHERE phonemeId = :phonemeId")
    suspend fun getLessonProgress(phonemeId: String): LessonProgress?

    @Upsert
    suspend fun updateLessonProgress(progress: LessonProgress)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhonemes(phonemes: List<Phoneme>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAppConfig(config: AppConfig)

    // The core gamification progression logic pushed to SQLite
    @Query("""
        SELECT 
            p.phonemeId, 
            p.letter, 
            p.sequenceOrder,
            COALESCE(lp.isCompleted, 0) AS isCompleted,
            COALESCE(lp.starsEarned, 0) AS starsEarned,
            CASE 
                WHEN p.sequenceOrder = 0 THEN 1
                WHEN (SELECT isCompleted FROM lesson_progress WHERE phonemeId = (SELECT phonemeId FROM phoneme WHERE sequenceOrder = p.sequenceOrder - 1)) = 1 THEN 1
                ELSE 0
            END AS isUnlocked
        FROM phoneme p
        LEFT JOIN lesson_progress lp ON p.phonemeId = lp.phonemeId
        ORDER BY p.sequenceOrder ASC
    """)
    fun getUnlockedPhonemes(): Flow<List<PhonemeWithStatus>>
}