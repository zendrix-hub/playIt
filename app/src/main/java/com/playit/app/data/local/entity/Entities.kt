package com.playit.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.playit.app.domain.model.*

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true) val profileId: Long = 0,
    val name: String,
    val avatarResId: Int,
    val totalStars: Int = 0,
    val currentStreak: Int = 0,
    val lastPlayedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain() = Profile(
        profileId = profileId,
        name = name,
        avatarResId = avatarResId,
        totalStars = totalStars,
        currentStreak = currentStreak,
        lastPlayedAt = lastPlayedAt,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(domain: Profile) = ProfileEntity(
            profileId = domain.profileId,
            name = domain.name,
            avatarResId = domain.avatarResId,
            totalStars = domain.totalStars,
            currentStreak = domain.currentStreak,
            lastPlayedAt = domain.lastPlayedAt,
            createdAt = domain.createdAt
        )
    }
}

@Entity(tableName = "phonemes")
data class PhonemeEntity(
    @PrimaryKey val phonemeId: String,
    val letter: String,
    val audioPath: String,
    val imagePath: String,
    val exampleWord: String
) {
    fun toDomain() = Phoneme(
        phonemeId = phonemeId,
        letter = letter,
        audioPath = audioPath,
        imagePath = imagePath,
        exampleWord = exampleWord
    )

    companion object {
        fun fromDomain(domain: Phoneme) = PhonemeEntity(
            phonemeId = domain.phonemeId,
            letter = domain.letter,
            audioPath = domain.audioPath,
            imagePath = domain.imagePath,
            exampleWord = domain.exampleWord
        )
    }
}

@Entity(
    tableName = "lesson_progress",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhonemeEntity::class,
            parentColumns = ["phonemeId"],
            childColumns = ["phonemeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profileId", "phonemeId"], unique = true), Index("phonemeId")]
)
data class LessonProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: Long,
    val phonemeId: String,
    val starsEarned: Int = 0,
    val heartsLost: Int = 0,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
) {
    fun toDomain() = LessonProgress(
        id = id,
        profileId = profileId,
        phonemeId = phonemeId,
        starsEarned = starsEarned,
        heartsLost = heartsLost,
        isCompleted = isCompleted,
        completedAt = completedAt
    )

    companion object {
        fun fromDomain(domain: LessonProgress) = LessonProgressEntity(
            id = domain.id,
            profileId = domain.profileId,
            phonemeId = domain.phonemeId,
            starsEarned = domain.starsEarned,
            heartsLost = domain.heartsLost,
            isCompleted = domain.isCompleted,
            completedAt = domain.completedAt
        )
    }
}

