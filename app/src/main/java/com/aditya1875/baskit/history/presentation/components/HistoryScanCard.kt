package com.aditya1875.baskit.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aditya1875.baskit.history.data.local.ScanEntity
import com.aditya1875.baskit.ui.theme.CardBg
import com.aditya1875.baskit.ui.theme.GreenAccent
import com.aditya1875.baskit.ui.theme.TextPrimary
import com.aditya1875.baskit.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScanCard(scan: ScanEntity, onClick: () -> Unit) {
    val isHealthy = scan.score > 60
    val accentColor = if (isHealthy) GreenAccent else Color(0xFFFF6B6B)
    val scoreColor = when {
        scan.score >= 70 -> GreenAccent
        scan.score >= 45 -> Color(0xFFFFAA00)
        else -> Color(0xFFFF4444)
    }
    val scoreBg = when {
        scan.score >= 70 -> Color(0xFF0A2A14)
        scan.score >= 45 -> Color(0xFF2A1C00)
        else -> Color(0xFF2A0808)
    }
    val timeStr = remember(scan.scannedAt) {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(scan.scannedAt))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(CardBg)
            .clickable(onClick = onClick)
    ) {
        // Left colored accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(88.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(accentColor, accentColor.copy(alpha = 0.3f))
                    )
                )
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E1E1E))
            ) {
                if (!scan.imageFrontUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = scan.imageFrontUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Center info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    scan.productName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary,
                    maxLines = 2
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Score pill
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(scoreBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            Modifier.size(7.dp).clip(CircleShape).background(scoreColor)
                        )
                        Text(
                            "Score: ${scan.score}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = scoreColor
                        )
                    }
                    // Calories
                    scan.calories?.let { kcal ->
                        Text(
                            "$kcal kcal",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Time top-right
            Text(
                timeStr,
                style = MaterialTheme.typography.labelSmall.copy(lineHeight = 14.sp),
                color = TextSecondary,
                modifier = Modifier.align(Alignment.Top)
            )
        }
    }
}