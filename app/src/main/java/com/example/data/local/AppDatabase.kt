package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ScanRecordEntity::class, IncidentReportEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
    abstract fun incidentDao(): IncidentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "counterwatch.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            seedInitialData(getInstance(context))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedInitialData(database: AppDatabase) {
            val now = System.currentTimeMillis()
            val hour = 3600_000L

            // Seed initial incident reports for the map & alerts (History remains empty of fake scans)
            database.incidentDao().insertIncident(
                IncidentReportEntity(
                    denomination = 200,
                    serialNumber = "BD1029384",
                    riskLevel = "CRITICAL",
                    subCity = "Merkato Zone",
                    latitude = 9.0345,
                    longitude = 38.7360,
                    timestamp = now - 42 * 60_000,
                    notes = "3 forged notes with identical serial BD1029384. Missing color-shift thread and magnetic ink response.",
                    isAnonymous = true,
                    isDispatched = true
                )
            )
            database.incidentDao().insertIncident(
                IncidentReportEntity(
                    denomination = 100,
                    serialNumber = "FX2839057",
                    riskLevel = "MEDIUM",
                    subCity = "Mexico Square",
                    latitude = 9.0112,
                    longitude = 38.7455,
                    timestamp = now - 3 * hour,
                    notes = "Photocopied paper texture detected by local gas station merchant scanner. Watermark lacks micro-emboss profile.",
                    isAnonymous = false,
                    isDispatched = false
                )
            )
            database.incidentDao().insertIncident(
                IncidentReportEntity(
                    denomination = 200,
                    serialNumber = "AA9102441",
                    riskLevel = "MEDIUM",
                    subCity = "Bole Airport Road",
                    latitude = 8.9950,
                    longitude = 38.7880,
                    timestamp = now - 6 * hour,
                    notes = "Poor UV print response on denomination watermark.",
                    isAnonymous = true,
                    isDispatched = false
                )
            )
        }
    }
}
