package com.playit.app.domain.model

data class Profile(
    val profileId: Long,
    val name: String,
    val avatarResId: Int,
    val totalStars: Int,
    val currentStreak: Int,
    val lastPlayedAt: Long?,
    val createdAt: Long
)

data class Phoneme(
    val phonemeId: String,
    val letter: String,
    val audioPath: String,
    val imagePath: String,
    val exampleWord: String
)

data class LessonProgress(
    val id: Long,
    val profileId: Long,
    val phonemeId: String,
    val starsEarned: Int,
    val heartsLost: Int,
    val isCompleted: Boolean,
    val completedAt: Long?
)

data class SayItAttempt(
    val attemptId: Long,
    val profileId: Long,
    val phonemeId: String,
    val isCorrect: Boolean,
    val attemptedAt: Long
)

data class FindItAttempt(
    val attemptId: Long,
    val profileId: Long,
    val phonemeId: String, // Target
    val selectedPhonemeId: String, // Selected
    val isCorrect: Boolean,
    val attemptedAt: Long
)

data class LetterGroup(
    val groupId: String,
    val groupNumber: Int
)

data class LetterGroupMember(
    val memberId: Long,
    val groupId: String,
    val phonemeId: String,
    val position: Int
)

data class BlendItWord(
    val wordId: Long,
    val groupId: String,
    val word: String,
    val wordPattern: String, // e.g. "CVC"
    val audioPath: String,
    val imagePath: String
)

data class BlendItAttempt(
    val attemptId: Long,
    val profileId: Long,
    val groupId: String,
    val wordId: Long,
    val isCorrect: Boolean,
    val attemptedAt: Long
)

data class BlendItProgress(
    val id: Long,
    val profileId: Long,
    val groupId: String,
    val starsEarned: Int,
    val heartsLost: Int,
    val isCompleted: Boolean,
    val completedAt: Long?
)

data class Achievement(
    val achievementId: Long,
    val profileId: Long,
    val title: String,
    val isUnlocked: Boolean,
    val unlockedAt: Long?
)

data class ReportLog(
    val reportId: Long,
    val profileId: Long,
    val filePath: String,
    val generatedAt: Long
)

data class PhonemeWithStatus(
    val phonemeId: String,
    val letter: String,
    val sequenceOrder: Int,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val starsEarned: Int
)

data class LetterCard(
    val id: Int,
    val char: String,
    val isUsed: Boolean = false
)
