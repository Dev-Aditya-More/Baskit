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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
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
import com.aditya1875.baskit.home.presentation.components.RecentScanItem
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
    var barcode by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBg)
    ) {
        // Enhanced top radial glow for more depth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GreenAccent.copy(alpha = 0.12f),
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
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(28.dp))

            // Header Section
            AnimatedVisibility(
                visible, 
                enter = fadeIn(tween(600)) + expandVertically(expandFrom = Alignment.Top)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(10.dp)
                                .background(GreenAccent, CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "baskit",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-1.5).sp,
                                color = TextPrimary
                            )
                        )
                    }
                    Text(
                        "Know what's in your basket",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 22.dp, top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(36.dp))

            // Lookup Card - Improved shadow and internal spacing to prevent "cut off" feeling
            AnimatedVisibility(
                visible, 
                enter = fadeIn(tween(600, 200)) + slideInVertically(initialOffsetY = { 40 })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 16.dp, 
                            shape = RoundedCornerShape(28.dp), 
                            ambientColor = Color.Black.copy(0.4f),
                            spotColor = Color.Black.copy(0.2f)
                        )
                        .background(CardBg, RoundedCornerShape(28.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(28.dp))
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        "Lookup a product",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    // Search Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardBg2, RoundedCornerShape(18.dp))
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = null,
                            tint = GreenAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
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
                            decorationBox = { inner ->
                                if (barcode.isEmpty()) {
                                    Text(
                                        "Enter barcode number",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextSecondary.copy(alpha = 0.5f)
                                    )
                                }
                                inner()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 14.dp)
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
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
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

            // Recent Scans Section
            AnimatedVisibility(
                visible = visible && recentScans.isNotEmpty(),
                enter = fadeIn(tween(600, 400)) + slideInVertically(initialOffsetY = { 40 })
            ) {
                Column {
                    Spacer(Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Recent Scans",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
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
                                .clickable {
                                    navController.navigate(Screen.History.route)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

            // Empty State
            AnimatedVisibility(
                visible = visible && recentScans.isEmpty(),
                enter = fadeIn(tween(800, 500))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No scans yet", 
                        color = TextPrimary.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        "Scan or search a product to get started",
                        color = TextSecondary.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            // Crucial spacing to ensure bottom items aren't hidden by the floating bottom bar
            Spacer(Modifier.height(140.dp))
        }
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
