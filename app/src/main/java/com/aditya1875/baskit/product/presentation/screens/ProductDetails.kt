package com.aditya1875.baskit.product.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aditya1875.baskit.history.data.local.ScanEntity
import com.aditya1875.baskit.history.presentation.viewmodel.ScanHistoryViewModel
import com.aditya1875.baskit.product.data.local.Product
import com.aditya1875.baskit.product.presentation.components.HeadsUpRow
import com.aditya1875.baskit.product.presentation.components.IngChip
import com.aditya1875.baskit.product.presentation.components.InsightRow
import com.aditya1875.baskit.product.presentation.components.ScoreRing
import com.aditya1875.baskit.product.presentation.components.SectionLabel
import com.aditya1875.baskit.product.presentation.components.VitalTile
import com.aditya1875.baskit.product.presentation.utils.buildInsights
import com.aditya1875.baskit.product.presentation.utils.buildVerdict
import com.aditya1875.baskit.product.presentation.utils.calculateScore
import com.aditya1875.baskit.product.presentation.utils.detectFlaggedIngredients
import com.aditya1875.baskit.product.presentation.utils.fmt
import com.aditya1875.baskit.product.presentation.utils.scoreColor
import com.aditya1875.baskit.product.presentation.utils.scoreLabel
import com.aditya1875.baskit.ui.theme.CardBg
import com.aditya1875.baskit.ui.theme.DangerRed
import com.aditya1875.baskit.ui.theme.DetailBg
import com.aditya1875.baskit.ui.theme.GreenAccent
import com.aditya1875.baskit.ui.theme.GreenHi
import com.aditya1875.baskit.ui.theme.TextHi
import com.aditya1875.baskit.ui.theme.TextMid
import com.aditya1875.baskit.ui.theme.TextPrimary
import com.aditya1875.baskit.ui.theme.TextSecondary
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductDetailScreen(
    viewModel: ProductViewModel,
    historyViewModel: ScanHistoryViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val product by viewModel.currentProduct.collectAsStateWithLifecycle()

    LaunchedEffect(product) {
        product?.let { p ->
            val score = calculateScore(p)
            historyViewModel.save(
                ScanEntity(
                    barcode = p.code ?: return@let,
                    productName = listOfNotNull(
                        p.productName?.takeIf { it.isNotBlank() },
                        p.genericName?.takeIf { it.isNotBlank() },
                        p.code
                    ).first(),
                    brand = p.brand?.split(",")?.firstOrNull()?.trim(),
                    imageFrontUrl = p.imageFrontUrl,
                    score = score,
                    calories = p.nutriments?.energyKcal100g?.toInt()
                )
            )
        }
    }

    Box(Modifier
        .fillMaxSize()
        .background(DetailBg)) {
        product?.let { p ->
            ProductDetailsContent(product = p, onBack = onBack)
        } ?: run {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = GreenHi, strokeWidth = 2.dp)
            }
        }
    }
}