@Entity(
    tableName = "say_it_attempts",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhonemeEntity::class,
            parentColumns = ["phonemeId"],
            childColumns = ["phonemeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("phonemeId")]
)
data class SayItAttemptEntity(
    @PrimaryKey(autoGenerate = true) val attemptId: Long = 0,
    val profileId: Long,
    val phonemeId: String,
    val isCorrect: Boolean,
    val attemptedAt: Long
) {
    fun toDomain() = SayItAttempt(
        attemptId = attemptId,
        profileId = profileId,
        phonemeId = phonemeId,
        isCorrect = isCorrect,
        attemptedAt = attemptedAt
    )

    companion object {
        fun fromDomain(domain: SayItAttempt) = SayItAttemptEntity(
            attemptId = domain.attemptId,
            profileId = domain.profileId,
            phonemeId = domain.phonemeId,
            isCorrect = domain.isCorrect,
            attemptedAt = domain.attemptedAt
        )
    }
}

@Entity(
    tableName = "find_it_attempts",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhonemeEntity::class,
            parentColumns = ["phonemeId"],
            childColumns = ["phonemeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("phonemeId")]
)
data class FindItAttemptEntity(
    @PrimaryKey(autoGenerate = true) val attemptId: Long = 0,
    val profileId: Long,
    val phonemeId: String,
    val selectedPhonemeId: String,
    val isCorrect: Boolean,
    val attemptedAt: Long
) {
    fun toDomain() = FindItAttempt(
        attemptId = attemptId,
        profileId = profileId,
        phonemeId = phonemeId,
        selectedPhonemeId = selectedPhonemeId,
        isCorrect = isCorrect,
        attemptedAt = attemptedAt
    )

    companion object {
        fun fromDomain(domain: FindItAttempt) = FindItAttemptEntity(
            attemptId = domain.attemptId,
            profileId = domain.profileId,
            phonemeId = domain.phonemeId,
            selectedPhonemeId = domain.selectedPhonemeId,
            isCorrect = domain.isCorrect,
            attemptedAt = domain.attemptedAt
        )
    }
}

@Entity(tableName = "letter_groups")
data class LetterGroupEntity(
    @PrimaryKey val groupId: String,
    val groupNumber: Int
) {
    fun toDomain() = LetterGroup(
        groupId = groupId,
        groupNumber = groupNumber
    )

    companion object {
        fun fromDomain(domain: LetterGroup) = LetterGroupEntity(
            groupId = domain.groupId,
            groupNumber = domain.groupNumber
        )
    }
}

@Entity(
    tableName = "letter_group_members",
    foreignKeys = [
        ForeignKey(
            entity = LetterGroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhonemeEntity::class,
            parentColumns = ["phonemeId"],
            childColumns = ["phonemeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId"), Index("phonemeId")]
)
data class LetterGroupMemberEntity(
    @PrimaryKey(autoGenerate = true) val memberId: Long = 0,
    val groupId: String,
    val phonemeId: String,
    val position: Int
) {
    fun toDomain() = LetterGroupMember(
        memberId = memberId,
        groupId = groupId,
        phonemeId = phonemeId,
        position = position
    )

    companion object {
        fun fromDomain(domain: LetterGroupMember) = LetterGroupMemberEntity(
            memberId = domain.memberId,
            groupId = domain.groupId,
            phonemeId = domain.phonemeId,
            position = domain.position
        )
    }
}

@Entity(
    tableName = "blend_it_words",
    foreignKeys = [
        ForeignKey(
            entity = LetterGroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("groupId"),
        Index(value = ["groupId", "word"], unique = true)
    ]
)
data class BlendItWordEntity(
    @PrimaryKey(autoGenerate = true) val wordId: Long = 0,
    val groupId: String,
    val word: String,
    val wordPattern: String,
    val audioPath: String,
    val imagePath: String
) {
    fun toDomain() = BlendItWord(
        wordId = wordId,
        groupId = groupId,
        word = word,
        wordPattern = wordPattern,
        audioPath = audioPath,
        imagePath = imagePath
    )

    companion object {
        fun fromDomain(domain: BlendItWord) = BlendItWordEntity(
            wordId = domain.wordId,
            groupId = domain.groupId,
            word = domain.word,
            wordPattern = domain.wordPattern,
            audioPath = domain.audioPath,
            imagePath = domain.imagePath
        )
    }
}

@Entity(
    tableName = "blend_it_attempts",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LetterGroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BlendItWordEntity::class,
            parentColumns = ["wordId"],
            childColumns = ["wordId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("groupId"), Index("wordId")]
)
data class BlendItAttemptEntity(
    @PrimaryKey(autoGenerate = true) val attemptId: Long = 0,
    val profileId: Long,
    val groupId: String,
    val wordId: Long,
    val isCorrect: Boolean,
    val attemptedAt: Long
) {
    fun toDomain() = BlendItAttempt(
        attemptId = attemptId,
        profileId = profileId,
        groupId = groupId,
        wordId = wordId,
        isCorrect = isCorrect,
        attemptedAt = attemptedAt
    )

    companion object {
        fun fromDomain(domain: BlendItAttempt) = BlendItAttemptEntity(
            attemptId = domain.attemptId,
            profileId = domain.profileId,
            groupId = domain.groupId,
            wordId = domain.wordId,
            isCorrect = domain.isCorrect,
            attemptedAt = domain.attemptedAt
        )
    }
}

@Entity(
    tableName = "blend_it_progress",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LetterGroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profileId", "groupId"], unique = true), Index("groupId")]
)
data class BlendItProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: Long,
    val groupId: String,
    val starsEarned: Int = 0,
    val heartsLost: Int = 0,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
) {
    fun toDomain() = BlendItProgress(
        id = id,
        profileId = profileId,
        groupId = groupId,
        starsEarned = starsEarned,
        heartsLost = heartsLost,
        isCompleted = isCompleted,
        completedAt = completedAt
    )

    companion object {
        fun fromDomain(domain: BlendItProgress) = BlendItProgressEntity(
            id = domain.id,
            profileId = domain.profileId,
            groupId = domain.groupId,
            starsEarned = domain.starsEarned,
            heartsLost = domain.heartsLost,
            isCompleted = domain.isCompleted,
            completedAt = domain.completedAt
        )
    }
}

@Entity(
    tableName = "achievements",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId")]
)
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true) val achievementId: Long = 0,
    val profileId: Long,
    val title: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
) {
    fun toDomain() = Achievement(
        achievementId = achievementId,
        profileId = profileId,
        title = title,
        isUnlocked = isUnlocked,
        unlockedAt = unlockedAt
    )

    companion object {
        fun fromDomain(domain: Achievement) = AchievementEntity(
            achievementId = domain.achievementId,
            profileId = domain.profileId,
            title = domain.title,
            isUnlocked = domain.isUnlocked,
            unlockedAt = domain.unlockedAt
        )
    }
}

@Entity(
    tableName = "report_logs",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId")]
)
data class ReportLogEntity(
    @PrimaryKey(autoGenerate = true) val reportId: Long = 0,
    val profileId: Long,
    val filePath: String,
    val generatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain() = ReportLog(
        reportId = reportId,
        profileId = profileId,
        filePath = filePath,
        generatedAt = generatedAt
    )

    companion object {
        fun fromDomain(domain: ReportLog) = ReportLogEntity(
            reportId = domain.reportId,
            profileId = domain.profileId,
            filePath = domain.filePath,
            generatedAt = domain.generatedAt
        )
    }
}