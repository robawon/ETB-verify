package com.example.domain.storage

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

/**
 * Saves a compressed JPEG copy of the scan bitmap to app-private storage.
 */
fun saveScanPhoto(context: Context, bitmap: Bitmap): String {
    return try {
        val photosDir = File(context.filesDir, "scan_photos")
        if (!photosDir.exists()) {
            photosDir.mkdirs()
        }

        val width = bitmap.width
        val height = bitmap.height
        val maxSide = 1024
        val scaledBitmap = if (width > maxSide || height > maxSide) {
            val ratio = width.toFloat() / height.toFloat()
            val newWidth: Int
            val newHeight: Int
            if (width > height) {
                newWidth = maxSide
                newHeight = (maxSide / ratio).toInt()
            } else {
                newHeight = maxSide
                newWidth = (maxSide * ratio).toInt()
            }
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } else {
            bitmap
        }

        val file = File(photosDir, "scan-${System.currentTimeMillis()}.jpg")
        val fos = FileOutputStream(file)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)
        fos.flush()
        fos.close()

        if (scaledBitmap !== bitmap) {
            scaledBitmap.recycle()
        }

        file.absolutePath
    } catch (_: Exception) {
        ""
    }
}

/**
 * Deletes the scan photo file at the specified path if it exists.
 */
fun deleteScanPhoto(path: String) {
    if (path.isBlank()) return
    try {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    } catch (_: Exception) {
        // Ignore deletion errors
    }
}