@Composable
fun ProductDetailsContent(product: Product, onBack: () -> Unit) {
    val displayName = listOfNotNull(
        product.productName?.takeIf { it.isNotBlank() },
        product.genericName?.takeIf { it.isNotBlank() },
        product.code
    ).first().trim().replaceFirstChar { it.uppercase() }

    val score = remember(product) { calculateScore(product) }
    val scoreColor = scoreColor(score)
    val scoreLbl = scoreLabel(score)
    val verdict = remember(product) { buildVerdict(product) }
    val insights = remember(product) { buildInsights(product) }
    val flagged = remember(product) { detectFlaggedIngredients(product.ingredientsText) }
    val allIng = remember(product) {
        product.ingredientsText
            ?.split(",", ";")
            ?.map { it.trim().replaceFirstChar { c -> c.uppercase() } }
            ?.filter { it.isNotBlank() }
            ?.take(14) ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(DetailBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
        ) {
            if (!product.imageFrontUrl.isNullOrBlank()) {
                AsyncImage(
                    model = product.imageFrontUrl,
                    contentDescription = displayName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF1A3A28), Color(0xFF0A1A10)))
                        ),
                    Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.ImageNotSupported, null,
                        Modifier.size(72.dp), tint = GreenHi.copy(0.25f)
                    )
                }
            }
            // Bottom gradient
            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Transparent,
                            1f to DetailBg
                        )
                    )
            )
            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(10.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.45f))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, "Back",
                    tint = Color.White, modifier = Modifier.size(20.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardBg)
                .padding(22.dp)
        ) {
            Column {
                Text(
                    verdict.label.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.8.sp,
                        fontSize = 10.sp
                    ),
                    color = verdict.color
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    displayName,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp,
                        lineHeight = 38.sp
                    ),
                    color = TextHi
                )
                Spacer(Modifier.height(8.dp))
                val sub = buildList {
                    product.brand?.split(",")?.firstOrNull()?.trim()
                        ?.takeIf { it.isNotBlank() }?.let { add(it) }
                    product.quantity?.takeIf { it.isNotBlank() }?.let { add(it) }
                }.joinToString(" • ")
                if (sub.isNotBlank()) {
                    Text(sub, style = MaterialTheme.typography.bodyMedium, color = TextMid)
                }
            }
        }

        Spacer(Modifier.height((-4).dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardBg)
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ScoreRing(score = score, color = scoreColor, size = 120.dp)
                Spacer(Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(scoreColor.copy(alpha = 0.15f))
                        .padding(horizontal = 18.dp, vertical = 6.dp)
                ) {
                    Text(
                        scoreLbl.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        ),
                        color = scoreColor
                    )
                }
            }
        }

        product.nutriments?.let { n ->
            Spacer(Modifier.height(28.dp))
            Text(
                "Nutritional Vitals",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextHi          // white, not gray
                )
            )
            Spacer(Modifier.height(14.dp))

            val primary = buildList {
                n.energyKcal100g?.let { add(Triple("CALORIES", "${it.toInt()}", "kcal")) }
                n.fat100g?.let { add(Triple("FAT", fmt(it), "g")) }
                n.proteins100g?.let { add(Triple("PROTEIN", fmt(it), "g")) }
                n.carbohydrates100g?.let { add(Triple("CARBS", fmt(it), "g")) }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                primary.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { (lbl, v, u) ->
                            VitalTile(lbl, v, u, Modifier.weight(1f))
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            // Secondary row: sugar, sat fat, salt, fibre
            val secondary = buildList {
                n.sugars100g?.let { add(Triple("SUGARS", fmt(it), "g")) }
                n.saturatedFat100g?.let { add(Triple("SAT. FAT", fmt(it), "g")) }
                n.salt100g?.let { add(Triple("SALT", fmt(it), "g")) }
                n.fiber100g?.let { add(Triple("FIBRE", fmt(it), "g")) }
            }
            if (secondary.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(secondary) { (lbl, v, u) ->
                        VitalTile(lbl, v, u, Modifier.width(104.dp))
                    }
                }
            }
        }

        // ── Ingredients card ───────────────────────────────────────────────
        if (allIng.isNotEmpty()) {
            Spacer(Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBg)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        Text(
                            "Ingredients",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextHi
                            )
                        )
                        if (flagged.isNotEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Warning, null,
                                    Modifier.size(13.dp), tint = DangerRed
                                )
                                Text(
                                    "${flagged.size} Flagged",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = DangerRed
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allIng.forEach { ing ->
                            val isFlagged = flagged.any { ing.contains(it, ignoreCase = true) }
                            IngChip(ing, isFlagged)
                        }
                    }

                    if (flagged.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        flagged.forEach { f ->
                            HeadsUpRow(f)
                            Spacer(Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        if (insights.isNotEmpty()) {
            Spacer(Modifier.height(28.dp))
            Text(
                "Health Insights",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextHi
                )
            )
            Spacer(Modifier.height(12.dp))
            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                insights.forEach { InsightRow(it) }
            }
        }

        // ── Allergens ──────────────────────────────────────────────────────
        product.allergens?.takeIf { it.isNotBlank() }?.let { raw ->
            val clean = raw.split(",")
                .map {
                    it.replace("en:", "").replace("-", " ").trim()
                        .replaceFirstChar { c -> c.uppercase() }
                }
                .filter { it.isNotBlank() }
            if (clean.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                Text(
                    "Allergens",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextHi
                    )
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clean) { allergen ->
                        Row(
                            Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DangerRed.copy(0.1f))
                                .border(1.dp, DangerRed.copy(0.25f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Warning, null,
                                Modifier.size(11.dp), DangerRed
                            )
                            Text(
                                allergen,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = DangerRed
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(100.dp))
    }
}