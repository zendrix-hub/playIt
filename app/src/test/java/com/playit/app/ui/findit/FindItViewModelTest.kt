package com.playit.app.ui.findit

import android.app.Application
import com.playit.app.domain.model.*
import com.playit.app.domain.repository.PlayItRepository
import com.playit.app.util.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FindItViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakePlayItRepository
    private lateinit var viewModel: FindItViewModel
    private val targetPhonemeId = "m"

    @Before
    fun setUp() {
        repository = FakePlayItRepository()
        viewModel = FindItViewModel(
            application = Application(),
            repository = repository,
            phonemeId = targetPhonemeId
        )
    }

    @Test
    fun testCorrectCardTapCompletesLessonAndGrantsStars() {
        val targetItem = FindItGridItem(id = "m", word = "m")
        viewModel.onCardTapped(targetItem)

        val state = viewModel.uiState.value
        assertTrue(state.isComplete)
        assertEquals(3, state.starsEarned)
        assertEquals(true, state.tapResults["m"])
        assertEquals(1, repository.updatedProgressList.size)
        assertEquals(true, repository.updatedProgressList.first().isCompleted)
        assertEquals(3, repository.updatedProgressList.first().starsEarned)
        assertEquals(1, repository.findItAttempts.size)
        assertTrue(repository.findItAttempts.first().isCorrect)
    }

    @Test
    fun testHeartsDepletionResetsToThreeOnZero() {
        val wrongItem = FindItGridItem(id = "s", word = "s")

        // Default hearts is 5. Tap wrong 4 times -> hearts decrease to 1
        repeat(4) {
            viewModel.onCardTapped(wrongItem)
        }
        assertEquals(1, viewModel.uiState.value.hearts)

        // 5th wrong tap -> newHearts = 0 <= 0, resets hearts to 3 per business rule
        viewModel.onCardTapped(wrongItem)

        val state = viewModel.uiState.value
        assertFalse(state.isComplete)
        assertEquals(3, state.hearts)
        assertEquals(5, repository.findItAttempts.size)
    }
}

private class FakePlayItRepository : PlayItRepository {
    val updatedProgressList = mutableListOf<LessonProgress>()
    val findItAttempts = mutableListOf<FindItAttempt>()
    var unlockedPhonemes = listOf(
        PhonemeWithStatus(
            phonemeId = "m",
            letter = "m",
            sequenceOrder = 1,
            isUnlocked = true,
            isCompleted = false,
            starsEarned = 0
        )
    )

    override fun getAllProfiles(): Flow<List<Profile>> = flowOf(emptyList())
    override suspend fun getProfileById(profileId: Long): Profile? = null
    override suspend fun insertProfile(profile: Profile): Long = 1L
    override suspend fun updateProfile(profile: Profile) {}
    override suspend fun deleteProfile(profileId: Long) {}

    override fun getAllPhonemes(): Flow<List<Phoneme>> = flowOf(emptyList())
    override suspend fun getPhonemeById(phonemeId: String): Phoneme? = null
    override suspend fun getLessonProgress(profileId: Long, phonemeId: String): LessonProgress? = null
    override fun getCompletedLessonsForProfile(profileId: Long): Flow<List<LessonProgress>> = flowOf(emptyList())
    override suspend fun updateLessonProgress(progress: LessonProgress) {
        updatedProgressList.add(progress)
    }
    override fun getUnlockedPhonemes(profileId: Long): Flow<List<PhonemeWithStatus>> = flowOf(unlockedPhonemes)

    override suspend fun insertSayItAttempt(attempt: SayItAttempt) {}
    override suspend fun insertFindItAttempt(attempt: FindItAttempt) {
        findItAttempts.add(attempt)
    }
    override fun getFindItAttempts(profileId: Long): Flow<List<FindItAttempt>> = flowOf(findItAttempts)
    override fun getSayItAttempts(profileId: Long): Flow<List<SayItAttempt>> = flowOf(emptyList())

    override fun getLetterGroups(): Flow<List<LetterGroup>> = flowOf(emptyList())
    override fun getLetterGroupMembers(groupId: String): Flow<List<LetterGroupMember>> = flowOf(emptyList())

    override fun getBlendItWords(groupId: String): Flow<List<BlendItWord>> = flowOf(emptyList())
    override suspend fun getBlendItProgress(profileId: Long, groupId: String): BlendItProgress? = null
    override suspend fun updateBlendItProgress(progress: BlendItProgress) {}
    override suspend fun insertBlendItAttempt(attempt: BlendItAttempt) {}
    override fun getBlendItAttempts(profileId: Long): Flow<List<BlendItAttempt>> = flowOf(emptyList())

    override fun getAchievements(profileId: Long): Flow<List<Achievement>> = flowOf(emptyList())
    override suspend fun updateAchievement(achievement: Achievement) {}

    override suspend fun insertReportLog(report: ReportLog) {}
    override fun getReportLogs(profileId: Long): Flow<List<ReportLog>> = flowOf(emptyList())
}
