package com.example.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.localization.AppLanguage
import com.example.presentation.localization.LocalAppLanguage
import com.example.ui.theme.*

@Composable
fun TopShieldBar(
    title: String = "ETB verify",
    subtitle: String = "National Currency Verification",
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBack) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(EthioGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "EthioShield Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Language Toggle Chip
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { langManager.toggleLanguage() }
                    .testTag("language_toggle_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Language Switcher",
                        tint = EthioGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAm) "English" else "አማርኛ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))
            actions()
        }
    }
}

@Composable
fun BottomShieldNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text(if (isAm) "መነሻ" else "Home", fontSize = 11.sp) },
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EthioGreen,
                selectedTextColor = EthioGreen,
                indicatorColor = EthioGreenFixed.copy(alpha = 0.4f)
            )
        )
        NavigationBarItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (currentRoute == "scanner") EthioGreen else EthioGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.DocumentScanner,
                        contentDescription = "Scan Note",
                        tint = if (currentRoute == "scanner") Color.White else EthioGreen
                    )
                }
            },
            label = { Text(if (isAm) "ቃኝ" else "Scan", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            selected = currentRoute == "scanner",
            onClick = { onNavigate("scanner") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Explore, contentDescription = "Fraud Map") },
            label = { Text(if (isAm) "ካርታ" else "Fraud Map", fontSize = 11.sp) },
            selected = currentRoute == "fraud_map",
            onClick = { onNavigate("fraud_map") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EthioGreen,
                selectedTextColor = EthioGreen,
                indicatorColor = EthioGreenFixed.copy(alpha = 0.4f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
            label = { Text(if (isAm) "አደጋዎች" else "Alerts", fontSize = 11.sp) },
            selected = currentRoute == "alerts",
            onClick = { onNavigate("alerts") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EthioGreen,
                selectedTextColor = EthioGreen,
                indicatorColor = EthioGreenFixed.copy(alpha = 0.4f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Insights, contentDescription = "Intelligence") },
            label = { Text(if (isAm) "ትንታኔ" else "Intel", fontSize = 11.sp) },
            selected = currentRoute == "intelligence",
            onClick = { onNavigate("intelligence") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EthioGreen,
                selectedTextColor = EthioGreen,
                indicatorColor = EthioGreenFixed.copy(alpha = 0.4f)
            )
        )
    }
}

@Composable
fun StatusBadge(
    isGenuine: Boolean,
    modifier: Modifier = Modifier,
    textOverride: String? = null
) {
    val bgColor = if (isGenuine) BadgeAuthenticBg else BadgeCounterfeitBg
    val borderColor = if (isGenuine) BadgeAuthenticBorder else BadgeCounterfeitBorder
    val textColor = if (isGenuine) BadgeAuthenticText else BadgeCounterfeitText
    val icon = if (isGenuine) Icons.Default.CheckCircle else Icons.Default.Warning

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = textOverride ?: if (isGenuine) "98.7% Authentic" else "Flagged Fake",
            color = textColor,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        )
    }
}
