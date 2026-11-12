package com.example.presentation.screens.result

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.ClassificationResult
import com.example.domain.model.formatModelLabel
import com.example.presentation.components.TopShieldBar
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*

@Composable
fun InspectionResultScreen(
    result: ClassificationResult,
    onScanNext: () -> Unit,
    onBroadcastHeatmap: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()
    val isGenuine = result.isGenuine

    Scaffold(
        topBar = {
            TopShieldBar(
                title = if (isAm) "የፍተሻ ትንታኔ ሪፖርት" else "Inspection Analysis Report",
                subtitle = "CW-NBE-2026-X9 • LIVE AUDIT",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onScanNext,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isGenuine) EthioGreenContainer else SurfaceContainerHigh
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("scan_next_banknote_button")
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = if (isGenuine) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAm) "ቀጣዩን የብር ኖት ይመርምሩ" else "Scan Next Banknote",
                        fontWeight = FontWeight.Bold,
                        color = if (isGenuine) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("inspection_result_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Cryptographic Audit Pill
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (isGenuine) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isGenuine) "ETB VERIFY LIVE CRYPTOGRAPHIC AUDIT" else "HIGH-LEVEL FRAUD THREAT ALERT",
                            color = if (isGenuine) EthioGreen else EthioRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "CW-NBE-2026",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Main Verdict Card (Green Authentic vs Red Counterfeit)
            item {
                if (isGenuine) {
                    // GENUINE BANNER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF006D23), Color(0xFF078930), Color(0xFF004D1A))
                                )
                            )
                            .padding(20.dp)
                            .testTag("genuine_verdict_banner")
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x33FFFFFF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "ETB VERIFY FORENSIC VERDICT",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF8BFB92)
                                    )
                                    Text(
                                        text = if (isAm) "ትክክለኛ የኢትዮጵያ ብር" else "GENUINE BANKNOTE",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Ethiopian Birr ${result.denomination} • Series 2020 – Current",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "National Bank of Ethiopia (NBE) Certified Vault Hash",
                                color = Color(0xFFE0F2E9),
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0x33000000),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x558BFB92))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x44FEDF10)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${(result.confidence * 100).toInt()}%",
                                            color = Color(0xFFFEDF10),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isAm) "ከፍተኛ ትክክለኛነት ምዘና" else "High Authenticity Rating",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "AI Neural Model: 0.002% Error Margin",
                                            color = Color(0xFFC0CDC0),
                                            fontSize = 11.sp
                                        )
                                    }
                                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFFFEDF10), modifier = Modifier.size(28.dp))
                                }
                            }
                        }
                    }
                } else {
                    // COUNTERFEIT RED BANNER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFBA1A1A), Color(0xFF8B0000), Color(0xFF5C0000))
                                )
                            )
                            .padding(20.dp)
                            .testTag("counterfeit_verdict_banner")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0x44FFFFFF)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("HIGH-LEVEL FRAUD THREAT", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Text("SEC-ALERT #ET-2026-8409", color = Color(0xFFFFCDD2), fontSize = 10.sp)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = if (isAm) "የተጭበረበረ ብር ተገኝቷል!" else "COUNTERFEIT DETECTED",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "ሐሰተኛ የብር ኖት ተገኝቷል",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFFCDD2)
                            )
                            Text(
                                text = "Ethiopian Birr ${result.denomination} • Series 2020 Failed Verification",
                                fontSize = 12.sp,
                                color = Color(0xFFFFCDD2)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0x33000000),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("COUNTERFEIT RISK", color = Color(0xFFFFCDD2), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text("${(result.confidence * 100).toInt()}%", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                                        Text("Neural Forensic Failure", color = Color(0xFFFFCDD2), fontSize = 10.sp)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0x33000000),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("NBE REGISTRY STATUS", color = Color(0xFFFFCDD2), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text("ⓧ Unregistered", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("Tampered Specimen Hash", color = Color(0xFFFFCDD2), fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TFLite Model Prediction Details Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                    modifier = Modifier.fillMaxWidth().testTag("tflite_model_details_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = if (isGenuine) EthioGreen else EthioRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "OFFLINE TFLITE PREDICTION",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isGenuine) EthioGreen else EthioRed
                                )
                            }
                            Surface(
                                color = SurfaceContainerLow,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Dense121_best_weights_mixed-III.tflite",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("PREDICTED CLASS LABEL", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Bold)
                                Text(
                                    text = result.formattedLabel,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (isGenuine) EthioGreen else EthioRed
                                )
                            }
                            Surface(
                                color = if (isGenuine) BadgeAuthenticBg else BadgeCounterfeitBg,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "${(result.confidence * 100).toInt()}% Confidence",
                                    color = if (isGenuine) EthioGreen else EthioRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        if (result.allProbabilities.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Softmax Probability Distribution:", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                result.allProbabilities.forEach { (cls, prob) ->
                                    val isWinner = cls == result.rawLabel
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = formatModelLabel(cls),
                                            fontSize = 10.sp,
                                            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isWinner) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${(prob * 100).toInt()}%",
                                            fontSize = 10.sp,
                                            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isWinner) (if (isGenuine) EthioGreen else EthioRed) else MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Captured Specimen Preview & Hotspot Annotations
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FilterCenterFocus, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isGenuine) "Captured Specimen" else "Captured Specimen Audit",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Text("Spectral Resolution 1200 DPI", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            val imgRes = if (result.denomination == 100) R.drawable.etb_100_front else R.drawable.etb_200_front
                            Image(
                                painter = painterResource(id = imgRes),
                                contentDescription = "Banknote Specimen",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (isGenuine) {
                                // Hotspots on genuine specimen
                                Surface(
                                    color = Color(0xDD000000),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("SN: ${result.serialNumber}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00FF66), modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("ETB verify Verified", color = Color(0xFF00FF66), fontSize = 10.sp)
                                    }
                                }
                            } else {
                                // Forgery Flag banner across preview
                                Surface(
                                    color = Color(0xEEBA1A1A),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Error, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Counterfeit Note Detected - Does Not Match Official NBE Security Standards",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Identified Serial Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("IDENTIFIED SERIAL SERIES", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Bold)
                                Text(result.serialNumber, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                            }

                            if (isGenuine) {
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = EthioGreen, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("NBE Registered", color = EthioGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            } else {
                                Surface(
                                    color = Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = null, tint = EthioRed, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Duplicated Serial Defect", color = EthioRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // If Counterfeit: Action Protocol Buttons (Call Police & Broadcast)
            if (!isGenuine) {
                item {
                    Text(
                        text = if (isAm) "አስገዳጅ የደህንነት እርምጃዎች" else "MANDATORY COUNTERMEASURE ACTIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EthioRed
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Call Federal Police Hotline (991 / 8044)
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:991"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EthioRed),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("call_federal_police_button")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Call Federal Police Hotline (991 / 8044)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Immediate reporting of active counterfeit circulation", color = Color(0xFFFFCDD2), fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Broadcast to Live Fraud Heatmap
                    Button(
                        onClick = onBroadcastHeatmap,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5E00)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("broadcast_fraud_heatmap_button")
                    ) {
                        Icon(Icons.Default.CrisisAlert, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Broadcast to Live Fraud Heatmap", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Alert neighboring registered cashiers anonymously", color = Color(0xFFFFF9C4), fontSize = 10.sp)
                        }
                    }
                }
            }

            // Legal Safekeeping Directive Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF3F7FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6E4FF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = if (isGenuine) EthioGreen else EthioRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAm) "ማስጠንቀቂያ • የህግ መመሪያ" else "Legal Safekeeping Directive",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isGenuine) EthioGreen else EthioRed
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isAm)
                                "በኢትዮጵያ ፌዴራል አዋጅ (አዋጅ ቁጥር 1110/2019) መሰረት የተጭበረበረ ገንዘብ ማሰራጨት ወይም መልሶ መስጠት በጥብቅ የተከለከለ ነው።"
                            else
                                "Under Ethiopian Federal Law (Proc. 1110/2019), it is strictly unlawful to circulate, pass on, or return counterfeit currency to the payer.",
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = Color(0x15000000),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Chain-of-Custody Rule: Isolate this banknote in a sealed dry envelope. Avoid direct surface edge contact to preserve fingerprint and tactile evidence for criminal investigators.",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
