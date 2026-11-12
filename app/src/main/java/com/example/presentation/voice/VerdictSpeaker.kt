package com.example.presentation.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.domain.model.ClassificationResult
import java.util.Locale

/**
 * Data class holding spoken text verdicts in English and Amharic.
 */
data class SpokenVerdict(
    val en: String,
    val am: String
)

/**
 * Builds a spoken verdict based on the classification result's denomination, genuineness, and confidence.
 */
fun buildSpokenVerdict(result: ClassificationResult): SpokenVerdict {
    val d = result.denomination
    val pct = (result.confidence * 100).toInt()
    val en = if (result.isGenuine) {
        "This $d birr note looks genuine. I am $pct percent sure."
    } else {
        "Warning. This $d birr note looks counterfeit. Do not accept it. I am $pct percent sure."
    }
    val am = if (result.isGenuine) {
        "ይህ የ$d ብር ኖት ትክክለኛ ይመስላል። በ $pct በመቶ እርግጠኛ ነኝ።"
    } else {
        "ማስጠንቀቂያ። ይህ የ$d ብር ኖት የተጭበረበረ ይመስላል። አይቀበሉት። በ $pct በመቶ እርግጠኛ ነኝ።"
    }
    return SpokenVerdict(en = en, am = am)
}

/**
 * Manages TextToSpeech initialization, availability checking, and speaking audit verdicts.
 */
class VerdictSpeaker(
    context: Context,
    private val onAmharicMissing: () -> Unit
) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val pendingUtterances = mutableListOf<Pair<SpokenVerdict, Boolean>>()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                val queued = pendingUtterances.toList()
                pendingUtterances.clear()
                for ((verdict, preferAmharic) in queued) {
                    speak(verdict, preferAmharic)
                }
            } else {
                Log.e("VerdictSpeaker", "TTS initialization failed with status: $status")
            }
        }
    }

    /**
     * Speaks the given verdict, preferring Amharic if available, or falling back to English.
     */
    fun speak(verdict: SpokenVerdict, preferAmharic: Boolean) {
        val engine = tts
        if (!isInitialized || engine == null) {
            pendingUtterances.add(verdict to preferAmharic)
            return
        }

        if (preferAmharic) {
            val amharicLocale = Locale("am", "ET")
            val availability = engine.isLanguageAvailable(amharicLocale)
            val isAvailable = availability >= TextToSpeech.LANG_AVAILABLE
            if (isAvailable) {
                engine.language = amharicLocale
                engine.speak(verdict.am, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                onAmharicMissing()
                engine.language = Locale.US
                engine.speak(verdict.en, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        } else {
            engine.language = Locale.US
            engine.speak(verdict.en, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    /**
     * Stops any ongoing text-to-speech utterance.
     */
    fun stop() {
        tts?.stop()
    }

    /**
     * Shuts down and releases the text-to-speech engine resources.
     */
    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("VerdictSpeaker", "Error shutting down TTS", e)
        }
        tts = null
        isInitialized = false
    }
}
