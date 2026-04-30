package com.aditya1875.baskit.history.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aditya1875.baskit.history.data.local.ScanEntity
import com.aditya1875.baskit.history.data.repository.HistoryFilter
import com.aditya1875.baskit.history.presentation.components.DateSectionHeader
import com.aditya1875.baskit.history.presentation.components.FilterPill
import com.aditya1875.baskit.history.presentation.components.HistoryScanCard
import com.aditya1875.baskit.history.presentation.utils.groupByDate
import com.aditya1875.baskit.history.presentation.viewmodel.ScanHistoryViewModel
import com.aditya1875.baskit.ui.theme.GreenAccent
import com.aditya1875.baskit.ui.theme.HistBg
import com.aditya1875.baskit.ui.theme.TextPrimary
import com.aditya1875.baskit.ui.theme.TextSecondary
import org.koin.androidx.compose.koinViewModel
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: ScanHistoryViewModel = koinViewModel(),
    onScanClick: (ScanEntity) -> Unit = {}
) {
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val scans by viewModel.scans.collectAsStateWithLifecycle()
    val monthCount by viewModel.monthCount.collectAsStateWithLifecycle()

    val grouped = remember(scans) { groupByDate(scans) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HistBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2A2A2A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, null,
                            tint = TextSecondary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        "BASKIT",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 3.sp,
                            color = TextPrimary
                        )
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Outlined.BarChart, null,
                        tint = GreenAccent, modifier = Modifier.size(24.dp))
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "History",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-1).sp,
                            color = TextPrimary
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Tracking $monthCount nutritional scans this month.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterPill("Recent", filter == HistoryFilter.RECENT) {
                        viewModel.setFilter(HistoryFilter.RECENT)
                    }
                    FilterPill("Healthy", filter == HistoryFilter.HEALTHY) {
                        viewModel.setFilter(HistoryFilter.HEALTHY)
                    }
                    FilterPill("Unhealthy", filter == HistoryFilter.UNHEALTHY) {
                        viewModel.setFilter(HistoryFilter.UNHEALTHY)
                    }
                }
            }

            if (grouped.isEmpty()) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(top = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No scans here yet",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            grouped.forEach { (dateLabel, items) ->
                item {
                    DateSectionHeader(label = dateLabel)
                }
                items(items, key = { it.id }) { scan ->
                    HistoryScanCard(scan = scan, onClick = { onScanClick(scan) })
                    Spacer(Modifier.height(10.dp))
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}