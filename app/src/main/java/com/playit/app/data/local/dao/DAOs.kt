package com.playit.app.data.local.dao

import androidx.room.*
import com.playit.app.data.local.entity.*
import com.playit.app.domain.model.PhonemeWithStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    suspend fun getProfileById(profileId: Long): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    @Query("DELETE FROM profiles WHERE profileId = :profileId")
    suspend fun deleteProfile(profileId: Long)
}

@Dao
interface PhonemeDao {
    @Query("SELECT * FROM phonemes")
    fun getAllPhonemes(): Flow<List<PhonemeEntity>>

    @Query("SELECT * FROM phonemes WHERE phonemeId = :phonemeId")
    suspend fun getPhonemeById(phonemeId: String): PhonemeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhonemes(phonemes: List<PhonemeEntity>)

    @Query("""
        SELECT 
            p.phonemeId,
            p.letter,
            (lg.groupNumber * 10 + lgm.position) AS sequenceOrder,
            COALESCE(lp.isCompleted, 0) AS isCompleted,
            COALESCE(lp.starsEarned, 0) AS starsEarned,
            CASE 
                WHEN lg.groupNumber = 1 AND lgm.position = 0 THEN 1
                WHEN (
                    SELECT COALESCE(prev_lp.isCompleted, 0)
                    FROM letter_group_members prev_lgm
                    INNER JOIN letter_groups prev_lg ON prev_lgm.groupId = prev_lg.groupId
                    LEFT JOIN lesson_progress prev_lp ON prev_lgm.phonemeId = prev_lp.phonemeId AND prev_lp.profileId = :profileId
                    WHERE 
                        (prev_lg.groupNumber < lg.groupNumber) OR 
                        (prev_lg.groupNumber = lg.groupNumber AND prev_lgm.position = lgm.position - 1)
                    ORDER BY prev_lg.groupNumber DESC, prev_lgm.position DESC
                    LIMIT 1
                ) = 1 THEN 1
                ELSE 0
            END AS isUnlocked
        FROM phonemes p
        INNER JOIN letter_group_members lgm ON p.phonemeId = lgm.phonemeId
        INNER JOIN letter_groups lg ON lgm.groupId = lg.groupId
        LEFT JOIN lesson_progress lp ON p.phonemeId = lp.phonemeId AND lp.profileId = :profileId
        ORDER BY lg.groupNumber ASC, lgm.position ASC
    """)
    fun getUnlockedPhonemes(profileId: Long): Flow<List<PhonemeWithStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetterGroups(groups: List<LetterGroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetterGroupMembers(members: List<LetterGroupMemberEntity>)
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM lesson_progress WHERE profileId = :profileId AND phonemeId = :phonemeId")
    suspend fun getLessonProgress(profileId: Long, phonemeId: String): LessonProgressEntity?

    @Query("SELECT * FROM lesson_progress WHERE profileId = :profileId AND isCompleted = 1")
    fun getCompletedLessonsForProfile(profileId: Long): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLessonProgress(progress: LessonProgressEntity)
}

@Dao
interface AttemptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSayItAttempt(attempt: SayItAttemptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFindItAttempt(attempt: FindItAttemptEntity)

    @Query("SELECT * FROM find_it_attempts WHERE profileId = :profileId")
    fun getFindItAttempts(profileId: Long): Flow<List<FindItAttemptEntity>>

    @Query("SELECT * FROM say_it_attempts WHERE profileId = :profileId")
    fun getSayItAttempts(profileId: Long): Flow<List<SayItAttemptEntity>>
}

@Dao
interface BlendItDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlendItWords(words: List<BlendItWordEntity>)

    @Query("SELECT * FROM blend_it_words WHERE groupId = :groupId")
    fun getBlendItWords(groupId: String): Flow<List<BlendItWordEntity>>

    @Query("SELECT * FROM blend_it_progress WHERE profileId = :profileId AND groupId = :groupId")
    suspend fun getBlendItProgress(profileId: Long, groupId: String): BlendItProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBlendItProgress(progress: BlendItProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlendItAttempt(attempt: BlendItAttemptEntity)

    @Query("SELECT * FROM blend_it_attempts WHERE profileId = :profileId")
    fun getBlendItAttempts(profileId: Long): Flow<List<BlendItAttemptEntity>>

    @Query("SELECT * FROM letter_groups ORDER BY groupNumber ASC")
    fun getLetterGroups(): Flow<List<LetterGroupEntity>>

    @Query("SELECT * FROM letter_group_members WHERE groupId = :groupId ORDER BY position ASC")
    fun getLetterGroupMembers(groupId: String): Flow<List<LetterGroupMemberEntity>>
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements WHERE profileId = :profileId")
    fun getAchievements(profileId: Long): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE profileId = :profileId")
    suspend fun getAchievementsList(profileId: Long): List<AchievementEntity>

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)
}

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportLog(report: ReportLogEntity)

    @Query("SELECT * FROM report_logs WHERE profileId = :profileId ORDER BY generatedAt DESC")
    fun getReportLogs(profileId: Long): Flow<List<ReportLogEntity>>
}
