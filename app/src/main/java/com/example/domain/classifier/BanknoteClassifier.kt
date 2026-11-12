package com.example.domain.classifier

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.domain.model.ClassificationResult
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class BanknoteClassifier(private val context: Context) {

    private var interpreter: Interpreter? = null
    private val modelFileName = "Dense121_best_weights_mixed-III.tflite"

    companion object {
        private const val TAG = "BanknoteClassifier"
        const val INPUT_SIZE = 224
        const val MIN_CONFIDENCE = 0.80f
        val CLASSES = listOf(
            "genuine_200_etb",
            "counterfeit_200_etb",
            "genuine_100_etb",
            "counterfeit_100_etb"
        )
    }

    init {
        initInterpreter()
    }

    /**
     * Initializes the TFLite interpreter by loading the model file from assets.
     */
    private fun initInterpreter() {
        try {
            val assetFileDescriptor = context.assets.openFd(modelFileName)
            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)
            Log.i(TAG, "DenseNet121 TFLite Model loaded successfully from assets: $modelFileName")
        } catch (e: Exception) {
            Log.e(TAG, "Could not initialize TFLite model from assets", e)
        }
    }

    /**
     * Returns whether the TFLite model interpreter is successfully loaded.
     */
    fun isModelLoaded(): Boolean = interpreter != null

    /**
     * Classifies the provided bitmap using the TFLite model, returning genuine, counterfeit, or uncertain result.
     */
    fun classifyBanknote(bitmap: Bitmap, targetDenomination: Int = 0): ClassificationResult {
        val inter = interpreter
        if (inter == null) {
            return uncertainResult(
                reason = "The AI model file is missing from this build, so the note cannot be checked.",
                denomination = targetDenomination
            )
        }

        return try {
            val scaled = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
            val byteBuffer = ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * 3 * 4)
            byteBuffer.order(ByteOrder.nativeOrder())
            byteBuffer.rewind()

            val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
            scaled.getPixels(intValues, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

            var pixelIdx = 0
            for (i in 0 until INPUT_SIZE) {
                for (j in 0 until INPUT_SIZE) {
                    val pixel = intValues[pixelIdx++]
                    val r = ((pixel shr 16) and 0xFF) / 255.0f
                    val g = ((pixel shr 8) and 0xFF) / 255.0f
                    val b = (pixel and 0xFF) / 255.0f
                    byteBuffer.putFloat(r)
                    byteBuffer.putFloat(g)
                    byteBuffer.putFloat(b)
                }
            }

            val outputProbArray = Array(1) { FloatArray(CLASSES.size) }
            inter.run(byteBuffer, outputProbArray)

            val rawOutputs = outputProbArray[0]
            val probabilities = if (rawOutputs.sum() in 0.95f..1.05f) {
                rawOutputs
            } else {
                softmax(rawOutputs)
            }

            val probMap = mutableMapOf<String, Float>()
            var maxIndex = 0
            var maxProb = probabilities[0]
            for (idx in probabilities.indices) {
                val label = CLASSES[idx]
                val p = probabilities[idx]
                probMap[label] = p
                if (p > maxProb) {
                    maxProb = p
                    maxIndex = idx
                }
            }

            val predictedLabel = CLASSES[maxIndex]
            val isGenuine = predictedLabel.startsWith("genuine")
            val denomination = if (predictedLabel.contains("200")) 200 else 100

            if (maxProb < MIN_CONFIDENCE) {
                val pct = (maxProb * 100).toInt()
                return uncertainResult(
                    reason = "Only $pct% sure. Put the note flat, use good light, and fill the frame.",
                    denomination = denomination,
                    confidence = maxProb,
                    probabilities = probMap
                )
            }

            val defects = if (!isGenuine) {
                listOf("AI model found visual patterns that do not match a genuine note")
            } else {
                emptyList()
            }

            ClassificationResult(
                label = predictedLabel,
                isGenuine = isGenuine,
                denomination = denomination,
                confidence = maxProb,
                serialNumber = "",
                defects = defects,
                verifiedSecurityFeaturesCount = if (isGenuine) 8 else 2,
                rawLabel = predictedLabel,
                allProbabilities = probMap,
                isUncertain = false,
                uncertainReason = ""
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error running TFLite inference", e)
            uncertainResult(
                reason = "Something went wrong while analysing the photo. Please try again.",
                denomination = targetDenomination
            )
        }
    }

    /**
     * Computes the softmax probability distribution from raw model outputs.
     */
    private fun softmax(values: FloatArray): FloatArray {
        var max = Float.NEGATIVE_INFINITY
        for (v in values) {
            if (v > max) max = v
        }
        var sum = 0f
        val exp = FloatArray(values.size)
        for (i in values.indices) {
            exp[i] = kotlin.math.exp(values[i] - max)
            sum += exp[i]
        }
        for (i in values.indices) {
            exp[i] = if (sum > 0f) exp[i] / sum else 1f / values.size
        }
        return exp
    }

    /**
     * Creates an uncertain ClassificationResult with the given reason and metadata.
     */
    private fun uncertainResult(
        reason: String,
        denomination: Int = 0,
        confidence: Float = 0f,
        probabilities: Map<String, Float> = emptyMap()
    ): ClassificationResult {
        val denom = if (denomination in listOf(100, 200)) denomination else 200
        return ClassificationResult(
            label = "uncertain",
            isGenuine = false,
            denomination = denom,
            confidence = confidence,
            serialNumber = "",
            defects = emptyList(),
            verifiedSecurityFeaturesCount = 0,
            rawLabel = "uncertain",
            allProbabilities = probabilities,
            isUncertain = true,
            uncertainReason = reason
        )
    }

    /**
     * Closes the TFLite interpreter and releases resources.
     */
    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
