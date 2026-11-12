package com.example.presentation.screens.report

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.IncidentReportEntity
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun IncidentReportScreen(
    initialSerial: String = "BD1029384",
    initialDenom: Int = 200,
    onSubmitReport: suspend (IncidentReportEntity) -> Unit,
    onBackClick: () -> Unit
) {
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
                                latitude = 9.0345,
                                longitude = 38.7360,
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
                            val isSel = denomination == denom
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) EthioRed else SurfaceContainerLow,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { denomination = denom }
                                    .testTag("report_denom_$denom")
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$denom ETB",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                // Suspect Serial Number
                item {
                    Text("Suspect Serial Number", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = serialNumber,
                        onValueChange = { serialNumber = it.uppercase() },
                        placeholder = { Text("e.g. BD1029384") },
                        leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null, tint = EthioRed) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("suspect_serial_input")
                    )
                }

                // Incident Location
                item {
                    Text("Incident Sub-City / Location", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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
                                Text("GPS Tagged", color = EthioGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("subcity_input")
                    )
                }

                // Optional Notes
                item {
                    Text("Inspection Notes / Observed Defects", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("Describe physical flaws: missing thread, blurry printing, slick texture...") },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("notes_input")
                    )
                }

                // Anonymous Mode Switch
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isAm) "ስም የማያሳይ ሪፖርት (Anonymous Mode)" else "Anonymous Reporting Mode",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Protects identity. Device hardware tokens are cryptographically redacted before dispatch.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = isAnonymous,
                                onCheckedChange = { isAnonymous = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EthioGreen, checkedTrackColor = EthioGreenFixed),
                                modifier = Modifier.testTag("anonymous_toggle")
                            )
                        }
                    }
                }
            }
        }
    }
}
