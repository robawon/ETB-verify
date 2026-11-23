package com.example.presentation.screens.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.IncidentReportEntity
import com.example.domain.model.ClassificationResult
import com.example.domain.report.ReportData
import com.example.domain.report.buildReportPdf
import com.example.domain.report.shareReportPdf
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.example.presentation.components.LocationPermissionStatus
import com.example.presentation.components.PermissionNotice
import com.example.presentation.components.getCurrentLatLng
import com.example.presentation.components.hasLocationPermission
import com.example.presentation.components.openAppSettings
import com.example.presentation.components.rememberLocationPermissionRequester
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Composable screen for submitting an incident report about a counterfeit banknote.
 */
@Composable
fun IncidentReportScreen(
    initialSerial: String = "",
    initialDenom: Int = 200,
    scanResult: ClassificationResult? = null,
    onSubmitReport: suspend (IncidentReportEntity) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()

    var denomination by remember { mutableIntStateOf(initialDenom) }
    var serialNumber by remember { mutableStateOf(initialSerial) }
    var subCity by remember { mutableStateOf("Merkato, Addis Ketema") }
    var notes by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var isBuildingPdf by remember { mutableStateOf(false) }

    // --- Real GPS for the report ---
    val context = LocalContext.current
    var gpsLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var locationProblem by remember { mutableStateOf<String?>(null) }
    var locationPermanentlyDenied by remember { mutableStateOf(false) }
    var isLocating by remember { mutableStateOf(false) }

    fun locateMe() {
        coroutineScope.launch {
            isLocating = true
            val result = withTimeoutOrNull(10_000) { getCurrentLatLng(context) }
            isLocating = false
            if (result != null) {
                gpsLocation = result
                locationProblem = null
                locationPermanentlyDenied = false
            } else {
                locationProblem = if (isAm) {
                    "አካባቢዎን ማግኘት አልተቻለም። አካባቢ (GPS) መብራቱን ያረጋግጡ። ያለ እሱም ሪፖርት ማስገባት ይችላሉ።"
                } else {
                    "Couldn't get your location. Make sure location (GPS) is turned on. You can still submit without it."
                }
            }
        }
    }

    val deniedMessage = if (isAm) {
        "አካባቢ ፈቃድ አልተሰጠም። ሪፖርቱ ከቦታ መረጃ ውጭ ይላካል። ያለ እሱም ሪፖርት ማስገባት ይችላሉ።"
    } else {
        "Location access was denied, so this report will not carry a GPS tag. You can still submit it."
    }

    val requestLocation = rememberLocationPermissionRequester { status ->
        when (status) {
            LocationPermissionStatus.GRANTED -> locateMe()
            LocationPermissionStatus.DENIED -> {
                locationPermanentlyDenied = false
                locationProblem = deniedMessage
            }
            LocationPermissionStatus.PERMANENTLY_DENIED -> {
                locationPermanentlyDenied = true
                locationProblem = deniedMessage
            }
        }
    }

    // Ask once when the screen opens
    LaunchedEffect(Unit) {
        if (hasLocationPermission(context)) locateMe() else requestLocation()
    }

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የአስመሳይ ብር ሪፖርት" else "Report Counterfeit",
                subtitle = "Federal Fraud Database Intake",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp)
            ) {
                Button(
                    onClick = {
                        isSubmitting = true
                        coroutineScope.launch {
                            val incident = IncidentReportEntity(
                                denomination = denomination,
                                serialNumber = serialNumber.ifBlank { "UNKNOWN" },
                                riskLevel = "CRITICAL",
                                subCity = subCity,
                                latitude = gpsLocation?.first ?: 9.0345,
                                longitude = gpsLocation?.second ?: 38.7360,
                                notes = notes,
                                isAnonymous = isAnonymous
                            )
                            onSubmitReport(incident)
                            isSubmitting = false
                            isSuccess = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EthioRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_report_button")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAm) "ሪፖርቱን በይፋ አስገባ" else "Submit Forensic Report",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (isSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EthioGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(BadgeAuthenticBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(36.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isAm) "ሪፖርቱ በተሳካ ሁኔታ ተልኳል!" else "Report Filed Successfully!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Reference Case: #ETH-2026-${System.currentTimeMillis() % 10000}",
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isAm)
                                "የመረጃው ዝርዝር ለፌዴራል ፖሊስና ለብሔራዊ ባንክ ተላልፏል። ማህበረሰቡን ስለጠበቁ እናመሰግናለን።"
                            else
                                "Incident telemetry dispatched to National Bank of Ethiopia and local law enforcement fraud response team.",
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onBackClick,
                            colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Return to Dashboard", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                if (isBuildingPdf) return@OutlinedButton
                                isBuildingPdf = true
                                coroutineScope.launch(Dispatchers.IO) {
                                    try {
                                        val reportData = ReportData(
                                            denomination = denomination,
                                            serialNumber = serialNumber,
                                            locationText = subCity,
                                            notes = notes,
                                            isAnonymous = isAnonymous,
                                            aiVerdictCounterfeit = scanResult?.let { !it.isGenuine },
                                            aiConfidence = scanResult?.confidence,
                                            timestamp = System.currentTimeMillis(),
                                            isAmharic = isAm
                                        )
                                        val pdfFile = buildReportPdf(context, reportData)
                                        withContext(Dispatchers.Main) {
                                            isBuildingPdf = false
                                            shareReportPdf(context, pdfFile)
                                        }
                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            isBuildingPdf = false
                                            Toast.makeText(context, "Could not create the PDF", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isBuildingPdf) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAm) "ሪፖርቱን በPDF አጋራ" else "Share report as PDF",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("report_form_content"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (isAm) "የተጠረጠረ የብር ኖት መረጃ" else "Suspected Banknote Information",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isAm) "ህገ-ወጥ ገንዘብን ለመግታት መረጃዎችን በጥንቃቄ ይሙሉ" else "Help protect Ethiopian financial markets by submitting forged note details.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Denomination Selector
                item {
                    Text("Denomination", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(200, 100, 50).forEach { denom ->
                            val isSelected = denomination == denom
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) EthioRedContainer else SurfaceContainerLow,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EthioRed else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { denomination = denom }
                                    .testTag("report_denom_$denom")
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$denom ETB",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) EthioRed else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Serial Number Field
                item {
                    Text(if (isAm) "መለያ ቁጥር (Serial Number)" else "Serial Number", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = serialNumber,
                        onValueChange = { serialNumber = it.uppercase() },
                        placeholder = { Text("e.g. FJ 0302758") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("report_serial_input")
                    )
                }

                // Location / Sub-city
                item {
                    Text(if (isAm) "ቦታ / ክፍለ ከተማ (Sub-City)" else "Location / Sub-City", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = subCity,
                        onValueChange = { subCity = it },

                        placeholder = { Text("e.g. Merkato, Shema Tera") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = EthioGreen) },
                        trailingIcon = {
                            Surface(
                                color = BadgeAuthenticBg,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    if (gpsLocation != null) "GPS Tagged" else "No GPS",
                                    color = if (gpsLocation != null) EthioGreen else EthioRed,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(4.dp)
                                )                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("report_subcity_input")
                    )
                    if (isLocating) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(if (isAm) "አካባቢዎን በማግኘት ላይ…" else "Finding your location…", fontSize = 11.sp)
                    }
                    gpsLocation?.let { (lat, lng) ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = (if (isAm) "መጋጠሚያ: " else "Coordinates: ") + "%.5f, %.5f".format(lat, lng),
                            color = EthioGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    locationProblem?.let { problem ->
                        Spacer(modifier = Modifier.height(8.dp))
                        PermissionNotice(
                            title = if (isAm) "አካባቢ ያስፈልጋል" else "Location access needed",
                            message = problem,
                            buttonText = if (locationPermanentlyDenied) {
                                if (isAm) "ቅንብሮችን ክፈት" else "Open Settings"
                            } else {
                                if (isAm) "ድጋሚ ሞክር" else "Try again"
                            },
                            onButtonClick = {
                                when {
                                    locationPermanentlyDenied -> openAppSettings(context)
                                    hasLocationPermission(context) -> locateMe()
                                    else -> requestLocation()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Notes / Description
                item {
                    Text(if (isAm) "ተጨማሪ ማስታወሻዎች" else "Incident Notes", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text(if (isAm) "ማንኛውም ዝርዝር ሁኔታ..." else "Describe suspicious texture, watermarks, or transaction context...") },
                        modifier = Modifier.fillMaxWidth().height(110.dp).testTag("report_notes_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Anonymous Toggle
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(if (isAnonymous) "Anonymous Reporter" else "Public Reporter", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(
                                    text = if (isAnonymous) "Your identity is hidden from public ledgers." else "Your credentials will be registered.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Switch(
                                checked = isAnonymous,
                                onCheckedChange = { isAnonymous = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EthioRed)
                            )
                        }
                    }
                }
            }
        }
    }
}
