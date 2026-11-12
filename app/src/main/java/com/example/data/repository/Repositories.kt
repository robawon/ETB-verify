package com.example.data.repository

import com.example.data.local.IncidentReportEntity
import com.example.data.local.ScanDao
import com.example.data.local.IncidentDao
import com.example.data.local.ScanRecordEntity
import com.example.domain.model.HotspotZone
import com.example.domain.model.RiskLevel
import com.example.domain.model.SerialDuplicateAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScanRepository(
    private val scanDao: ScanDao,
    private val incidentDao: IncidentDao
) {
    val allScans: Flow<List<ScanRecordEntity>> = scanDao.getAllScans()
    val totalCount: Flow<Int> = scanDao.getTotalCount()
    val genuineCount: Flow<Int> = scanDao.getGenuineCount()
    val counterfeitCount: Flow<Int> = scanDao.getCounterfeitCount()
    val allIncidents: Flow<List<IncidentReportEntity>> = incidentDao.getAllIncidents()

    fun searchScans(query: String): Flow<List<ScanRecordEntity>> = scanDao.searchScans(query)

    suspend fun getScanById(id: Long): ScanRecordEntity? = scanDao.getScanById(id)

    suspend fun saveScan(record: ScanRecordEntity): Long = scanDao.insertScan(record)

    suspend fun deleteScan(id: Long) = scanDao.deleteScanById(id)

    suspend fun reportIncident(incident: IncidentReportEntity): Long = incidentDao.insertIncident(incident)

    suspend fun checkSerialRepetition(serial: String): List<ScanRecordEntity> {
        if (serial.isBlank()) return emptyList()
        return scanDao.getScansWithSerial(serial)
    }
}

class IntelligenceRepository {
    fun getHotspots(): List<HotspotZone> = listOf(
        HotspotZone(
            id = "hz_merkato",
            nameEn = "Merkato Vegetable Market, Shema Tera",
            nameAm = "መርካቶ የአታክልት ተራ፣ ሸማ ተራ",
            risk = RiskLevel.HIGH,
            reportsCount = 8,
            batchDescription = "200 ETB Fake Batch • Identical serial BD1029384",
            distanceKm = 1.8,
            subCity = "Addis Ketema",
            timeAgo = "42 min ago",
            lat = 9.0345,
            lng = 38.7360
        ),
        HotspotZone(
            id = "hz_mexico",
            nameEn = "Mexico Square Taxi Terminal",
            nameAm = "ሜክሲኮ ታክሲ ተርሚናል",
            risk = RiskLevel.MEDIUM,
            reportsCount = 3,
            batchDescription = "Photocopied paper texture • 100 ETB notes",
            distanceKm = 3.2,
            subCity = "Kirkos",
            timeAgo = "3 hours ago",
            lat = 9.0112,
            lng = 38.7455
        ),
        HotspotZone(
            id = "hz_bole",
            nameEn = "Bole Medhanealem Commercial District",
            nameAm = "ቦሌ መድኃኔዓለም የገበያ ማዕከል",
            risk = RiskLevel.LOW,
            reportsCount = 2,
            batchDescription = "Poor UV print response on denomination watermark",
            distanceKm = 4.9,
            subCity = "Bole",
            timeAgo = "6 hours ago",
            lat = 8.9950,
            lng = 38.7880
        ),
        HotspotZone(
            id = "hz_piassa",
            nameEn = "Piassa Gold & Jewelry Quarter",
            nameAm = "ፒያሳ ወርቅ ሰፈር",
            risk = RiskLevel.LOW,
            reportsCount = 0,
            batchDescription = "14 verified genuine scans recorded in last 24h",
            distanceKm = 2.1,
            subCity = "Arada",
            timeAgo = "Active Clean Zone",
            lat = 9.0390,
            lng = 38.7520
        )
    )

    fun getSerialDuplicateAlerts(): List<SerialDuplicateAlert> = listOf(
        SerialDuplicateAlert(
            serialNumber = "BD1029384",
            denomination = 200,
            occurrences = 4,
            locations = listOf("Merkato Shema Tera", "Merkato Military Tera", "Autobis Tera"),
            firstSeen = System.currentTimeMillis() - 86400_000L,
            isSyndicateRing = true
        ),
        SerialDuplicateAlert(
            serialNumber = "FX2839057",
            denomination = 100,
            occurrences = 2,
            locations = listOf("Mexico Square Taxi Terminal", "Lagahar Station"),
            firstSeen = System.currentTimeMillis() - 3 * 3600_000L,
            isSyndicateRing = false
        )
    )
}
