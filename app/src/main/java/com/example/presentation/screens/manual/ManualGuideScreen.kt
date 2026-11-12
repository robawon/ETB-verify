package com.example.presentation.screens.manual

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.SecurityFeaturesCatalog
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*

@Composable
fun ManualGuideScreen(
    onBackClick: () -> Unit,
    onReportIncident: () -> Unit
) {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()

    var selectedDenom by remember { mutableIntStateOf(200) }
    var isFrontView by remember { mutableStateOf(true) }
    var selectedFeatureId by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የብር ኖቶች የደህንነት መለያዎች" else "Banknote Security Guide",
                subtitle = "National Bank of Ethiopia Directive",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("manual_guide_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // NBE Directive Header Poster Banner
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF04240C),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x666FDD79)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF6FDD79), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("የኢትዮጵያ ብሔራዊ ባንክ (NBE Poster)", color = Color(0xFF6FDD79), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("Official Reference", color = Color(0xFFFEDF10), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.nbe_poster),
                                contentDescription = "NBE Poster",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            // Denomination Selector Tabs
            item {
                Text(
                    text = if (isAm) "የብር ኖቱን ይምረጡ / SELECT NOTE" else "Select Denomination",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(200, 100, 50, 10).forEach { denom ->
                        val isSelected = selectedDenom == denom
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) EthioGreenContainer else SurfaceContainerLow,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) EthioGreen else MaterialTheme.colorScheme.surfaceContainerHighest
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedDenom = denom }
                                .testTag("guide_denom_$denom")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$denom",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isAm) "ብር" else "Birr",
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color(0xFFC0CDC0) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Banknote Specimen with 8 numbered pin markers
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = EthioGreenContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "$selectedDenom BIRR SERIES",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Row {
                                TextButton(
                                    onClick = { isFrontView = true },
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = if (isAm) "የፊት ገጽ" else "Front",
                                        fontWeight = if (isFrontView) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isFrontView) EthioGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                TextButton(
                                    onClick = { isFrontView = false },
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = if (isAm) "የኋላ ገጽ" else "Back",
                                        fontWeight = if (!isFrontView) FontWeight.Bold else FontWeight.Normal,
                                        color = if (!isFrontView) EthioGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            val img = when {
                                selectedDenom == 100 && isFrontView -> R.drawable.etb_100_front
                                selectedDenom == 100 && !isFrontView -> R.drawable.etb_100_back
                                selectedDenom == 200 && isFrontView -> R.drawable.etb_200_front
                                else -> R.drawable.etb_200_back
                            }

                            Image(
                                painter = painterResource(id = img),
                                contentDescription = "$selectedDenom ETB Specimen",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // 8 Hotspot Pins Overlay
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf(1, 2, 3, 4, 5, 6, 7, 8).take(4).forEach { num ->
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(if (selectedFeatureId == num) Color(0xFFFEDF10) else Color(0xCC000000))
                                            .clickable { selectedFeatureId = num },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$num",
                                            color = if (selectedFeatureId == num) Color.Black else Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 8 Security Points Detailed Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAm) "የደህንነት ምልክቶች ዝርዝር (8 Points)" else "Security Features Checklist (8 Points)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text("Bilingual Guide", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                }
            }

            items(SecurityFeaturesCatalog.features) { feat ->
                val isSelected = selectedFeatureId == feat.id
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) Color(0xFFF1F8F3) else SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) EthioGreen else MaterialTheme.colorScheme.surfaceContainerHighest
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedFeatureId = feat.id }
                        .testTag("feature_card_${feat.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) EthioGreen else Color(0xFFFEDF10)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = feat.number,
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isAm) feat.titleAm else feat.titleEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Surface(
                                    color = SurfaceContainerHigh,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        feat.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (isAm) feat.descAm else feat.descEn,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TouchApp, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = feat.actionTag,
                                    color = EthioGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Spot the Fake comparison section
            item {
                Text(
                    text = if (isAm) "ትክክለኛ vs ሀሰተኛ (Spot the Fake)" else "Authentic vs Counterfeit (Spot the Fake)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                SecurityFeaturesCatalog.spotChecks.forEach { check ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isAm) check.titleAm else check.titleEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Genuine row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BadgeAuthenticBg)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text("ትክክለኛ (Genuine)", color = EthioGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                    Text(if (isAm) check.genuineAm else check.genuineEn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Counterfeit row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BadgeCounterfeitBg)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = null, tint = EthioRed, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text("ሀሰተኛ (Counterfeit)", color = EthioRed, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                    Text(if (isAm) check.counterfeitAm else check.counterfeitEn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }

            // Official Notice & Report CTA
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFBA1A1A),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAm) "ማስጠንቀቂያ! / OFFICIAL NOTICE" else "OFFICIAL NOTICE",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isAm)
                                "አዲስ የታተሙት የብር ዓይነቶች በእጃችሁ ሲደርሱ ከላይ የተገለጹትን የደህንነት ምልክቶች ማሟላታቸውን ያረጋግጡ። አጠራጣሪ ወይም ሀሰተኛ የብር ኖት የያዘ ሰው ለፖሊስ ወዲያውኑ ማሳወቅ አለበት።"
                            else
                                "Federal Law strictly prohibits possession, trade, or printing of fraudulent currency notes. Suspect notes must be quarantined immediately.",
                            color = Color(0xFFFFEBEE),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onReportIncident,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("guide_report_button")
                        ) {
                            Icon(Icons.Default.Report, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAm) "አጠራጣሪ ብር ሪፖርት አድርግ (Report Incident)" else "Report Counterfeit Note",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
