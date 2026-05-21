package com.aditya1875.baskit.home.presentation.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.aditya1875.baskit.core.navigation.Screen
import com.aditya1875.baskit.history.presentation.viewmodel.ScanHistoryViewModel
import com.aditya1875.baskit.home.presentation.components.HealthAlertCard
import com.aditya1875.baskit.home.presentation.components.RecentScanItem
import com.aditya1875.baskit.home.presentation.components.StatTile
import com.aditya1875.baskit.home.presentation.components.SuggestionCard
import com.aditya1875.baskit.home.presentation.components.VitalitySummary
import com.aditya1875.baskit.ui.theme.CardBg
import com.aditya1875.baskit.ui.theme.CardBg2
import com.aditya1875.baskit.ui.theme.GreenAccent
import com.aditya1875.baskit.ui.theme.HomeBg
import com.aditya1875.baskit.ui.theme.TextPrimary
import com.aditya1875.baskit.ui.theme.TextSecondary
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onFetchBarcode: (String) -> Unit,
    historyViewModel: ScanHistoryViewModel = koinViewModel()
) {
    val recentScans by historyViewModel.recentScans.collectAsStateWithLifecycle()
    val weekly by historyViewModel.weeklyStats.collectAsStateWithLifecycle()
    var barcode by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var lookupExpanded by remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBg)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .height(420.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GreenAccent.copy(alpha = 0.14f),
                            Color.Transparent
                        ),
                        center = Offset(500f, 0f),
                        radius = 800f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Top app bar ────────────────────────────────────────────────
            AnimatedVisibility(
                visible,
                enter = fadeIn(tween(500))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CardBg)
                            .border(1.dp, Color.White.copy(alpha = 0.06f), CircleShape)
                            .clickable {
                                navController.navigate(Screen.Profile.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Person, null,
                            tint = TextSecondary, modifier = Modifier.size(20.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(8.dp).clip(CircleShape).background(GreenAccent)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "BASKIT",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 3.sp,
                                color = TextPrimary
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CardBg)
                            .border(1.dp, Color.White.copy(alpha = 0.06f), CircleShape)
                            .clickable {
                                navController.navigate(Screen.Insights.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ShowChart, null,
                            tint = GreenAccent, modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Vitality summary ───────────────────────────────────────────
            AnimatedVisibility(
                visible,
                enter = fadeIn(tween(600, 100)) + slideInVertically(initialOffsetY = { 30 })
            ) {
                VitalitySummary(score = weekly.avgScore, scanCount = weekly.totalScans)
            }

            Spacer(Modifier.height(20.dp))

            // ── Health alert (only if any unhealthy this week) ─────────────
            if (weekly.unhealthyCount > 0) {
                AnimatedVisibility(
                    visible,
                    enter = fadeIn(tween(600, 200)) + slideInVertically(initialOffsetY = { 30 })
                ) {
                    HealthAlertCard(unhealthyCount = weekly.unhealthyCount)
                }
                Spacer(Modifier.height(14.dp))
            }

            // ── Stats tiles ────────────────────────────────────────────────
            AnimatedVisibility(
                visible,
                enter = fadeIn(tween(600, 250)) + slideInVertically(initialOffsetY = { 30 })
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatTile(
                        icon = Icons.Outlined.FavoriteBorder,
                        iconTint = GreenAccent,
                        value = weekly.healthyCount.toString(),
                        label = "Healthy Picks",
                        modifier = Modifier.weight(1f)
                    )
                    StatTile(
                        icon = Icons.Outlined.LocalFireDepartment,
                        iconTint = Color(0xFFFF8A3D),
                        value = formatCalories(weekly.totalCalories),
                        label = "Calories Scanned",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Lookup (compact, expandable) ───────────────────────────────
            AnimatedVisibility(
                visible,
                enter = fadeIn(tween(600, 300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardBg)
                        .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { lookupExpanded = !lookupExpanded }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GreenAccent.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Search, null,
                                tint = GreenAccent, modifier = Modifier.size(18.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Lookup by barcode",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextPrimary
                            )
                            Text(
                                if (lookupExpanded) "Type the digits below" else "Tap to enter manually",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = TextSecondary
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = lookupExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                start = 16.dp, end = 16.dp, bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(CardBg2)
                                    .padding(horizontal = 14.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = barcode,
                                    onValueChange = { barcode = it.filter { c -> c.isDigit() } },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Search
                                    ),
                                    keyboardActions = KeyboardActions(onSearch = {
                                        focusManager.clearFocus()
                                        if (barcode.isNotBlank()) onFetchBarcode(barcode)
                                    }),
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextPrimary
                                    ),
                                    cursorBrush = SolidColor(GreenAccent),
                                    decorationBox = { inner ->
                                        if (barcode.isEmpty()) {
                                            Text(
                                                "e.g. 8901058868893",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = TextSecondary.copy(alpha = 0.5f)
                                            )
                                        }
                                        inner()
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 12.dp)
                                )
                            }
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    if (barcode.isNotBlank()) onFetchBarcode(barcode)
                                },
                                enabled = barcode.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GreenAccent,
                                    contentColor = Color(0xFF080F09),
                                    disabledContainerColor = GreenAccent.copy(alpha = 0.15f),
                                    disabledContentColor = Color.White.copy(alpha = 0.2f)
                                )
                            ) {
                                Text(
                                    "Fetch Product",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // ── Recent scans ───────────────────────────────────────────────
            AnimatedVisibility(
                visible = visible && recentScans.isNotEmpty(),
                enter = fadeIn(tween(600, 400)) + slideInVertically(initialOffsetY = { 40 })
            ) {
                Column {
                    Spacer(Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Recent Scans",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            "View All",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GreenAccent,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { navController.navigate(Screen.History.route) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        recentScans.forEach { scan ->
                            RecentScanItem(
                                scan = scan,
                                onClick = {
                                    navController.navigate(Screen.ProductGraph.pass(scan.barcode))
                                }
                            )
                        }
                    }
                }
            }

            // ── Smart suggestion ───────────────────────────────────────────
            val suggestion = remember(weekly) { pickSuggestion(weekly.unhealthyCount, weekly.totalScans) }
            if (suggestion != null) {
                Spacer(Modifier.height(28.dp))
                AnimatedVisibility(
                    visible,
                    enter = fadeIn(tween(700, 500)) + slideInVertically(initialOffsetY = { 30 })
                ) {
                    SuggestionCard(title = suggestion.first, body = suggestion.second)
                }
            }

            // Empty state
            AnimatedVisibility(
                visible = visible && recentScans.isEmpty(),
                enter = fadeIn(tween(800, 500))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No scans yet",
                        color = TextPrimary.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        "Scan or search a product to get started",
                        color = TextSecondary.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

private fun formatCalories(total: Int): String =
    if (total >= 1000) "%.1fk".format(total / 1000.0) else total.toString()

private fun pickSuggestion(unhealthy: Int, total: Int): Pair<String, String>? {
    if (total == 0) return "Get Started" to "Scan your first product to unlock weekly insights."
    return when {
        unhealthy >= 3 -> "Smart Swap" to "Try whole-food alternatives to lift your weekly score."
        unhealthy >= 1 -> "Stay Mindful" to "Read labels for sugar and sodium on packaged picks."
        else -> "Keep Going" to "You're stacking healthy choices — great momentum this week."
    }
}

fun relativeTime(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    val mins = diff / 60_000
    val hours = mins / 60
    val days = hours / 24
    return when {
        mins < 2 -> "Just now"
        mins < 60 -> "Scanned ${mins}m ago"
        hours < 24 -> "Scanned ${hours}h ago"
        days == 1L -> "Scanned Yesterday"
        else -> "Scanned $days days ago"
    }
}
