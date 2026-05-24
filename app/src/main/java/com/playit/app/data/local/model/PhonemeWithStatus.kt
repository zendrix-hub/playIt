package com.playit.app.data.local.model

data class PhonemeWithStatus(
    val phonemeId: String,
    val letter: String,
    val sequenceOrder: Int,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val starsEarned: Int
)