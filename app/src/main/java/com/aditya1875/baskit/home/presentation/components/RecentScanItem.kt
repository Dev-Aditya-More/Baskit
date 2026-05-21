package com.aditya1875.baskit.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.aditya1875.baskit.home.presentation.screen.relativeTime
import com.aditya1875.baskit.ui.theme.CardBg
import com.aditya1875.baskit.ui.theme.TextPrimary
import com.aditya1875.baskit.ui.theme.TextSecondary

@Composable
fun RecentScanItem(scan: ScanEntity, onClick: () -> Unit) {
    val scoreColor = when {
        scan.score >= 70 -> Color(0xFF00E676)
        scan.score >= 45 -> Color(0xFFFFAA00)
        else -> Color(0xFFFF4444)
    }
    val scoreBg = when {
        scan.score >= 70 -> Color(0xFF002A12)
        scan.score >= 45 -> Color(0xFF2A1E00)
        else -> Color(0xFF2A0808)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left accent stripe
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(72.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(scoreColor, scoreColor.copy(alpha = 0.3f))
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
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1A2A1A))
            ) {
                if (!scan.imageFrontUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = scan.imageFrontUrl,
                        contentDescription = scan.productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    scan.productName.take(40),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    relativeTime(scan.scannedAt),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = TextSecondary
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(scoreBg)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    "${scan.score}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = scoreColor
                )
                Box(
                    Modifier
                        .size(width = 16.dp, height = 1.dp)
                        .background(scoreColor.copy(alpha = 0.4f))
                )
                Text(
                    "100",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 9.sp
                    ),
                    color = scoreColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}
