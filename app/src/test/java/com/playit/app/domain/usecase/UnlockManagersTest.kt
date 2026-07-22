package com.playit.app.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UnlockManagersTest {

    private val unlockManager = UnlockManager()
    private val groupUnlockManager = GroupUnlockManager()

    private val testSequence = listOf(
        "m", "s", "a", "i", "BLEND_1",
        "o", "b", "e", "u", "BLEND_2"
    )

    private val group1Letters = listOf("m", "s", "a", "i")

    // ─── UnlockManager Tests ─────────────────────────────────────────────────

    @Test
    fun testFirstNodeAlwaysUnlocked() {
        // Given an empty history of completed phonemes
        val completed = emptySet<String>()

        // Then "m" (the first node) must be unlocked
        assertTrue(unlockManager.isLetterUnlocked("m", testSequence, completed))
    }

    @Test
    fun testSecondNodeLockedIfFirstIncomplete() {
        val completed = emptySet<String>()

        // "s" must be locked since "m" is not complete
        assertFalse(unlockManager.isLetterUnlocked("s", testSequence, completed))
    }

    @Test
    fun testSecondNodeUnlockedIfFirstComplete() {
        val completed = setOf("m")

        // "s" unlocks when "m" is complete
        assertTrue(unlockManager.isLetterUnlocked("s", testSequence, completed))
    }

    @Test
    fun testThirdNodeLockedIfSecondIncomplete() {
        val completed = setOf("m")

        // "a" stays locked because "s" is incomplete
        assertFalse(unlockManager.isLetterUnlocked("a", testSequence, completed))
    }

    @Test
    fun testNodeUnlockedSkippingBlendPredecessor() {
        // "o" is located after "BLEND_1". It should unlock once its actual predecessor letter "i" is complete.
        val completed = setOf("m", "s", "a", "i")

        assertTrue(unlockManager.isLetterUnlocked("o", testSequence, completed))
    }

    @Test
    fun testNodeLockedSkippingBlendPredecessorIfIncomplete() {
        val completed = setOf("m", "s", "a") // "i" is missing

        assertFalse(unlockManager.isLetterUnlocked("o", testSequence, completed))
    }

    // ─── GroupUnlockManager Tests ────────────────────────────────────────────

    @Test
    fun testBlendLockedIfGroupPartiallyComplete() {
        // 3 of 4 completed
        val completed = setOf("m", "s", "a")

        assertFalse(groupUnlockManager.isBlendItUnlocked(group1Letters, completed))
    }

    @Test
    fun testBlendUnlockedIfGroupFullyComplete() {
        // All 4 completed
        val completed = setOf("m", "s", "a", "i")

        assertTrue(groupUnlockManager.isBlendItUnlocked(group1Letters, completed))
    }

    @Test
    fun testGroupUnlockedForGroupOneAlwaysTrue() {
        val completed = emptySet<String>()

        assertTrue(groupUnlockManager.isGroupUnlocked(1, group1Letters, completed))
    }

    @Test
    fun testGroupTwoLockedIfGroupOneIncomplete() {
        val completed = setOf("m", "s", "a") // missing "i"

        assertFalse(groupUnlockManager.isGroupUnlocked(2, group1Letters, completed))
    }

    @Test
    fun testGroupTwoUnlockedIfGroupOneFullyComplete() {
        val completed = setOf("m", "s", "a", "i")

        assertTrue(groupUnlockManager.isGroupUnlocked(2, group1Letters, completed))
    }
}
