package com.playit.app.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ==========================================
// 1. DATABASE ENTITIES
// ==========================================

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "phoneme_id")
    val phonemeId: String, // e.g. "m", "s", "a"

    @ColumnInfo(name = "stars")
    val stars: Int = 0, // 0 to 3 rating scale

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "unlocked")
    val unlocked: Boolean = false
)

@Entity(
    tableName = "attempt_log",
    foreignKeys = [
        ForeignKey(
            entity = LessonProgressEntity::class,
            parentColumns = ["phoneme_id"],
            childColumns = ["phoneme_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["phoneme_id"]),
        Index(value = ["timestamp"])
    ]
)
data class AttemptLogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "attempt_id")
    val attemptId: Long = 0,

    @ColumnInfo(name = "phoneme_id")
    val phonemeId: String,

    @ColumnInfo(name = "stage")
    val stage: String, // "SAY_IT" or "FIND_IT"

    @ColumnInfo(name = "success")
    val success: Boolean,

    @ColumnInfo(name = "hearts_lost")
    val heartsLost: Int,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)

// ==========================================
// 2. DATA ACCESS OBJECT (DAO)
// ==========================================

@Dao
interface ProgressDao {

    @Query("SELECT * FROM lesson_progress ORDER BY phoneme_id ASC")
    fun getAllProgress(): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: LessonProgressEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAttemptLog(attempt: AttemptLogEntity)

    @Query("SELECT COUNT(*) FROM lesson_progress WHERE is_completed = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(stars), 0) FROM lesson_progress")
    fun getTotalStarsEarned(): Flow<Int>

    @Query("SELECT * FROM attempt_log WHERE phoneme_id = :phonemeId ORDER BY timestamp DESC")
    fun getAttemptsForPhoneme(phonemeId: String): Flow<List<AttemptLogEntity>>
}

// ==========================================
// 3. DATABASE INITIALIZER & SEEDER
// ==========================================

@Database(
    entities = [LessonProgressEntity::class, AttemptLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlayITDatabase : RoomDatabase() {

    abstract fun progressDao(): ProgressDao

    companion object {
        private const val DATABASE_NAME = "playit_database.db"
        private const val PREPOPULATED_ASSET_PATH = "database/playit_prepopulated.db"

        @Volatile
        private var INSTANCE: PlayITDatabase? = null

        fun getInstance(context: Context): PlayITDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PlayITDatabase::class.java,
                    DATABASE_NAME
                )
                    // Seeding step: Room pre-loads custom lesson records, unlocks,
                    // and simulated telemetry histories out of your assets folder!
                    .createFromAsset(PREPOPULATED_ASSET_PATH)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}