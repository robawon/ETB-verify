package com.example.domain.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.regex.Pattern
import kotlin.coroutines.resume

class BanknoteSerialOcr {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val serialPattern = Pattern.compile("(?i)\\b([A-Z]{1,2})\\s?(\\d{7})\\b")

    /**
     * Extracts and formats the serial number from the bitmap using OCR, returning empty string on failure or absence.
     */
    suspend fun extractSerialNumber(bitmap: Bitmap): String = suspendCancellableCoroutine { cont ->
        try {
            val image = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val fullText = visionText.text
                    val matcher = serialPattern.matcher(fullText)
                    if (matcher.find()) {
                        val prefix = matcher.group(1)?.uppercase() ?: ""
                        val digits = matcher.group(2) ?: ""
                        if (prefix.isNotBlank() && digits.isNotBlank()) {
                            cont.resume("$prefix $digits")
                        } else {
                            cont.resume("")
                        }
                    } else {
                        cont.resume("")
                    }
                }
                .addOnFailureListener {
                    cont.resume("")
                }
        } catch (e: Exception) {
            cont.resume("")
        }
    }
}
