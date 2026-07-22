package com.playit.app.domain.repository

import com.playit.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface PlayItRepository {
    // Profile operations
    fun getAllProfiles(): Flow<List<Profile>>
    suspend fun getProfileById(profileId: Long): Profile?
    suspend fun insertProfile(profile: Profile): Long
    suspend fun updateProfile(profile: Profile)
    suspend fun deleteProfile(profileId: Long)

    // Phoneme & Progress operations
    fun getAllPhonemes(): Flow<List<Phoneme>>
    suspend fun getPhonemeById(phonemeId: String): Phoneme?
    suspend fun getLessonProgress(profileId: Long, phonemeId: String): LessonProgress?
    suspend fun updateLessonProgress(progress: LessonProgress)
    fun getUnlockedPhonemes(profileId: Long): Flow<List<PhonemeWithStatus>>

    // Attempt logs for Say It & Find It
    suspend fun insertSayItAttempt(attempt: SayItAttempt)
    suspend fun insertFindItAttempt(attempt: FindItAttempt)
    fun getFindItAttempts(profileId: Long): Flow<List<FindItAttempt>>
    fun getSayItAttempts(profileId: Long): Flow<List<SayItAttempt>>

    // Letter Groups
    fun getLetterGroups(): Flow<List<LetterGroup>>
    fun getLetterGroupMembers(groupId: String): Flow<List<LetterGroupMember>>

    // Blend It operations
    fun getBlendItWords(groupId: String): Flow<List<BlendItWord>>
    suspend fun getBlendItProgress(profileId: Long, groupId: String): BlendItProgress?
    suspend fun updateBlendItProgress(progress: BlendItProgress)
    suspend fun insertBlendItAttempt(attempt: BlendItAttempt)
    fun getBlendItAttempts(profileId: Long): Flow<List<BlendItAttempt>>

    // Achievements
    fun getAchievements(profileId: Long): Flow<List<Achievement>>
    suspend fun updateAchievement(achievement: Achievement)

    // PDF Reports
    suspend fun insertReportLog(report: ReportLog)
    fun getReportLogs(profileId: Long): Flow<List<ReportLog>>
}
