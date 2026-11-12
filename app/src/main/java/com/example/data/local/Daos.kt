package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE id = :id")
    suspend fun getScanById(id: Long): ScanRecordEntity?

    @Query("SELECT * FROM scan_records WHERE serialNumber LIKE '%' || :query || '%' OR locationName LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchScans(query: String): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE isGenuine = 0 ORDER BY timestamp DESC")
    fun getCounterfeitScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE serialNumber = :serialNumber")
    suspend fun getScansWithSerial(serialNumber: String): List<ScanRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(record: ScanRecordEntity): Long

    @Query("DELETE FROM scan_records WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("SELECT COUNT(*) FROM scan_records")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE isGenuine = 1")
    fun getGenuineCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE isGenuine = 0")
    fun getCounterfeitCount(): Flow<Int>
}

@Dao
interface IncidentDao {
    @Query("SELECT * FROM incident_reports ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentReportEntity): Long
}
