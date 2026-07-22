package com.playit.app.domain.usecase

/**
 * Domain usecase to calculate total stars accumulated by a student.
 */
class StarCalculator {

    fun calculateTotalStars(starsPerCompletedLesson: List<Int>): Int {
        return starsPerCompletedLesson.sum()
    }
}
