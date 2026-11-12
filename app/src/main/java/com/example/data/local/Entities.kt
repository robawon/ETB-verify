package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val denomination: Int,
    val isGenuine: Boolean,
    val confidence: Float,
    val serialNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double = 9.0320,
    val longitude: Double = 38.7420,
    val locationName: String = "Addis Ababa Central",
    val defectsSummary: String = "",
    val notes: String = "",
    val imageUri: String = ""
)

@Entity(tableName = "incident_reports")
data class IncidentReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val denomination: Int,
    val serialNumber: String,
    val riskLevel: String,
    val subCity: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String,
    val isAnonymous: Boolean,
    val isDispatched: Boolean = false
)
