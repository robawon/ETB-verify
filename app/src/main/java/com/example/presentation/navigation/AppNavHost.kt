package com.example.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.data.local.ScanRecordEntity
import com.example.data.repository.IntelligenceRepository
import com.example.data.repository.ScanRepository
import com.example.domain.classifier.BanknoteClassifier
import com.example.domain.model.ClassificationResult
import com.example.domain.ocr.BanknoteSerialOcr
import com.example.presentation.screens.alerts.AlertsScreen
import com.example.presentation.screens.analytics.IntelligenceScreen
import com.example.presentation.screens.history.ScanHistoryScreen
import com.example.presentation.screens.home.HomeScreen
import com.example.presentation.screens.manual.ManualGuideScreen
import com.example.presentation.screens.map.FraudMapScreen
import com.example.presentation.screens.report.IncidentReportScreen
import com.example.presentation.screens.result.InspectionResultScreen
import com.example.presentation.screens.scanner.ScannerScreen
import com.example.presentation.screens.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController,
    scanRepo: ScanRepository,
    intelRepo: IntelligenceRepository,
    classifier: BanknoteClassifier,
    serialOcr: BanknoteSerialOcr
) {
    val coroutineScope = rememberCoroutineScope()
    val scans by scanRepo.allScans.collectAsState(initial = emptyList())
    val totalCount by scanRepo.totalCount.collectAsState(initial = 34)
    val genuineCount by scanRepo.genuineCount.collectAsState(initial = 32)
    val counterfeitCount by scanRepo.counterfeitCount.collectAsState(initial = 2)

    var currentInspectionResult by remember {
        mutableStateOf<ClassificationResult?>(null)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onLaunchScanner = { navController.navigate(Routes.SCANNER) },
                onEnterDashboard = { navController.navigate(Routes.HOME) }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                scans = scans,
                totalCount = if (totalCount == 0) scans.size else totalCount,
                genuineCount = if (genuineCount == 0) scans.count { it.isGenuine } else genuineCount,
                counterfeitCount = if (counterfeitCount == 0) scans.count { !it.isGenuine } else counterfeitCount,
                onNavigateToScan = { navController.navigate(Routes.SCANNER) },
                onNavigateToMap = { navController.navigate(Routes.FRAUD_MAP) },
                onNavigateToReport = { navController.navigate(Routes.REPORT) },
                onNavigateToGuide = { navController.navigate(Routes.MANUAL_GUIDE) },
                onNavigateToAlerts = { navController.navigate(Routes.ALERTS) },
                onNavigateToIntel = { navController.navigate(Routes.INTELLIGENCE) },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                onScanDetailClick = { scan ->
                    currentInspectionResult = ClassificationResult(
                        label = if (scan.isGenuine) "genuine_${scan.denomination}_etb" else "counterfeit_${scan.denomination}_etb",
                        isGenuine = scan.isGenuine,
                        denomination = scan.denomination,
                        confidence = scan.confidence,
                        serialNumber = scan.serialNumber,
                        timestamp = scan.timestamp,
                        defects = if (scan.defectsSummary.isNotBlank()) listOf(scan.defectsSummary) else emptyList()
                    )
                    navController.navigate(Routes.RESULT)
                }
            )
        }

        composable(Routes.SCANNER) {
            ScannerScreen(
                classifier = classifier,
                serialOcr = serialOcr,
                onBackClick = { navController.popBackStack() },
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                        "fraud_map" -> navController.navigate(Routes.FRAUD_MAP)
                        "alerts" -> navController.navigate(Routes.ALERTS)
                        "intelligence" -> navController.navigate(Routes.INTELLIGENCE)
                    }
                },
                onInspectionResult = { result ->
                    currentInspectionResult = result
                    coroutineScope.launch {
                        scanRepo.saveScan(
                            ScanRecordEntity(
                                denomination = result.denomination,
                                isGenuine = result.isGenuine,
                                confidence = result.confidence,
                                serialNumber = result.serialNumber,
                                timestamp = result.timestamp,
                                locationName = result.locationName,
                                defectsSummary = result.defects.joinToString(" • ")
                            )
                        )
                    }
                    navController.navigate(Routes.RESULT)
                }
            )
        }

        composable(Routes.RESULT) {
            val result = currentInspectionResult ?: ClassificationResult(
                label = "genuine_200_etb",
                isGenuine = true,
                denomination = 200,
                confidence = 0.987f,
                serialNumber = "FJ 0302758"
            )
            InspectionResultScreen(
                result = result,
                onScanNext = {
                    navController.navigate(Routes.SCANNER) {
                        popUpTo(Routes.HOME)
                    }
                },
                onBroadcastHeatmap = { navController.navigate(Routes.FRAUD_MAP) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.MANUAL_GUIDE) {
            ManualGuideScreen(
                onBackClick = { navController.popBackStack() },
                onReportIncident = { navController.navigate(Routes.REPORT) }
            )
        }

        composable(Routes.FRAUD_MAP) {
            FraudMapScreen(
                intelRepo = intelRepo,
                onNavigateToHome = { navController.navigate(Routes.HOME) },
                onNavigateToScan = { navController.navigate(Routes.SCANNER) },
                onNavigateToAlerts = { navController.navigate(Routes.ALERTS) },
                onNavigateToIntel = { navController.navigate(Routes.INTELLIGENCE) },
                onReportIncidentClick = { navController.navigate(Routes.REPORT) }
            )
        }

        composable(Routes.REPORT) {
            IncidentReportScreen(
                initialSerial = currentInspectionResult?.serialNumber ?: "BD1029384",
                initialDenom = currentInspectionResult?.denomination ?: 200,
                onSubmitReport = { report ->
                    scanRepo.reportIncident(report)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.ALERTS) {
            AlertsScreen(
                intelRepo = intelRepo,
                onNavigateToHome = { navController.navigate(Routes.HOME) },
                onNavigateToScan = { navController.navigate(Routes.SCANNER) },
                onNavigateToMap = { navController.navigate(Routes.FRAUD_MAP) },
                onNavigateToIntel = { navController.navigate(Routes.INTELLIGENCE) },
                onInspectGuideClick = { navController.navigate(Routes.MANUAL_GUIDE) }
            )
        }

        composable(Routes.INTELLIGENCE) {
            IntelligenceScreen(
                intelRepo = intelRepo,
                onNavigateToHome = { navController.navigate(Routes.HOME) },
                onNavigateToScan = { navController.navigate(Routes.SCANNER) },
                onNavigateToMap = { navController.navigate(Routes.FRAUD_MAP) },
                onNavigateToAlerts = { navController.navigate(Routes.ALERTS) }
            )
        }

        composable(Routes.HISTORY) {
            ScanHistoryScreen(
                scans = scans,
                onDeleteScan = { id ->
                    coroutineScope.launch {
                        scanRepo.deleteScan(id)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
