package com.example.presentation.screens.history

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.local.ScanRecordEntity
import com.example.presentation.components.StatusBadge
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanHistoryScreen(
    scans: List<ScanRecordEntity>,
    onDeleteScan: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedScanDetail by remember { mutableStateOf<ScanRecordEntity?>(null) }

    val filteredScans = remember(scans, searchQuery, selectedFilter) {
        scans.filter { scan ->
            val matchesQuery = scan.serialNumber.contains(searchQuery, ignoreCase = true) ||
                    scan.locationName.contains(searchQuery, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "Genuine" -> scan.isGenuine
                "Counterfeit" -> !scan.isGenuine
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }

    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የፍተሻ ማህደር" else "Scan History",
                subtitle = "${scans.size} Signed Records",
                showBack = true,
                onBackClick = onBackClick,
                actions = {
                    IconButton(
                        onClick = {
                            val auditSummary = buildString {
                                appendLine("=== ETB VERIFY AUDIT LOG EXPORT ===")
                                appendLine("Total Scans: ${scans.size}")
                                appendLine("Genuine Notes: ${scans.count { it.isGenuine }}")
                                appendLine("Intercepted Forgeries: ${scans.count { !it.isGenuine }}")
                                appendLine("Timestamp: ${Date()}")
                                appendLine("\nRecent Records:")
                                scans.take(10).forEach {
                                    appendLine("- ${it.denomination} ETB | SN: ${it.serialNumber} | ${if (it.isGenuine) "GENUINE" else "FAKE"} | ${it.locationName}")
                                }
                            }
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "ETB verify Audit Export")
                                putExtra(Intent.EXTRA_TEXT, auditSummary)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Export Audit Log"))
                        },
                        modifier = Modifier.testTag("export_audit_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Export Log", tint = EthioGreen)
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("scan_history_content")
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (isAm) "ተከታታይ ቁጥር ወይም ቦታ ይፈልጉ..." else "Search by serial or location...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EthioGreen) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedContainerColor = SurfaceContainerLowest
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("history_search_input")
            )

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Genuine", "Counterfeit").forEach { filter ->
                    val isSel = selectedFilter == filter
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSel) EthioGreen else SurfaceContainerLow,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedFilter = filter }
                    ) {
                        Text(
                            text = filter,
                            color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredScans.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.FindInPage, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No matching audit logs found", color = MaterialTheme.colorScheme.outline, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredScans, key = { it.id }) { scan ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedScanDetail = scan }
                                .testTag("history_item_${scan.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (scan.isGenuine) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${scan.denomination}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = if (scan.isGenuine) EthioGreen else EthioRed
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${scan.denomination} ETB",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "#${scan.serialNumber}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "${scan.locationName} • ${dateFormat.format(Date(scan.timestamp))}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                StatusBadge(
                                    isGenuine = scan.isGenuine,
                                    textOverride = if (scan.isGenuine) "${(scan.confidence * 100).toInt()}% OK" else "FAKE"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Bottom Sheet
    selectedScanDetail?.let { scan ->
        ModalBottomSheet(
            onDismissRequest = { selectedScanDetail = null },
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Audit Record #${scan.id}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                    StatusBadge(isGenuine = scan.isGenuine)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Denomination: ${scan.denomination} ETB", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Serial Number: ${scan.serialNumber}", fontSize = 12.sp)
                        Text("Confidence Score: ${(scan.confidence * 100).toInt()}%", fontSize = 12.sp)
                        Text("Inspection Location: ${scan.locationName}", fontSize = 12.sp)
                        Text("Timestamp: ${dateFormat.format(Date(scan.timestamp))}", fontSize = 12.sp)
                        if (scan.defectsSummary.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Defects: ${scan.defectsSummary}", color = EthioRed, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDeleteScan(scan.id)
                            selectedScanDetail = null
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EthioRed),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Delete Log")
                    }

                    Button(
                        onClick = { selectedScanDetail = null },
                        colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            }
        }
    }
}
