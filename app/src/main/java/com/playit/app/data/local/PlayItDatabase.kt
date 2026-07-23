package com.playit.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.playit.app.data.local.dao.*
import com.playit.app.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Database(
    entities = [
        ProfileEntity::class,
        PhonemeEntity::class,
        LessonProgressEntity::class,
        SayItAttemptEntity::class,
        FindItAttemptEntity::class,
        LetterGroupEntity::class,
        LetterGroupMemberEntity::class,
        BlendItWordEntity::class,
        BlendItAttemptEntity::class,
        BlendItProgressEntity::class,
        AchievementEntity::class,
        ReportLogEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class PlayItDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun phonemeDao(): PhonemeDao
    abstract fun progressDao(): ProgressDao
    abstract fun attemptDao(): AttemptDao
    abstract fun blendItDao(): BlendItDao
    abstract fun achievementDao(): AchievementDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: PlayItDatabase? = null

        fun getInstance(context: Context): PlayItDatabase {
            return INSTANCE ?: synchronized(this) {
                lateinit var dbInstance: PlayItDatabase
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayItDatabase::class.java,
                    "playit_database"
                )
                    // TODO: Replace fallbackToDestructiveMigration with explicit Migration objects before GA release when schema changes
                    .fallbackToDestructiveMigration()
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                seedDatabase(dbInstance)
                            }
                        }
                    })
                    .build()
                dbInstance = instance
                INSTANCE = instance
                instance
            }
        }
    }
}

private suspend fun seedDatabase(db: PlayItDatabase) {
    // 1. Seed Letter Groups
    val groups = listOf(
        LetterGroupEntity("1", 1),
        LetterGroupEntity("2", 2),
        LetterGroupEntity("3", 3),
        LetterGroupEntity("4", 4),
        LetterGroupEntity("5", 5),
        LetterGroupEntity("6", 6),
        LetterGroupEntity("7", 7)
    )
    db.phonemeDao().insertLetterGroups(groups)

    // 2. Seed Phonemes
    val exampleWords = mapOf(
        "m" to "map", "s" to "sun", "a" to "apple", "i" to "ink",
        "o" to "octopus", "b" to "ball", "e" to "egg", "u" to "umbrella",
        "t" to "tent", "k" to "kite", "l" to "lamp", "y" to "yo-yo",
        "n" to "nest", "g" to "goat", "ng" to "ring", "p" to "pen",
        "r" to "rabbit", "d" to "dog", "h" to "hat", "w" to "window",
        "c" to "cat", "f" to "fish", "j" to "jar", "ñ" to "niño",
        "q" to "queen", "v" to "vase", "x" to "x-ray", "z" to "zebra"
    )

    val groupLetters = listOf(
        listOf("m", "s", "a", "i"),      // Group 1
        listOf("o", "b", "e", "u"),      // Group 2
        listOf("t", "k", "l", "y"),      // Group 3
        listOf("n", "g", "ng", "p"),     // Group 4
        listOf("r", "d", "h", "w"),      // Group 5
        listOf("c", "f", "j", "ñ"),      // Group 6
        listOf("q", "v", "x", "z")       // Group 7
    )

    val phonemes = mutableListOf<PhonemeEntity>()
    val members = mutableListOf<LetterGroupMemberEntity>()

    groupLetters.forEachIndexed { groupIdx, letters ->
        val groupId = (groupIdx + 1).toString()
        letters.forEachIndexed { pos, letter ->
            phonemes.add(
                PhonemeEntity(
                    phonemeId = letter,
                    letter = letter,
                    audioPath = "audio/$letter.mp3",
                    imagePath = "images/$letter.png",
                    exampleWord = exampleWords[letter] ?: ""
                )
            )
            members.add(
                LetterGroupMemberEntity(
                    groupId = groupId,
                    phonemeId = letter,
                    position = pos
                )
            )
        }
    }

    db.phonemeDao().insertPhonemes(phonemes)
    db.phonemeDao().insertLetterGroupMembers(members)

    // 3. Seed Blend It Words
    val blendWords = listOf(
        // Group 1 words (letters: m, s, a, i)
        BlendItWordEntity(groupId = "1", word = "am", wordPattern = "VC", audioPath = "audio/word_am.mp3", imagePath = "images/word_am.png"),
        BlendItWordEntity(groupId = "1", word = "as", wordPattern = "VC", audioPath = "audio/word_as.mp3", imagePath = "images/word_as.png"),
        BlendItWordEntity(groupId = "1", word = "is", wordPattern = "VC", audioPath = "audio/word_is.mp3", imagePath = "images/word_is.png"),
        BlendItWordEntity(groupId = "1", word = "ma", wordPattern = "CV", audioPath = "audio/word_ma.mp3", imagePath = "images/word_ma.png"),
        BlendItWordEntity(groupId = "1", word = "mass", wordPattern = "CVCC", audioPath = "audio/word_mass.mp3", imagePath = "images/word_mass.png"),
        BlendItWordEntity(groupId = "1", word = "sam", wordPattern = "CVC", audioPath = "audio/word_sam.mp3", imagePath = "images/word_sam.png"),
        BlendItWordEntity(groupId = "1", word = "sis", wordPattern = "CVC", audioPath = "audio/word_sis.mp3", imagePath = "images/word_sis.png"),

        // Group 2 words (letters: o, b, e, u + previously mastered)
        BlendItWordEntity(groupId = "2", word = "mom", wordPattern = "CVC", audioPath = "audio/word_mom.mp3", imagePath = "images/word_mom.png"),
        BlendItWordEntity(groupId = "2", word = "so", wordPattern = "CV", audioPath = "audio/word_so.mp3", imagePath = "images/word_so.png"),

        // Group 6 words (letters: c, f, j, ñ + previously mastered)
        BlendItWordEntity(groupId = "6", word = "mac", wordPattern = "CVC", audioPath = "audio/word_mac.mp3", imagePath = "images/word_mac.png")
    )
    db.blendItDao().insertBlendItWords(blendWords)
}