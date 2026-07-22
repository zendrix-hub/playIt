package com.playit.app.data.repository

import com.playit.app.data.local.PlayItDatabase
import com.playit.app.data.local.entity.*
import com.playit.app.domain.model.*
import com.playit.app.domain.repository.PlayItRepository
import com.playit.app.domain.usecase.StarCalculator
import com.playit.app.domain.usecase.StreakTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlayItRepositoryImpl(private val db: PlayItDatabase) : PlayItRepository {
    private val profileDao = db.profileDao()
    private val phonemeDao = db.phonemeDao()
    private val progressDao = db.progressDao()
    private val attemptDao = db.attemptDao()
    private val blendItDao = db.blendItDao()
    private val achievementDao = db.achievementDao()
    private val reportDao = db.reportDao()

    private val starCalculator = StarCalculator()
    private val streakTracker = StreakTracker()

    // Profile operations
    override fun getAllProfiles(): Flow<List<Profile>> {
        return profileDao.getAllProfiles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProfileById(profileId: Long): Profile? {
        return profileDao.getProfileById(profileId)?.toDomain()
    }

    override suspend fun insertProfile(profile: Profile): Long {
        val generatedId = profileDao.insertProfile(ProfileEntity.fromDomain(profile))

        // Seed 3 default achievements for this new profile
        val initialAchievements = listOf(
            AchievementEntity(profileId = generatedId, title = "First Steps", isUnlocked = false),
            AchievementEntity(profileId = generatedId, title = "Word Blender", isUnlocked = false),
            AchievementEntity(profileId = generatedId, title = "Streak Master", isUnlocked = false)
        )
        achievementDao.insertAchievements(initialAchievements)

        return generatedId
    }

    override suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(ProfileEntity.fromDomain(profile))
    }

    override suspend fun deleteProfile(profileId: Long) {
        profileDao.deleteProfile(profileId)
    }

    // Phoneme & Progress operations
    override fun getAllPhonemes(): Flow<List<Phoneme>> {
        return phonemeDao.getAllPhonemes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getPhonemeById(phonemeId: String): Phoneme? {
        return phonemeDao.getPhonemeById(phonemeId)?.toDomain()
    }

    override suspend fun getLessonProgress(profileId: Long, phonemeId: String): LessonProgress? {
        return progressDao.getLessonProgress(profileId, phonemeId)?.toDomain()
    }

    override fun getCompletedLessonsForProfile(profileId: Long): Flow<List<LessonProgress>> {
        return progressDao.getCompletedLessonsForProfile(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateLessonProgress(progress: LessonProgress) {
        // 1. Perform the upsert of the progress record
        progressDao.upsertLessonProgress(LessonProgressEntity.fromDomain(progress))

        // 2. If this lesson progress completion is true, recalculate the profile stats
        if (progress.isCompleted) {
            val profileId = progress.profileId
            val profile = profileDao.getProfileById(profileId)?.toDomain()
            if (profile != null) {
                // Fetch all completed lessons to sum up stars
                val completedEntities = progressDao.getCompletedLessonsForProfile(profileId).first()
                val starsList = completedEntities.map { it.starsEarned }
                val newStars = starCalculator.calculateTotalStars(starsList)

                // Track consecutive streak updates
                val currentTimestamp = System.currentTimeMillis()
                val (newStreak, updatedTimestamp) = streakTracker.updateStreak(
                    currentStreak = profile.currentStreak,
                    lastPlayedTimestamp = profile.lastPlayedAt,
                    currentTimestamp = currentTimestamp
                )

                // Update the profile entity inside the database
                val updatedProfile = profile.copy(
                    totalStars = newStars,
                    currentStreak = newStreak,
                    lastPlayedAt = updatedTimestamp
                )
                profileDao.updateProfile(ProfileEntity.fromDomain(updatedProfile))

                // 3. Process achievements
                // Milestone 1: First Steps (Complete 1st lesson)
                if (completedEntities.isNotEmpty()) {
                    unlockAchievement(profileId, "First Steps")
                }

                // Milestone 3: Streak Master (3-day streak)
                if (newStreak >= 3) {
                    unlockAchievement(profileId, "Streak Master")
                }
            }
        }
    }

    private suspend fun unlockAchievement(profileId: Long, title: String) {
        try {
            val achievements = achievementDao.getAchievementsList(profileId)
            val target = achievements.find { it.title.equals(title, ignoreCase = true) }
            if (target != null) {
                if (!target.isUnlocked) {
                    val updated = target.copy(isUnlocked = true, unlockedAt = System.currentTimeMillis())
                    achievementDao.updateAchievement(updated)
                }
            } else {
                // Failsafe insertion if profile had no achievements seeded
                val newAchievement = AchievementEntity(
                    profileId = profileId,
                    title = title,
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis()
                )
                achievementDao.insertAchievements(listOf(newAchievement))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getUnlockedPhonemes(profileId: Long): Flow<List<PhonemeWithStatus>> {
        return phonemeDao.getUnlockedPhonemes(profileId)
    }

    // Attempt logs
    override suspend fun insertSayItAttempt(attempt: SayItAttempt) {
        attemptDao.insertSayItAttempt(SayItAttemptEntity.fromDomain(attempt))
    }

    override suspend fun insertFindItAttempt(attempt: FindItAttempt) {
        attemptDao.insertFindItAttempt(FindItAttemptEntity.fromDomain(attempt))
    }

    override fun getFindItAttempts(profileId: Long): Flow<List<FindItAttempt>> {
        return attemptDao.getFindItAttempts(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getSayItAttempts(profileId: Long): Flow<List<SayItAttempt>> {
        return attemptDao.getSayItAttempts(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Letter Groups
    override fun getLetterGroups(): Flow<List<LetterGroup>> {
        return blendItDao.getLetterGroups().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLetterGroupMembers(groupId: String): Flow<List<LetterGroupMember>> {
        return blendItDao.getLetterGroupMembers(groupId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Blend It operations
    override fun getBlendItWords(groupId: String): Flow<List<BlendItWord>> {
        return blendItDao.getBlendItWords(groupId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getBlendItProgress(profileId: Long, groupId: String): BlendItProgress? {
        return blendItDao.getBlendItProgress(profileId, groupId)?.toDomain()
    }

    override suspend fun updateBlendItProgress(progress: BlendItProgress) {
        blendItDao.upsertBlendItProgress(BlendItProgressEntity.fromDomain(progress))

        // Also update profile stats if Blend It progress is completed
        if (progress.isCompleted) {
            val profileId = progress.profileId
            val profile = profileDao.getProfileById(profileId)?.toDomain()
            if (profile != null) {
                val currentTimestamp = System.currentTimeMillis()
                val (newStreak, updatedTimestamp) = streakTracker.updateStreak(
                    currentStreak = profile.currentStreak,
                    lastPlayedTimestamp = profile.lastPlayedAt,
                    currentTimestamp = currentTimestamp
                )
                val updatedProfile = profile.copy(
                    currentStreak = newStreak,
                    lastPlayedAt = updatedTimestamp
                )
                profileDao.updateProfile(ProfileEntity.fromDomain(updatedProfile))

                // Milestone 2: Word Blender (Complete first Blend It checkpoint)
                unlockAchievement(profileId, "Word Blender")
            }
        }
    }

    override suspend fun insertBlendItAttempt(attempt: BlendItAttempt) {
        blendItDao.insertBlendItAttempt(BlendItAttemptEntity.fromDomain(attempt))
    }

    override fun getBlendItAttempts(profileId: Long): Flow<List<BlendItAttempt>> {
        return blendItDao.getBlendItAttempts(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Achievements
    override fun getAchievements(profileId: Long): Flow<List<Achievement>> {
        return achievementDao.getAchievements(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateAchievement(achievement: Achievement) {
        achievementDao.updateAchievement(AchievementEntity.fromDomain(achievement))
    }

    // PDF Reports
    override suspend fun insertReportLog(report: ReportLog) {
        reportDao.insertReportLog(ReportLogEntity.fromDomain(report))
    }

    override fun getReportLogs(profileId: Long): Flow<List<ReportLog>> {
        return reportDao.getReportLogs(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
