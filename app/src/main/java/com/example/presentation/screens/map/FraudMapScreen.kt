package com.example.presentation.screens.map

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.IntelligenceRepository
import com.example.domain.model.HotspotZone
import com.example.domain.model.RiskLevel
import com.example.presentation.components.BottomShieldNavigation
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*

@Composable
fun FraudMapScreen(
    intelRepo: IntelligenceRepository,
    onNavigateToHome: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToIntel: () -> Unit,
    onReportIncidentClick: () -> Unit
) {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()
    val hotspots = remember { intelRepo.getHotspots() }

    var selectedFilter by remember { mutableStateOf("All Notes") }
    var selectedHotspot by remember { mutableStateOf<HotspotZone?>(hotspots.firstOrNull()) }
    var searchQuery by remember { mutableStateOf("Merkato, Addis Ababa") }

    val infiniteTransition = rememberInfiniteTransition(label = "radar_sweep")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የማጭበርበር ካርታ" else "Fraud Map",
                subtitle = if (isAm) "የቀጥታ ስርጭት ራዳር" else "Live Addis Ababa Hotspot Radar"
            )
        },
        bottomBar = {
            BottomShieldNavigation(
                currentRoute = "fraud_map",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "scanner" -> onNavigateToScan()
                        "alerts" -> onNavigateToAlerts()
                        "intelligence" -> onNavigateToIntel()
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("fraud_map_content")
        ) {
            // Search Bar & Filters
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EthioGreen) },
                        trailingIcon = { Icon(Icons.Default.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.outline) },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceContainerLowest,
                            unfocusedContainerColor = SurfaceContainerLowest,
                            focusedBorderColor = EthioGreen
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("map_search_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All Notes", "200 ETB Only", "Last 24 Hours").forEach { filter ->
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
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Vector GIS Radar Map
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFE8EEF5))
                        .testTag("gis_radar_canvas")
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2
                        val cy = size.height / 2

                        // Map road grid lines simulation
                        val roadColor = Color(0xFFD4DFEB)
                        for (i in 0..10) {
                            val y = i * (size.height / 10)
                            drawLine(roadColor, Offset(0f, y), Offset(size.width, y), 1.5.dp.toPx())
                        }
                        for (i in 0..10) {
                            val x = i * (size.width / 10)
                            drawLine(roadColor, Offset(x, 0f), Offset(x, size.height), 1.5.dp.toPx())
                        }

                        // Highway yellow lines
                        drawLine(Color(0xFFF9D67A), Offset(0f, cy * 0.8f), Offset(size.width, cy * 1.2f), 4.dp.toPx())
                        drawLine(Color(0xFFF9D67A), Offset(cx * 0.7f, 0f), Offset(cx * 1.1f, size.height), 3.5.dp.toPx())

                        // Radar circles
                        drawCircle(EthioGreen.copy(alpha = 0.15f), radius = 110.dp.toPx(), center = Offset(cx, cy), style = Stroke(1.dp.toPx()))
                        drawCircle(EthioGreen.copy(alpha = 0.10f), radius = 60.dp.toPx(), center = Offset(cx, cy), style = Stroke(1.dp.toPx()))

                        // Radar sweep line
                        val rad = Math.toRadians(sweepAngle.toDouble())
                        val endX = cx + (110.dp.toPx() * Math.cos(rad)).toFloat()
                        val endY = cy + (110.dp.toPx() * Math.sin(rad)).toFloat()
                        drawLine(EthioGreen.copy(alpha = 0.4f), Offset(cx, cy), Offset(endX, endY), 2.dp.toPx())

                        // Merkato High Risk Zone Halo
                        drawCircle(Color(0x33BA1A1A), radius = 38.dp.toPx(), center = Offset(cx * 0.65f, cy * 0.75f))
                        drawCircle(EthioRed, radius = 5.dp.toPx(), center = Offset(cx * 0.65f, cy * 0.75f))

                        // Bole Medium Risk Zone Halo
                        drawCircle(Color(0x33FCDD09), radius = 28.dp.toPx(), center = Offset(cx * 1.45f, cy * 1.15f))
                        drawCircle(Color(0xFFFCDD09), radius = 5.dp.toPx(), center = Offset(cx * 1.45f, cy * 1.15f))

                        // Piassa Clean Zone Halo
                        drawCircle(Color(0x33006D23), radius = 24.dp.toPx(), center = Offset(cx * 1.1f, cy * 0.45f))
                        drawCircle(EthioGreen, radius = 5.dp.toPx(), center = Offset(cx * 1.1f, cy * 0.45f))
                    }

                    // Floating Markers on top of Map
                    // Piassa Verified
                    Surface(
                        color = EthioGreenContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 28.dp, start = 40.dp)
                            .clickable { selectedHotspot = hotspots.find { it.id == "hz_piassa" } }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Piassa Verified • 14 scans", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Merkato Red Alert Pin
                    Surface(
                        color = EthioRed,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 24.dp, bottom = 40.dp)
                            .clickable { selectedHotspot = hotspots.find { it.id == "hz_merkato" } }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Merkato: 8 Reports", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Bole Yellow Alert Pin
                    Surface(
                        color = Color(0xFFE2C600),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 24.dp, bottom = 55.dp)
                            .clickable { selectedHotspot = hotspots.find { it.id == "hz_bole" } }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Bole: 2 Reports", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Floating Controls on Right Side (Layers, GPS, Traffic Light)
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FloatingActionButton(
                            onClick = { },
                            containerColor = SurfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(Icons.Default.Layers, contentDescription = "Layers", modifier = Modifier.size(18.dp))
                        }

                        FloatingActionButton(
                            onClick = { },
                            containerColor = SurfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(Icons.Default.MyLocation, contentDescription = "My Location", modifier = Modifier.size(18.dp))
                        }

                        // Severity Legend Traffic Light
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceContainerLowest,
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EthioRed))
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFEDF10)))
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EthioGreen))
                            }
                        }
                    }

                    // Floating Action Button: + REPORT INCIDENT
                    Button(
                        onClick = onReportIncidentClick,
                        colors = ButtonDefaults.buttonColors(containerColor = EthioRed),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .height(42.dp)
                            .testTag("map_report_incident_fab")
                    ) {
                        Icon(Icons.Default.AddAlert, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAm) "+ ሪፖርት አድርግ" else "+ REPORT INCIDENT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Nearby Incident Feed Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Radar, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAm) "የአካባቢ አደጋዎች ዜና" else "Nearby Incident Feed",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "4 Active • 5km",
                            color = EthioGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Detailed Incident Cards in Feed
            items(hotspots) { hotspot ->
                val isCritical = hotspot.risk == RiskLevel.HIGH
                val badgeColor = if (isCritical) EthioRed else Color(0xFF854D0E)
                val badgeBg = if (isCritical) Color(0xFFFFEBEE) else Color(0xFFFEFBE8)

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("hotspot_item_${hotspot.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = badgeBg
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        if (isCritical) Icons.Default.Cancel else Icons.Default.WarningAmber,
                                        contentDescription = null,
                                        tint = badgeColor,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isCritical) "CRITICAL ALERT • 200 ETB" else "MEDIUM RISK • 100 ETB",
                                        color = badgeColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(hotspot.timeAgo, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = if (isAm) hotspot.nameAm else hotspot.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                color = SurfaceContainerLow,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "${hotspot.distanceKm} km away",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EthioGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = hotspot.batchDescription,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = SurfaceContainerLow, shape = RoundedCornerShape(6.dp)) {
                                Text("Offset Dye Duplication", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Surface(color = SurfaceContainerLow, shape = RoundedCornerShape(6.dp)) {
                                Text("Sub-City Police Dispatched", fontSize = 10.sp, color = EthioGreen, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(36.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("View Evidence", fontSize = 11.sp)
                            }

                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(36.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(Icons.Default.BroadcastOnHome, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Warn Merchants", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // NBE Intelligence Sync Banner
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFEFF5FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCFE1FF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("National Bank of Ethiopia Intelligence Sync", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0D47A1))
                            Text(
                                "Automated cluster analysis indicates illicit batch circulation concentrates along major light-rail commuter arteries during evening market hours.",
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
