package com.playit.app

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.playit.app.data.local.PlayItDatabase
import com.playit.app.data.local.entity.BlendItProgressEntity
import com.playit.app.data.local.entity.BlendItWordEntity
import com.playit.app.data.local.entity.LetterGroupEntity
import com.playit.app.data.local.entity.LetterGroupMemberEntity
import com.playit.app.data.local.entity.PhonemeEntity
import com.playit.app.data.local.entity.ProfileEntity
import com.playit.app.data.repository.PlayItRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Task UI-10.06 — Instrumented Room DAO & Repository Integration Tests
 *
 * Covers:
 * 1. PhonemeDao.getUnlockedPhonemes sequence order & FK mapping.
 * 2. BlendItProgress upsert FK constraint verification (regression guards ENG-1.01 / BUG-06).
 * 3. BlendItWordEntity unique constraint on (groupId, word) (regression guards ENG-2.08 / IMPROVEMENT-01).
 * 4. PlayItRepositoryImpl profile integration end-to-end flow.
 */
@RunWith(AndroidJUnit4::class)
class RoomDaoInstrumentedTest {

    private lateinit var db: PlayItDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, PlayItDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetUnlockedPhonemesReturnsCorrectSequenceAndStatus() = runBlocking {
        // 1. Seed letter group and phonemes
        db.phonemeDao().insertLetterGroups(listOf(LetterGroupEntity("1", 1)))
        db.phonemeDao().insertPhonemes(listOf(
            PhonemeEntity("m", "M", "map", "audio_m"),
            PhonemeEntity("s", "S", "sun", "audio_s")
        ))
        db.phonemeDao().insertLetterGroupMembers(listOf(
            LetterGroupMemberEntity("1", "m", 1),
            LetterGroupMemberEntity("1", "s", 2)
        ))

        // 2. Create profile
        val profileId = db.profileDao().insertProfile(ProfileEntity(name = "TestChild", avatarId = "avatar1"))

        // 3. Query getUnlockedPhonemes
        val phonemes = db.phonemeDao().getUnlockedPhonemes(profileId).first()

        assertEquals(2, phonemes.size)
        assertEquals("m", phonemes[0].phonemeId)
        assertEquals(11, phonemes[0].sequenceOrder)
        assertEquals("s", phonemes[1].phonemeId)
        assertEquals(12, phonemes[1].sequenceOrder)
    }

    @Test
    fun testBlendItProgressUpsertValidGroupIdSucceeds() = runBlocking {
        // Seed parent group for Foreign Key validity (group "1")
        db.phonemeDao().insertLetterGroups(listOf(LetterGroupEntity("1", 1)))
        val profileId = db.profileDao().insertProfile(ProfileEntity(name = "TestChild", avatarId = "avatar1"))

        val progress = BlendItProgressEntity(
            profileId = profileId,
            groupId = "1", // Correct stripped groupId format matching letter_groups.groupId
            isCompleted = true,
            starsEarned = 3,
            wordsCorrect = 3,
            heartsUsed = 0,
            completedAt = System.currentTimeMillis()
        )

        db.blendItDao().upsertBlendItProgress(progress)

        val retrieved = db.blendItDao().getBlendItProgress(profileId, "1")
        assertNotNull(retrieved)
        assertTrue(retrieved!!.isCompleted)
        assertEquals(3, retrieved.starsEarned)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testBlendItProgressUpsertInvalidGroupIdFailsFKConstraint() {
        runBlocking {
            val profileId = db.profileDao().insertProfile(ProfileEntity(name = "TestChild", avatarId = "avatar1"))

            // Passing raw unstripped "BLEND_1" instead of "1" violates FK constraint against letter_groups
            val invalidProgress = BlendItProgressEntity(
                profileId = profileId,
                groupId = "BLEND_1", // Invalid FK (does not exist in letter_groups table)
                isCompleted = true,
                starsEarned = 3,
                wordsCorrect = 3,
                heartsUsed = 0,
                completedAt = System.currentTimeMillis()
            )

            db.blendItDao().upsertBlendItProgress(invalidProgress)
        }
    }

    @Test
    fun testBlendItWordEntityUniqueConstraint() = runBlocking {
        db.phonemeDao().insertLetterGroups(listOf(LetterGroupEntity("1", 1)))

        val word1 = BlendItWordEntity("1", "1", "bao", "audio_bao", "img_bao")
        val wordDuplicate = BlendItWordEntity("2", "1", "bao", "audio_bao", "img_bao") // Same (groupId, word)

        db.blendItDao().insertBlendItWords(listOf(word1))

        // OnConflictStrategy.REPLACE replaces the existing row, maintaining uniqueness of (groupId, word)
        db.blendItDao().insertBlendItWords(listOf(wordDuplicate))

        val words = db.blendItDao().getBlendItWords("1").first()
        assertEquals(1, words.size)
        assertEquals("bao", words[0].word)
    }

    @Test
    fun testPlayItRepositoryImplProfileFlow() = runBlocking {
        val repository = PlayItRepositoryImpl(db)

        val newProfileId = repository.createProfile("Anya", "avatar2")
        assertTrue(newProfileId > 0)

        val profiles = repository.getAllProfiles().first()
        assertEquals(1, profiles.size)
        assertEquals("Anya", profiles[0].name)
    }
}
