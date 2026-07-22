package com.playit.app

import android.app.Application
import com.playit.app.data.local.PlayItDatabase
import com.playit.app.data.preferences.SessionManager
import com.playit.app.data.repository.PlayItRepositoryImpl

class PlayItApplication : Application() {
    val database by lazy { PlayItDatabase.getInstance(this) }
    val repository by lazy { PlayItRepositoryImpl(database) }

    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}