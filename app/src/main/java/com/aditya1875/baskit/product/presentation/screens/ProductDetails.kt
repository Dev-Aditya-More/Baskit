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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aditya1875.baskit.history.data.local.ScanEntity
import com.aditya1875.baskit.history.presentation.viewmodel.ScanHistoryViewModel
import com.aditya1875.baskit.product.data.local.Product
import com.aditya1875.baskit.product.presentation.components.GradeBadge
import com.aditya1875.baskit.product.presentation.components.HeadsUpRow
import com.aditya1875.baskit.product.presentation.components.IngChip
import com.aditya1875.baskit.product.presentation.components.InsightRow
import com.aditya1875.baskit.product.presentation.components.MacroBar
import com.aditya1875.baskit.product.presentation.components.NutrientBullet
import com.aditya1875.baskit.product.presentation.components.NutrientLevel
import com.aditya1875.baskit.product.presentation.components.ScoreRing
import com.aditya1875.baskit.product.presentation.utils.buildInsights
import com.aditya1875.baskit.product.presentation.utils.buildVerdict
import com.aditya1875.baskit.product.presentation.utils.calculateScore
import com.aditya1875.baskit.product.presentation.utils.detectFlaggedIngredients
import com.aditya1875.baskit.product.presentation.utils.fmt
import com.aditya1875.baskit.product.presentation.utils.gradeColor
import com.aditya1875.baskit.product.presentation.utils.novaColor
import com.aditya1875.baskit.product.presentation.utils.scoreColor
import com.aditya1875.baskit.product.presentation.utils.scoreLabel
import com.aditya1875.baskit.product.presentation.utils.verdictTagline
import com.aditya1875.baskit.ui.theme.CardBg
import com.aditya1875.baskit.ui.theme.CardBg2
import com.aditya1875.baskit.ui.theme.DangerRed
import com.aditya1875.baskit.ui.theme.DetailBg
import com.aditya1875.baskit.ui.theme.GreenAccent
import com.aditya1875.baskit.ui.theme.GreenHi
import com.aditya1875.baskit.ui.theme.TextHi
import com.aditya1875.baskit.ui.theme.TextMid
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

    Box(
        Modifier
            .fillMaxSize()
            .background(DetailBg)
    ) {
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
    val sColor = scoreColor(score)
    val sLabel = scoreLabel(score)
    val verdict = remember(product) { buildVerdict(product) }
    val tagline = remember(score) { verdictTagline(score) }
    val insights = remember(product) { buildInsights(product) }
    val flagged = remember(product) { detectFlaggedIngredients(product.ingredientsText) }
    val allIng = remember(product) {
        product.ingredientsText
            ?.split(",", ";")
            ?.map { it.trim().replaceFirstChar { c -> c.uppercase() } }
            ?.filter { it.isNotBlank() }
            ?.take(14) ?: emptyList()
    }
    val brand = product.brand?.split(",")?.firstOrNull()?.trim()?.takeIf { it.isNotBlank() }
    val category = product.categories?.split(",")?.lastOrNull()
        ?.replace("en:", "")?.replace("-", " ")?.trim()
        ?.replaceFirstChar { it.uppercase() }
        ?.takeIf { it.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(DetailBg)
    ) {
        HeroImage(
            imageUrl = product.imageFrontUrl,
            verdictColor = verdict.color,
            onBack = onBack
        )

        // Title block
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Verdict pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(verdict.color.copy(alpha = 0.18f))
                    .border(1.dp, verdict.color.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    Modifier.size(7.dp).clip(CircleShape).background(verdict.color)
                )
                Text(
                    verdict.label.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.6.sp,
                        fontSize = 10.sp
                    ),
                    color = verdict.color
                )
            }
            Spacer(Modifier.height(14.dp))
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
            val sub = listOfNotNull(brand, product.quantity?.takeIf { it.isNotBlank() })
                .joinToString(" • ")
            if (sub.isNotBlank()) {
                Text(sub, style = MaterialTheme.typography.bodyMedium, color = TextMid)
            }
            if (category != null) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Outlined.Category, null,
                        tint = TextSecondary, modifier = Modifier.size(12.dp)
                    )
                    Text(
                        category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp
                        ),
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Hero score card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.linearGradient(
                        listOf(CardBg, CardBg2)
                    )
                )
                .border(1.dp, sColor.copy(alpha = 0.18f), RoundedCornerShape(28.dp))
                .padding(22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ScoreRing(score = score, color = sColor, size = 124.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "VITALITY SCORE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.3.sp,
                            fontSize = 9.sp
                        ),
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        sLabel,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = sColor
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        tagline,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                        color = TextMid
                    )
                }
            }

            // Grade badges row
            val nutri = product.nutritionGrade?.uppercase()
            val nova = product.novaGroup
            val eco = product.ecoscoreGrade?.uppercase()
            if (!nutri.isNullOrBlank() || nova != null || !eco.isNullOrBlank()) {
                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (!nutri.isNullOrBlank()) {
                        GradeBadge(
                            "Nutri-Score",
                            nutri,
                            gradeColor(nutri),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (nova != null) {
                        GradeBadge(
                            "NOVA",
                            nova.toString(),
                            novaColor(nova),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (!eco.isNullOrBlank()) {
                        GradeBadge(
                            "Eco-Score",
                            eco,
                            gradeColor(eco),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Macros card
        product.nutriments?.let { n ->
            val hasMacros = listOfNotNull(
                n.energyKcal100g, n.proteins100g, n.carbohydrates100g, n.fat100g
            ).isNotEmpty()
            if (hasMacros) {
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(CardBg)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "Macros",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                color = TextHi
                            )
                            Text(
                                "Per 100g · % of daily reference",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp
                                ),
                                color = TextSecondary
                            )
                        }
                        n.energyKcal100g?.let { kcal ->
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${kcal.toInt()}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = (-0.5).sp
                                    ),
                                    color = TextHi
                                )
                                Text(
                                    "kcal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    val macroColor = Color(0xFF00E676)
                    val carbColor = Color(0xFF4FC3F7)
                    val fatColor = Color(0xFFFFAA00)
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        n.proteins100g?.let {
                            MacroBar("Protein", it, 50.0, macroColor)
                        }
                        n.carbohydrates100g?.let {
                            MacroBar("Carbs", it, 275.0, carbColor)
                        }
                        n.fat100g?.let {
                            MacroBar("Fat", it, 78.0, fatColor)
                        }
                    }
                }
            }

            // Detailed nutrient bullets — sugars, sat fat, salt, fibre
            val bullets = buildList {
                n.sugars100g?.let { v ->
                    val (lvl, lbl) = when {
                        v > 22.5 -> NutrientLevel.HIGH to "High"
                        v > 5 -> NutrientLevel.MID to "Medium"
                        else -> NutrientLevel.LOW to "Low"
                    }
                    add(Quad("Sugars", "${fmt(v)}g", lvl, lbl))
                }
                n.saturatedFat100g?.let { v ->
                    val (lvl, lbl) = when {
                        v > 5 -> NutrientLevel.HIGH to "High"
                        v > 1.5 -> NutrientLevel.MID to "Medium"
                        else -> NutrientLevel.LOW to "Low"
                    }
                    add(Quad("Saturated Fat", "${fmt(v)}g", lvl, lbl))
                }
                n.salt100g?.let { v ->
                    val (lvl, lbl) = when {
                        v > 1.5 -> NutrientLevel.HIGH to "High"
                        v > 0.3 -> NutrientLevel.MID to "Medium"
                        else -> NutrientLevel.LOW to "Low"
                    }
                    add(Quad("Salt", "${fmt(v)}g", lvl, lbl))
                }
                n.fiber100g?.let { v ->
                    val (lvl, lbl) = when {
                        v >= 6 -> NutrientLevel.LOW to "Excellent"
                        v >= 3 -> NutrientLevel.LOW to "Good"
                        else -> NutrientLevel.MID to "Low"
                    }
                    add(Quad("Fibre", "${fmt(v)}g", lvl, lbl))
                }
            }
            if (bullets.isNotEmpty()) {
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(CardBg)
                        .padding(20.dp)
                ) {
                    Text(
                        "Nutrient Levels",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = TextHi
                    )
                    Text(
                        "Traffic-light guidance per 100g",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        bullets.forEach { q ->
                            NutrientBullet(
                                label = q.label,
                                valueText = q.value,
                                level = q.level,
                                levelLabel = q.lvlLabel,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // Ingredients
        if (allIng.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBg)
                    .padding(20.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Ingredients",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = TextHi
                        )
                        Text(
                            "${allIng.size} listed",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextSecondary
                        )
                    }
                    if (flagged.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(DangerRed.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Warning, null,
                                Modifier.size(13.dp), tint = DangerRed
                            )
                            Text(
                                "${flagged.size} flagged",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = DangerRed
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
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

        // Health insights
        if (insights.isNotEmpty()) {
            Spacer(Modifier.height(28.dp))
            SectionHeader(
                title = "Health Insights",
                subtitle = "What this product means for you"
            )
            Spacer(Modifier.height(12.dp))
            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                insights.forEach { InsightRow(it) }
            }
        }

        // Allergens
        product.allergens?.takeIf { it.isNotBlank() }?.let { raw ->
            val clean = raw.split(",")
                .map {
                    it.replace("en:", "").replace("-", " ").trim()
                        .replaceFirstChar { c -> c.uppercase() }
                }
                .filter { it.isNotBlank() }
            if (clean.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                SectionHeader(title = "Allergens", subtitle = "Contains the following")
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clean) { allergen ->
                        Row(
                            Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(DangerRed.copy(alpha = 0.1f))
                                .border(1.dp, DangerRed.copy(0.25f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Warning, null,
                                Modifier.size(12.dp), DangerRed
                            )
                            Text(
                                allergen,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = DangerRed
                            )
                        }
                    }
                }
            }
        }

        // Labels / certifications
        product.labels?.takeIf { it.isNotBlank() }?.let { raw ->
            val labels = raw.split(",")
                .map {
                    it.replace("en:", "").replace("-", " ").trim()
                        .replaceFirstChar { c -> c.uppercase() }
                }
                .filter { it.isNotBlank() }
                .take(8)
            if (labels.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                SectionHeader(title = "Certifications", subtitle = "Verified labels")
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(labels) { lbl ->
                        Row(
                            Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GreenAccent.copy(alpha = 0.1f))
                                .border(1.dp, GreenAccent.copy(0.25f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Verified, null,
                                Modifier.size(12.dp), tint = GreenAccent
                            )
                            Text(
                                lbl,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = GreenAccent
                            )
                        }
                    }
                }
            }
        }

        // Footer barcode reference
        product.code?.takeIf { it.isNotBlank() }?.let { code ->
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Outlined.QrCode2, null,
                    Modifier.size(14.dp), tint = TextSecondary
                )
                Text(
                    "Barcode $code",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TextSecondary
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "Source · Open Food Facts",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TextSecondary
                )
            }
        }

        Spacer(Modifier.height(120.dp))
    }
}

@Composable
private fun HeroImage(
    imageUrl: String?,
    verdictColor: Color,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
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
        // Top-down dim
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.35f),
                        0.3f to Color.Transparent,
                        0.6f to Color.Transparent,
                        1f to DetailBg
                    )
                )
        )
        // Verdict glow accent
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(verdictColor.copy(alpha = 0.18f), Color.Transparent),
                        radius = 700f
                    )
                )
        )
        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.5f))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, "Back",
                    tint = Color.White, modifier = Modifier.size(20.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.5f))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.BookmarkBorder, null,
                    tint = Color.White, modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.3).sp
            ),
            color = TextHi
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = TextSecondary
        )
    }
}

private data class Quad(
    val label: String,
    val value: String,
    val level: NutrientLevel,
    val lvlLabel: String
)
