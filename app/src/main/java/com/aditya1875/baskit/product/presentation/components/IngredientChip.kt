package com.aditya1875.baskit.product.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aditya1875.baskit.ui.theme.DangerRed
import com.aditya1875.baskit.ui.theme.TextHi
import com.aditya1875.baskit.ui.theme.TextPrimary


@Composable
fun IngChip(name: String, flagged: Boolean) {
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (flagged) DangerRed.copy(0.2f) else Color.White.copy(0.05f))
            .border(1.dp,
                if (flagged) DangerRed.copy(0.5f) else Color.White.copy(0.1f),
                RoundedCornerShape(20.dp))
            .padding(horizontal = 13.dp, vertical = 7.dp)
    ) {
        Text(
            name,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = if (flagged) DangerRed else TextHi
        )
    }
}