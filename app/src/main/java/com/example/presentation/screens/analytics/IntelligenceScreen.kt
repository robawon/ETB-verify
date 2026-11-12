package com.example.presentation.screens.analytics

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
import com.example.presentation.components.BottomShieldNavigation
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*

@Composable
fun IntelligenceScreen(
    intelRepo: IntelligenceRepository,
    onNavigateToHome: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToAlerts: () -> Unit
) {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()
    val duplicateAlerts = remember { intelRepo.getSerialDuplicateAlerts() }

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የወንጀል መረጃ ትንታኔ" else "Crime Intelligence",
                subtitle = "Serial OCR Syndicate Detection"
            )
        },
        bottomBar = {
            BottomShieldNavigation(
                currentRoute = "intelligence",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "scanner" -> onNavigateToScan()
                        "fraud_map" -> onNavigateToMap()
                        "alerts" -> onNavigateToAlerts()
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
                .testTag("intelligence_screen_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Syndicate Flag Header Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF3E0A0E),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBA1A1A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Hub, contentDescription = null, tint = Color(0xFFFFB4AB), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "SERIAL DUPLICATION ENGINE",
                                    color = Color(0xFFFFB4AB),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text("ML KIT OCR ACTIVE", color = Color(0xFF8BFB92), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isAm) "ተደጋጋሚ የተጭበረበሩ ተከታታይ ቁጥሮች ተገኝተዋል" else "Counterfeit Ring Detected: Duplicate Serial Series",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isAm)
                                "በአዲስ አበባ የተለያዩ ክፍለ ከተሞች ውስጥ የተመዘገቡ የብር ኖቶች ተመሳሳይ ተከታታይ ቁጥር መያዛቸው በሰው ሰራሽ አስተውሎት ተለይቷል።"
                            else
                                "Automated serial OCR comparison identified identical 7-digit banknote codes circulated by illicit offset duplication networks across Addis Ababa markets.",
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = Color(0xFFFFDAD6)
                        )
                    }
                }
            }

            // Flagged Serial Numbers Table
            item {
                Text(
                    text = if (isAm) "የተጠረጠሩ ተከታታይ ቁጥሮች" else "Flagged Serial Numbers & Syndicates",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(duplicateAlerts.size) { idx ->
                val alert = duplicateAlerts[idx]
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (alert.isSyndicateRing) EthioRed else MaterialTheme.colorScheme.surfaceContainerHighest
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (alert.isSyndicateRing) Color(0xFFFFEBEE) else Color(0xFFFFF9C4)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Tag,
                                        contentDescription = null,
                                        tint = if (alert.isSyndicateRing) EthioRed else EthioGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(alert.serialNumber, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                                    Text("${alert.denomination} ETB Specimen", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                }
                            }

                            Surface(
                                color = if (alert.isSyndicateRing) EthioRed else Color(0xFF6C5E00),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    "${alert.occurrences}x Duplicate Hits",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Detected Circulation Nodes:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        alert.locations.forEach { loc ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(EthioRed))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(loc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            // Regional Risk Scores
            item {
                Text(
                    text = if (isAm) "የአዲስ አበባ ክፍለ ከተሞች የአደጋ ምዘና" else "Regional Risk Intelligence Scores",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val regions = listOf(
                    Triple("Addis Ketema (Merkato)", 88, EthioRed),
                    Triple("Kirkos (Mexico Terminal)", 54, Color(0xFFE2C600)),
                    Triple("Bole Commercial Hub", 22, EthioGreen),
                    Triple("Arada (Piassa Quarter)", 12, EthioGreen)
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        regions.forEach { (name, score, color) ->
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text("$score% Risk Index", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { score / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = color,
                                    trackColor = SurfaceContainerLow
                                )
                            }
                        }
                    }
                }
            }

            // Defect Analysis Breakdown
            item {
                Text(
                    text = if (isAm) "የተጭበረበሩ ብሮች የተለመዱ ጉድለቶች" else "Observed Forgery Defect Distribution",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val defects = listOf(
                            "Flat Offset Cotton / Slick Paper" to "68%",
                            "Missing Micro-metallic Security Thread" to "82%",
                            "Absence of Intaglio Tactile Ridges" to "91%",
                            "UV Phosphorescent Inactive Serial" to "74%"
                        )
                        defects.forEach { (defect, pct) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Checklist, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(defect, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Text(pct, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = EthioRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
