package com.example.presentation.screens.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.IntelligenceRepository
import com.example.domain.model.RiskLevel
import com.example.presentation.components.BottomShieldNavigation
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*

@Composable
fun AlertsScreen(
    intelRepo: IntelligenceRepository,
    onNavigateToHome: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToIntel: () -> Unit,
    onInspectGuideClick: () -> Unit
) {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()
    val hotspots = remember { intelRepo.getHotspots() }

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የአደጋ ማስጠንቀቂያዎች" else "Nearby Threat Alerts",
                subtitle = "Active Geofence Radar"
            )
        },
        bottomBar = {
            BottomShieldNavigation(
                currentRoute = "alerts",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "scanner" -> onNavigateToScan()
                        "fraud_map" -> onNavigateToMap()
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
                .testTag("alerts_screen_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Geofence status card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF04200C),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EthioGreenFixed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0x3300FF66)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CellTower, contentDescription = null, tint = EthioGreenFixed, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(EthioGreenFixed))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("GEOFENCE SURVEILLANCE ACTIVE", color = EthioGreenFixed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = if (isAm) "በአቅራቢያ ያሉ የብር አደጋዎች ክትትል" else "Proximity Threat Radar: Addis Ababa",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Radius: 5.0 km • Background Telemetry Active",
                                color = Color(0xFFA0C0A0),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = if (isAm) "የቅርብ ጊዜ አደጋዎች" else "Active Threat Broadcasts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(hotspots) { hotspot ->
                val isHigh = hotspot.risk == RiskLevel.HIGH
                val borderColor = if (isHigh) EthioRed else if (hotspot.risk == RiskLevel.MEDIUM) Color(0xFFFEDF10) else EthioGreen
                val bgHeader = if (isHigh) Color(0xFFFFEBEE) else if (hotspot.risk == RiskLevel.MEDIUM) Color(0xFFFEFBE8) else Color(0xFFE8F5E9)

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = bgHeader,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        if (isHigh) Icons.Default.Warning else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (isHigh) EthioRed else Color(0xFF716200),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${hotspot.risk.name} THREAT • ${hotspot.distanceKm} km",
                                        color = if (isHigh) EthioRed else Color(0xFF716200),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(hotspot.timeAgo, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isAm) hotspot.nameAm else hotspot.nameEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isHigh)
                                "Counterfeit Alert: Multiple counterfeit incidents have recently been reported nearby. Verify all received cash carefully."
                            else
                                hotspot.batchDescription,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = onInspectGuideClick,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isAm) "የጸጥታ ምልክቶችን እይ >" else "Check Security Signs >",
                                    color = EthioGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
