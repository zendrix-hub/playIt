package com.playit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_config")
data class AppConfig(
    @PrimaryKey val id: Int = 1,
    val activeChildName: String,
    val currentStreak: Int
)

@Entity(tableName = "phoneme")
data class Phoneme(
    @PrimaryKey val phonemeId: String,
    val letter: String,
    val sequenceOrder: Int
)

@Entity(tableName = "lesson_progress")
data class LessonProgress(
    @PrimaryKey val phonemeId: String,
    val starsEarned: Int,
    val isCompleted: Boolean
)