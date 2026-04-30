package com.aditya1875.baskit.product.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.baskit.product.presentation.model.HealthInsight
import com.aditya1875.baskit.product.presentation.model.Sentiment
import com.aditya1875.baskit.ui.theme.CardBg
import com.aditya1875.baskit.ui.theme.DangerRed
import com.aditya1875.baskit.ui.theme.GreenAccent
import com.aditya1875.baskit.ui.theme.TextSecondary
import com.aditya1875.baskit.ui.theme.WarnAmber

@Composable
fun InsightRow(insight: HealthInsight) {
    val (bg, accent) = when (insight.sentiment) {
        Sentiment.GOOD -> Color(0xFF0D2A1A) to GreenAccent
        Sentiment.WARN -> Color(0xFF2A1F0A) to WarnAmber
        Sentiment.BAD -> Color(0xFF2A0D0D) to DangerRed
        Sentiment.NEUTRAL -> CardBg to TextSecondary
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(insight.icon, null, tint = accent, modifier = Modifier.size(18.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(insight.label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = accent)
            Spacer(Modifier.height(2.dp))
            Text(insight.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 17.sp)
        }
    }
}