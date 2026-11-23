package com.example.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.localization.LocalAppLanguage

/**
 * Composable badge component indicating that displayed data is sample or demo data.
 */
@Composable
fun DemoBadge() {
    val langManager = LocalAppLanguage.current
    val isAm = langManager.isAmharic()
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFB300).copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300))
    ) {
        Text(
            text = if (isAm) "የናሙና መረጃ" else "DEMO DATA",
            color = Color(0xFF8F6B00),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
