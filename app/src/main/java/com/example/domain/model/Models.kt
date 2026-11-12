package com.example.domain.model

enum class Denomination(val value: Int, val label: String) {
    ETB_200(200, "ETB 200"),
    ETB_100(100, "ETB 100"),
    ETB_50(50, "ETB 50"),
    BATCH_ALL(0, "Batch Scan")
}

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

data class ClassificationResult(
    val label: String,
    val isGenuine: Boolean,
    val denomination: Int,
    val confidence: Float,
    val serialNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val defects: List<String> = emptyList(),
    val locationName: String = "Addis Ababa Central",
    val verifiedSecurityFeaturesCount: Int = 8,
    val rawLabel: String = label,
    val allProbabilities: Map<String, Float> = emptyMap(),
    val isUncertain: Boolean = false,
    val uncertainReason: String = ""
) {
    val formattedLabel: String
        get() = formatModelLabel(rawLabel.ifEmpty { label })
}

/**
 * Formats a raw model label into a user-friendly readable string.
 */
fun formatModelLabel(raw: String): String {
    return when (raw.lowercase().trim()) {
        "genuine_200_etb" -> "Genuine 200 ETB"
        "counterfeit_200_etb" -> "Counterfeit 200 ETB"
        "genuine_100_etb" -> "Genuine 100 ETB"
        "counterfeit_100_etb" -> "Counterfeit 100 ETB"
        else -> raw.split("_")
            .filter { it.isNotBlank() }
            .joinToString(" ") { word ->
                if (word.equals("etb", ignoreCase = true)) "ETB"
                else word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }
}

data class SecurityFeatureItem(
    val id: Int,
    val number: String,
    val titleAm: String,
    val titleEn: String,
    val badge: String,
    val descAm: String,
    val descEn: String,
    val actionTag: String
)

data class SpotCheckItem(
    val id: Int,
    val titleEn: String,
    val titleAm: String,
    val genuineEn: String,
    val genuineAm: String,
    val counterfeitEn: String,
    val counterfeitAm: String
)

data class HotspotZone(
    val id: String,
    val nameEn: String,
    val nameAm: String,
    val risk: RiskLevel,
    val reportsCount: Int,
    val batchDescription: String,
    val distanceKm: Double,
    val subCity: String,
    val timeAgo: String,
    val lat: Double,
    val lng: Double
)

data class SerialDuplicateAlert(
    val serialNumber: String,
    val denomination: Int,
    val occurrences: Int,
    val locations: List<String>,
    val firstSeen: Long,
    val isSyndicateRing: Boolean
)
