package com.playit.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.playit.app.data.local.dao.PlayItDao
import com.playit.app.data.local.entity.AppConfig
import com.playit.app.data.local.entity.LessonProgress
import com.playit.app.data.local.entity.Phoneme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [AppConfig::class, Phoneme::class, LessonProgress::class],
    version = 1,
    exportSchema = false
)
abstract class PlayItDatabase : RoomDatabase() {

    abstract fun playItDao(): PlayItDao

    companion object {
        @Volatile
        private var INSTANCE: PlayItDatabase? = null

        fun getInstance(context: Context): PlayItDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayItDatabase::class.java,
                    "playit_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = database.playItDao()
                    // Seed default AppConfig
                    dao.insertAppConfig(AppConfig(activeChildName = "Player 1", currentStreak = 0))

                    // Seed MVP Marungko Sequence
                    val seedPhonemes = listOf(
                        Phoneme("m", "M", 0),
                        Phoneme("a", "A", 1),
                        Phoneme("s", "S", 2),
                        Phoneme("i", "I", 3),
                        Phoneme("o", "O", 4)
                    )
                    dao.insertPhonemes(seedPhonemes)
                }
            }
        }
    }
}