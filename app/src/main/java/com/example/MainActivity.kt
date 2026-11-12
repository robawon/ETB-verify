package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.data.local.AppDatabase
import com.example.data.repository.IntelligenceRepository
import com.example.data.repository.ScanRepository
import com.example.domain.classifier.BanknoteClassifier
import com.example.domain.ocr.BanknoteSerialOcr
import com.example.presentation.localization.LocalAppLanguage
import com.example.presentation.localization.LocalizationManager
import com.example.presentation.navigation.AppNavHost
import com.example.ui.theme.ETBVerifyTheme

class MainActivity : ComponentActivity() {

    private lateinit var classifier: BanknoteClassifier
    private lateinit var serialOcr: BanknoteSerialOcr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val scanRepository = ScanRepository(database.scanDao(), database.incidentDao())
        val intelligenceRepository = IntelligenceRepository()
        classifier = BanknoteClassifier(applicationContext)
        serialOcr = BanknoteSerialOcr()

        setContent {
            val localizationManager = remember { LocalizationManager() }
            val navController = rememberNavController()

            CompositionLocalProvider(LocalAppLanguage provides localizationManager) {
                ETBVerifyTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavHost(
                            navController = navController,
                            scanRepo = scanRepository,
                            intelRepo = intelligenceRepository,
                            classifier = classifier,
                            serialOcr = serialOcr
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.close()
    }
}
