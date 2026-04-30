package com.aditya1875.baskit.home.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Product image
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
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

        // Name + time
        Column(modifier = Modifier.weight(1f)) {
            Text(
                scan.productName.take(40),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextPrimary,
                maxLines = 2
            )
            Spacer(Modifier.height(3.dp))
            Text(
                relativeTime(scan.scannedAt),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        // Score pill
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(scoreBg)
                .padding(horizontal = 10.dp, vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${scan.score}\n100",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                color = scoreColor
            )
        }
    }
}