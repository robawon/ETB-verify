package com.example.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ScanRecordEntity
import com.example.presentation.components.BottomShieldNavigation
import com.example.presentation.components.StatusBadge
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    scans: List<ScanRecordEntity>,
    totalCount: Int,
    genuineCount: Int,
    counterfeitCount: Int,
    onNavigateToScan: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToGuide: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToIntel: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onScanDetailClick: (ScanRecordEntity) -> Unit
) {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የኢትዮጵያ ብር ማረጋገጫ" else "ETB verify",
                subtitle = if (isAm) "ብሔራዊ የገንዘብ ጥበቃ ስርዓት" else "National Currency Verification"
            )
        },
        bottomBar = {
            BottomShieldNavigation(
                currentRoute = "home",
                onNavigate = { route ->
                    when (route) {
                        "scanner" -> onNavigateToScan()
                        "fraud_map" -> onNavigateToMap()
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
                .testTag("home_screen_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header title block
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = EthioGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAm) "ብሔራዊ የህዝብ ገንዘብ ጥበቃ" else "NATIONAL PUBLIC CURRENCY SHIELD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EthioGreen
                        )
                    }
                    Text(
                        text = if (isAm) "የህዝብ ብር ማረጋገጫ ስካነር" else "ETB verify Public Scanner",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isAm) "ፈጣን የብር ማረጋገጫ • መለያ አያስፈልግም" else "Instant AI Banknote Authentication • No Account Required",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLow,
                        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariantLight)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAm) "ነፃ የህዝብ አገልግሎት • ለሁሉም ዜጎችና ነጋዴዎች ክፍት" else "Free Public Service • Open for all citizens & merchants",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            if (isAm) "ሪፖርቶችን፣ ተከታታይ ቁጥሮችን ይፈልጉ..." else "Search reports, serial numbers, security...",
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.outline)
                    },
                    trailingIcon = {
                        Icon(Icons.Default.Mic, contentDescription = "Voice Search", tint = MaterialTheme.colorScheme.outline)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest,
                        focusedBorderColor = EthioGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_bar")
                )
            }

            // Critical Alert Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFEEEE),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToAlerts() }
                        .testTag("critical_alert_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EthioRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isAm) "አስቸኳይ ማስጠንቀቂያ • መርካቶ ቀጠና" else "CRITICAL ALERT • Merkato Zone",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EthioRed
                                )
                                Text(
                                    text = if (isAm) "የተጭበረበረ 200 ብር በስርጭት ላይ ይገኛል" else "Counterfeit 200 ETB In Circulation",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isAm) "3 ተመሳሳይ የተጭበረበሩ ተከታታይ ቁጥሮች በመርካቶ ተገኝተዋል" else "3 forged serial series flagged near Merkato, Addis Ababa within the last hour.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = EthioRed)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isAm) "ከ34 ደቂቃ በፊት የተመዘገበ" else "Flagged 34m ago",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (isAm) "ምልክቶችን ይመርምሩ >" else "Review Signatures >",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EthioRed
                            )
                        }
                    }
                }
            }

            // Big Green Verification Action Card
            item {
                Text(
                    text = if (isAm) "የማረጋገጫ እርምጃዎች" else "Verification Actions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF006D23), Color(0xFF078930), Color(0xFF005018))
                            )
                        )
                        .clickable { onNavigateToScan() }
                        .padding(18.dp)
                        .testTag("snap_scan_card")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x33FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0x33FFFFFF)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFFFEDF10), modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Instant Flash Scan", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isAm) "ፎቶ አንስተው በብርሃን ይመርምሩ" else "Snap & Scan with Flashlight",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isAm)
                                "የ 100 እና 200 ብር ኖቶችን ፈጣን በሆነ የካሜራ AI ያረጋግጡ። በማንኛውም የብርሃን ሁኔታ ይሰራል።"
                            else
                                "Instant camera verification for 50, 100 & 200 ETB banknotes. Hold note steady in any lighting condition.",
                            color = Color(0xFFE0F2E9),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Split Action Cards (Report Incident & Fraud Map)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceContainerLowest,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToReport() }
                            .testTag("report_incident_button")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFEBEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ReportProblem, contentDescription = null, tint = EthioRed, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isAm) "ሪፖርት አድርግ" else "Report Incident",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isAm) "የተጠረጠረ ብር ከቦታው ጋር" else "Submit forged note with real-time GPS tag",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceContainerLowest,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToMap() }
                            .testTag("fraud_map_button")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFF9C4)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = EthioGold, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isAm) "የማጭበርበር ካርታ" else "Fraud Map",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isAm) "የቀጥታ አደጋ ቀጠናዎች ራዳር" else "Live Addis Ababa hotspot radar & telemetry",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Audit Archive link banner
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToHistory() }
                        .testTag("audit_archive_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8EAF6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAm) "የተመረመሩ ማህደር" else "Audit Archive",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isAm) "$totalCount በምስጠራ የተረጋገጡ ፍተሻዎች" else "$totalCount Cryptographically Signed Scans",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = if (isAm) "ሁሉንም እይ >" else "View All >",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EthioGreen
                        )
                    }
                }
            }

            // Inspection Telemetry Stats
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAm) "የፍተሻ ስታትስቲክስ" else "Inspection Telemetry",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Oct 2026 Cycle",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Total
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(if (isAm) "ጠቅላላ" else "Scans Total", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "$totalCount", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                                Text(text = if (isAm) "በዚህ ወር" else "This month", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // Genuine
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(if (isAm) "ትክክለኛ" else "Genuine", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EthioGreen))
                                }
                                Text(text = "$genuineCount", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = EthioGreen)
                                val pct = if (totalCount > 0) (genuineCount * 100 / totalCount) else 94
                                Surface(
                                    color = BadgeAuthenticBg,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("$pct%", color = EthioGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                        }

                        // Forged
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(if (isAm) "የተጭበረበረ" else "Forged", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = EthioRed, modifier = Modifier.size(10.dp))
                                }
                                Text(text = "$counterfeitCount", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = EthioRed)
                                Surface(
                                    color = BadgeCounterfeitBg,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        if (isAm) "የተያዙ" else "Intercepted",
                                        color = EthioRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Security Reference Cards (200 ETB & 100 ETB)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isAm) "የጸጥታ ማረጋገጫ መመሪያ" else "Security Reference",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isAm) "የኢትዮጵያ ብሔራዊ ባንክ መመሪያዎች" else "Official National Bank of Ethiopia specifications",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = onNavigateToGuide) {
                        Text(if (isAm) "መመሪያዎች >" else "Library >", color = EthioGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onNavigateToGuide() }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.etb_200_front),
                                        contentDescription = "200 ETB Note",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Surface(
                                        color = Color(0xCC000000),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.align(Alignment.TopStart).padding(6.dp)
                                    ) {
                                        Text("200 ETB", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Series 2020 • Ochre", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(
                                    "Features color-shifting thread, embossed pigeon tactile marking for visually impaired.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = onNavigateToGuide,
                                    modifier = Modifier.fillMaxWidth().height(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isAm) "ምልክቶችን መርምር" else "Inspect Features", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onNavigateToGuide() }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.etb_100_front),
                                        contentDescription = "100 ETB Note",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Surface(
                                        color = Color(0xCC000000),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.align(Alignment.TopStart).padding(6.dp)
                                    ) {
                                        Text("100 ETB", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Series 2020 • Blue", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(
                                    "Windowed security thread, micro-text 'ETB 100', intaglio castle gate motif.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = onNavigateToGuide,
                                    modifier = Modifier.fillMaxWidth().height(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isAm) "ምልክቶችን መርምር" else "Inspect Features", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Recent Field Inspections
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAm) "የቅርብ ጊዜ ፍተሻዎች" else "Recent Field Inspections",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = onNavigateToHistory) {
                        Text(if (isAm) "ሙሉ ማህደር >" else "Audit Log >", color = EthioGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(scans.take(4)) { scan ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onScanDetailClick(scan) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (scan.isGenuine) Color(0xFFE3F2FD) else Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${scan.denomination}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
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
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Text(
                                text = "${scan.locationName} • ${if (scan.isGenuine) "High Conf." else "Threat Flag"}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        StatusBadge(
                            isGenuine = scan.isGenuine,
                            textOverride = if (scan.isGenuine) "${(scan.confidence * 100).toInt()}% Authentic" else "Flagged Fake"
                        )
                    }
                }
            }
        }
    }
}
