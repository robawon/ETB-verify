package com.example.presentation.screens.scanner

/**
 * Represents a single scanned banknote entry in the batch session.
 */
data class BatchEntry(
    val denomination: Int,
    val isGenuine: Boolean,
    val confidence: Float,
    val serial: String,
    val isRepeatedSerial: Boolean
)

/**
 * Summarizes statistics and aggregated values for an entire batch scanning session.
 */
data class BatchSummary(
    val total: Int,
    val genuineCount: Int,
    val counterfeitCount: Int,
    val genuineValue: Int,
    val counterfeitValue: Int,
    val repeatedSerialCount: Int
)

/**
 * Computes batch summary statistics from a list of batch entries, excluding repeated serials from counts and monetary totals.
 */
fun summarizeBatch(entries: List<BatchEntry>): BatchSummary {
    var total = 0
    var genuineCount = 0
    var counterfeitCount = 0
    var genuineValue = 0
    var counterfeitValue = 0
    var repeatedSerialCount = 0

    for (entry in entries) {
        total++
        if (entry.isRepeatedSerial) {
            repeatedSerialCount++
        } else {
            if (entry.isGenuine) {
                genuineCount++
                genuineValue += entry.denomination
            } else {
                counterfeitCount++
                counterfeitValue += entry.denomination
            }
        }
    }

    return BatchSummary(
        total = total,
        genuineCount = genuineCount,
        counterfeitCount = counterfeitCount,
        genuineValue = genuineValue,
        counterfeitValue = counterfeitValue,
        repeatedSerialCount = repeatedSerialCount
    )
}
