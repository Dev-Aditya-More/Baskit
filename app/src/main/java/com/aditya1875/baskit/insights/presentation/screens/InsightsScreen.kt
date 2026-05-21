package com.aditya1875.baskit.insights.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aditya1875.baskit.history.data.local.ScanEntity
import com.aditya1875.baskit.insights.presentation.viewmodel.DayActivity
import com.aditya1875.baskit.insights.presentation.viewmodel.InsightsState
import com.aditya1875.baskit.insights.presentation.viewmodel.InsightsViewModel
import com.aditya1875.baskit.product.presentation.utils.scoreColor
import com.aditya1875.baskit.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InsightsScreen(viewModel: InsightsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(Modifier.fillMaxSize().background(Color(0xFF0D0D0D))) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(GreenAccent.copy(alpha = 0.10f), Color.Transparent),
                        center = Offset(500f, 0f),
                        radius = 800f
                    )
                )
        )

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            AnimatedVisibility(visible, enter = fadeIn(tween(400))) {
                Column {
                    Text(
                        "Insights",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = TextPrimary
                    )
                    Text(
                        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            if (state.totalScans == 0) {
                EmptyInsights()
            } else {
                AnimatedVisibility(
                    visible,
                    enter = fadeIn(tween(500, 100)) + slideInVertically { 30 }
                ) {
                    OverviewStats(state)
                }

                Spacer(Modifier.height(20.dp))

                AnimatedVisibility(
                    visible,
                    enter = fadeIn(tween(500, 200)) + slideInVertically { 30 }
                ) {
                    ScoreBreakdown(state)
                }

                Spacer(Modifier.height(20.dp))

                AnimatedVisibility(
                    visible,
                    enter = fadeIn(tween(500, 300)) + slideInVertically { 30 }
                ) {
                    WeeklyActivityChart(state.dayActivity)
                }

                state.bestThisWeek?.let { best ->
                    Spacer(Modifier.height(20.dp))
                    AnimatedVisibility(
                        visible,
                        enter = fadeIn(tween(500, 400)) + slideInVertically { 30 }
                    ) {
                        BestThisWeek(best)
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun EmptyInsights() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(GreenAccent.copy(alpha = 0.10f))
                    .border(1.dp, GreenAccent.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.BarChart, null,
                    tint = GreenAccent,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "No data yet",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Scan some products to see\nyour health insights here.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun OverviewStats(state: InsightsState) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        InsightTile(
            value = state.totalScans.toString(),
            label = "Total\nScans",
            modifier = Modifier.weight(1f)
        )
        InsightTile(
            value = state.avgScore.toString(),
            label = "Avg\nScore",
            valueColor = scoreColor(state.avgScore),
            modifier = Modifier.weight(1f)
        )
        InsightTile(
            value = "${state.healthyPercent}%",
            label = "Healthy\nRate",
            valueColor = GreenAccent,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InsightTile(
    value: String,
    label: String,
    valueColor: Color = TextPrimary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(
                value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = valueColor
            )
            Spacer(Modifier.height(2.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = TextSecondary,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun ScoreBreakdown(state: InsightsState) {
    val tiers = listOf(
        Triple("Excellent", state.excellentCount, Color(0xFF00E676)),
        Triple("Good", state.goodCount, Color(0xFFFFAA00)),
        Triple("Fair", state.fairCount, Color(0xFFFF6B00)),
        Triple("Poor", state.poorCount, Color(0xFFFF4444))
    )
    val total = state.totalScans.coerceAtLeast(1)

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Score Breakdown",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = TextPrimary
        )
        tiers.forEach { (label, count, color) ->
            GradeTierRow(label, count, total, color)
        }
    }
}

@Composable
private fun GradeTierRow(label: String, count: Int, total: Int, color: Color) {
    val fraction = count.toFloat() / total
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            modifier = Modifier.width(66.dp)
        )
        Box(
            Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.White.copy(alpha = 0.06f))
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color.copy(alpha = 0.8f))
            )
        }
        Text(
            count.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary,
            modifier = Modifier.width(20.dp)
        )
    }
}

@Composable
private fun WeeklyActivityChart(days: List<DayActivity>) {
    val maxCount = days.maxOf { it.count }.coerceAtLeast(1)
    val maxBarHeight = 60.dp

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            "7-Day Activity",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = TextPrimary
        )
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            days.forEach { day ->
                val barH = (day.count.toFloat() / maxCount * maxBarHeight.value)
                    .dp.coerceAtLeast(if (day.count > 0) 8.dp else 4.dp)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (day.count > 0) {
                        Text(
                            day.count.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = GreenAccent
                        )
                        Spacer(Modifier.height(2.dp))
                    } else {
                        Spacer(Modifier.height(14.dp))
                    }
                    Box(
                        Modifier
                            .width(26.dp)
                            .height(barH)
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(
                                if (day.count > 0) GreenAccent.copy(alpha = 0.75f)
                                else Color.White.copy(alpha = 0.04f)
                            )
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (day.label == "Today") "Now" else day.label.take(3),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = if (day.label == "Today") GreenAccent else TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun BestThisWeek(product: ScanEntity) {
    val color = scoreColor(product.score)
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            "Best This Week",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = TextPrimary
        )
        Spacer(Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f))
                    .border(1.5.dp, color.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    product.score.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = color
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    product.productName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary,
                    maxLines = 2
                )
                product.brand?.let {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        it,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TextSecondary
                    )
                }
            }
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "Top Pick",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = color
                )
            }
        }
    }
}
