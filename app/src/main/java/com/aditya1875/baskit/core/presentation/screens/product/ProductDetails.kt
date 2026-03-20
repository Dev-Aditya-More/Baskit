package com.aditya1875.baskit.core.presentation.screens.product

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aditya1875.baskit.core.data.local.Nutriments
import com.aditya1875.baskit.core.data.local.Product
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel

// ─── Screen shell ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductViewModel,
    onBack: () -> Unit
) {
    val product by viewModel.currentProduct.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        val p = product
        if (p != null) {
            ProductDetailsContent(
                product = p,
                modifier = Modifier.padding(top = padding.calculateTopPadding())
            )
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No product loaded",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                )
            }
        }
    }
}


@Composable
fun ProductDetailsContent(product: Product, modifier: Modifier = Modifier) {
    val displayName = listOfNotNull(
        product.productName?.takeIf { it.isNotBlank() },
        product.genericName?.takeIf { it.isNotBlank() },
        product.code
    ).first().trim().replaceFirstChar { it.uppercase() }

    val insights = rememberHealthInsights(product)
    val verdict = rememberVerdict(product)
    val scrollState = rememberScrollState()

    // Track which sections have scrolled into view for staggered animation
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        HeroSection(product = product, displayName = displayName)

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            // ── Verdict pill — the one quick takeaway ─────────────────────
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(400)) + slideInVertically(
                    tween(400), initialOffsetY = { 30 }
                )
            ) {
                VerdictCard(verdict = verdict)
            }

            Spacer(Modifier.height(24.dp))

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(500, delayMillis = 80))
            ) {
                Column {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    product.brand?.takeIf { it.isNotBlank() }?.let {
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = it.split(",").first().trim(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    val tags = buildList {
                        product.quantity?.takeIf { it.isNotBlank() }?.let { add(it) }
                        product.novaGroup?.let { add("NOVA $it") }
                        product.packaging?.split(",")?.firstOrNull()
                            ?.trim()?.takeIf { it.isNotBlank() }?.let { add(it) }
                    }
                    if (tags.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            tags.take(3).forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            SectionSpacer()
            SectionHeader("Scores")
            Spacer(Modifier.height(12.dp))
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(500, delayMillis = 120))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ScoreCard(
                        label = "Nutri-Score",
                        grade = product.nutritionGrade?.uppercase(),
                        info = nutriScoreInfo(product.nutritionGrade),
                        modifier = Modifier.weight(1f)
                    )
                    ScoreCard(
                        label = "Eco-Score",
                        grade = product.ecoscoreGrade?.uppercase(),
                        info = ecoScoreInfo(product.ecoscoreGrade),
                        modifier = Modifier.weight(1f)
                    )
                    product.novaGroup?.let { nova ->
                        ScoreCard(
                            label = "NOVA",
                            grade = nova.toString(),
                            info = novaInfo(nova),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (insights.isNotEmpty()) {
                SectionSpacer()
                SectionHeader("Health insights")
                Spacer(Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    insights.forEachIndexed { index, insight ->
                        AnimatedVisibility(
                            visible = contentVisible,
                            enter = fadeIn(tween(400, delayMillis = 160 + index * 40)) +
                                    slideInHorizontally(
                                        tween(400, delayMillis = 160 + index * 40),
                                        initialOffsetX = { -20 }
                                    )
                        ) {
                            InsightCard(insight)
                        }
                    }
                }
            }

            product.nutriments?.let { n ->
                SectionSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    SectionHeader("Nutrition facts")
                    Text(
                        "per 100g",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                NutritionTable(n)
            }

            product.allergens?.takeIf { it.isNotBlank() }?.let { raw ->
                val clean = raw.split(",")
                    .map { it.replace("en:", "").replace("-", " ").trim() }
                    .filter { it.isNotBlank() }
                if (clean.isNotEmpty()) {
                    SectionSpacer()
                    SectionHeader("Allergens")
                    Spacer(Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        clean.forEach { allergen ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    allergen.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            product.additivesTags?.takeIf { it.isNotEmpty() }?.let { additives ->
                SectionSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("Additives")
                    val countColor = if (additives.size > 3)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(countColor.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "${additives.size} found",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = countColor
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    additives.joinToString("  ·  ") {
                        it.replace("en:", "").uppercase()
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
                    lineHeight = 20.sp
                )
            }

            // ── Ingredients ───────────────────────────────────────────────
            product.ingredientsText?.takeIf { it.isNotBlank() }?.let { ing ->
                SectionSpacer()
                SectionHeader("Ingredients")
                Spacer(Modifier.height(10.dp))
                Text(
                    text = ing,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                    lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(64.dp))
        }
    }
}


@Composable
private fun HeroSection(product: Product, displayName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.07f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        if (!product.imageFrontUrl.isNullOrBlank()) {
            AsyncImage(
                model = product.imageFrontUrl,
                contentDescription = displayName,
                modifier = Modifier
                    .fillMaxHeight(0.88f)
                    .padding(horizontal = 32.dp)
                    .shadow(
                        elevation = 32.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        spotColor = Color.Black.copy(alpha = 0.25f)
                    ),
                contentScale = ContentScale.Fit
            )
        } else {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.ImageNotSupported,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.6f to Color.Transparent,
                        1f to MaterialTheme.colorScheme.background
                    )
                )
        )
    }
}

data class Verdict(
    val emoji: String,
    val headline: String,
    val sub: String,
    val color: Color
)

@Composable
fun rememberVerdict(product: Product): Verdict = remember(product) {
    buildVerdict(product)
}

fun buildVerdict(product: Product): Verdict {
    // Score the product 0–100 from available signals
    var score = 50

    when (product.nutritionGrade?.lowercase()) {
        "a" -> score += 20; "b" -> score += 12; "c" -> score += 0
        "d" -> score -= 12; "e" -> score -= 20
    }
    when (product.novaGroup) {
        1 -> score += 15; 2 -> score += 8; 3 -> score -= 8; 4 -> score -= 20
    }
    product.nutriments?.sugars100g?.let { s ->
        if (s > 22.5) score -= 10 else if (s < 5) score += 5
    }
    product.nutriments?.saturatedFat100g?.let { f ->
        if (f > 5) score -= 8 else if (f < 1.5) score += 4
    }

    score = score.coerceIn(0, 100)

    return when {
        score >= 72 -> Verdict("✅", "Looks good", "A solid choice with no major concerns.", Color(0xFF1B5E20))
        score >= 52 -> Verdict("🟡", "Consume mindfully", "Some nutritional trade-offs to be aware of.", Color(0xFFF57F17))
        score >= 35 -> Verdict("⚠️", "Worth knowing", "This product has a few things to watch out for.", Color(0xFFE65100))
        else -> Verdict("🚫", "Heavy processing", "Highly processed with poor nutritional signals.", Color(0xFFB71C1C))
    }
}

@Composable
private fun VerdictCard(verdict: Verdict) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            )
            .border(
                width = 1.dp,
                color = verdict.color.copy(alpha = 0.25f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Colored left accent
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(verdict.color)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${verdict.emoji}  ${verdict.headline}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = verdict.color
            )
            Spacer(Modifier.height(2.dp))
            Text(
                verdict.sub,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun ScoreCard(
    label: String,
    grade: String?,
    info: ScoreInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        info.color.copy(alpha = 0.14f),
                        info.color.copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                1.dp,
                info.color.copy(alpha = 0.2f),
                RoundedCornerShape(20.dp)
            )
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = grade ?: "?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = info.color
        )
        Spacer(Modifier.height(2.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
            textAlign = TextAlign.Center,
            lineHeight = 13.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            info.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
            ),
            color = info.color,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun InsightCard(insight: HealthInsight) {

    val (bgColor, accentColor) = when (insight.sentiment) {
        Sentiment.GOOD -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) to
                Color(0xFF2E7D32)
        Sentiment.WARN -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f) to
                Color(0xFFF57F17)
        Sentiment.BAD -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.40f) to
                MaterialTheme.colorScheme.error
        Sentiment.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant to
                MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(start = 0.dp, top = 0.dp, end = 14.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(60.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(accentColor.copy(alpha = 0.9f), accentColor.copy(alpha = 0.3f))
                    )
                )
        )
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector = insight.icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            Text(
                insight.label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = accentColor
            )
            Spacer(Modifier.height(2.dp))
            Text(
                insight.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                lineHeight = 17.sp
            )
        }
    }
}


@Composable
private fun NutritionTable(n: Nutriments) {
    val rows = buildList {
        n.energyKcal100g?.let { add(Triple("Energy", "${it.toInt()} kcal", false)) }
        n.fat100g?.let { add(Triple("Fat", "${fmt(it)}g", false)) }
        n.saturatedFat100g?.let { add(Triple("of which saturates", "${fmt(it)}g", true)) }
        n.carbohydrates100g?.let { add(Triple("Carbohydrates", "${fmt(it)}g", false)) }
        n.sugars100g?.let { add(Triple("of which sugars", "${fmt(it)}g", true)) }
        n.fiber100g?.let { add(Triple("Fibre", "${fmt(it)}g", false)) }
        n.proteins100g?.let { add(Triple("Protein", "${fmt(it)}g", false)) }
        n.salt100g?.let { add(Triple("Salt", "${fmt(it)}g", false)) }
    }
    if (rows.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
    ) {
        rows.forEachIndexed { i, (name, value, isSub) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (isSub) 28.dp else 16.dp,
                        end = 16.dp,
                        top = if (isSub) 9.dp else 12.dp,
                        bottom = if (isSub) 9.dp else 12.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isSub)
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
                        else
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.78f)
                    )
                )
                Text(
                    value,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (!isSub) FontWeight.SemiBold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = if (!isSub) 1f else 0.6f
                        )
                    )
                )
            }
            if (i < rows.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
                )
            }
        }
    }
}


@Composable
private fun SectionSpacer() {
    Spacer(Modifier.height(28.dp))
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        modifier = Modifier.padding(bottom = 22.dp)
    )
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.2).sp
        ),
        color = MaterialTheme.colorScheme.onBackground
    )
}

private fun fmt(d: Double): String =
    if (d == d.toLong().toDouble()) d.toLong().toString()
    else "%.1f".format(d)


data class HealthInsight(
    val icon: ImageVector,
    val label: String,
    val description: String,
    val sentiment: Sentiment
)

enum class Sentiment { GOOD, WARN, BAD, NEUTRAL }

@Composable
fun rememberHealthInsights(product: Product): List<HealthInsight> =
    remember(product) { buildInsights(product) }

fun buildInsights(product: Product): List<HealthInsight> {
    val insights = mutableListOf<HealthInsight>()
    val n = product.nutriments

    n?.sugars100g?.let { sugar ->
        insights += when {
            sugar > 22.5 -> HealthInsight(
                Icons.Outlined.Warning, "High sugar",
                "${fmt(sugar)}g per 100g — well above the 22.5g high-sugar threshold.",
                Sentiment.BAD
            )
            sugar > 12 -> HealthInsight(
                Icons.Outlined.Info, "Moderate sugar",
                "${fmt(sugar)}g per 100g. Consume mindfully.",
                Sentiment.WARN
            )
            else -> HealthInsight(
                Icons.Outlined.CheckCircle, "Low sugar",
                "Only ${fmt(sugar)}g per 100g. A good choice.",
                Sentiment.GOOD
            )
        }
    }

    n?.saturatedFat100g?.let { sat ->
        insights += when {
            sat > 5 -> HealthInsight(
                Icons.Outlined.Warning, "High saturated fat",
                "${fmt(sat)}g per 100g. High intake is linked to cardiovascular risk.",
                Sentiment.BAD
            )
            sat > 1.5 -> HealthInsight(
                Icons.Outlined.Info, "Moderate saturated fat",
                "${fmt(sat)}g per 100g. Within moderate range.",
                Sentiment.WARN
            )
            else -> HealthInsight(
                Icons.Outlined.CheckCircle, "Low saturated fat",
                "Only ${fmt(sat)}g per 100g.",
                Sentiment.GOOD
            )
        }
    }

    n?.salt100g?.let { salt ->
        insights += when {
            salt > 1.5 -> HealthInsight(
                Icons.Outlined.Warning, "High salt",
                "${fmt(salt)}g per 100g. Excess sodium raises blood pressure.",
                Sentiment.BAD
            )
            salt > 0.3 -> HealthInsight(
                Icons.Outlined.Info, "Moderate salt",
                "${fmt(salt)}g per 100g.",
                Sentiment.WARN
            )
            else -> HealthInsight(
                Icons.Outlined.CheckCircle, "Low salt",
                "Only ${fmt(salt)}g per 100g.",
                Sentiment.GOOD
            )
        }
    }

    n?.proteins100g?.let { prot ->
        if (prot >= 10) insights += HealthInsight(
            Icons.Outlined.FitnessCenter, "Good protein source",
            "${fmt(prot)}g per 100g — great for muscle repair and satiety.",
            Sentiment.GOOD
        )
    }

    n?.fiber100g?.let { fiber ->
        when {
            fiber >= 6 -> insights += HealthInsight(
                Icons.Outlined.Spa, "High fibre",
                "${fmt(fiber)}g per 100g — excellent for digestive health.",
                Sentiment.GOOD
            )
            fiber >= 3 -> insights += HealthInsight(
                Icons.Outlined.Spa, "Good fibre content",
                "${fmt(fiber)}g per 100g.",
                Sentiment.GOOD
            )
        }
    }

    n?.energyKcal100g?.let { kcal ->
        if (kcal > 450) insights += HealthInsight(
            Icons.Outlined.LocalFireDepartment, "Energy dense",
            "${kcal.toInt()} kcal per 100g — watch your portion sizes.",
            Sentiment.WARN
        )
    }

    product.novaGroup?.let { nova ->
        when (nova) {
            4 -> insights += HealthInsight(
                Icons.Outlined.Science, "Ultra-processed (NOVA 4)",
                "Regular consumption is associated with poorer health outcomes.",
                Sentiment.BAD
            )
            3 -> insights += HealthInsight(
                Icons.Outlined.Science, "Processed (NOVA 3)",
                "Contains processed ingredients. Moderate consumption advised.",
                Sentiment.WARN
            )
            1, 2 -> insights += HealthInsight(
                Icons.Outlined.Eco, "Minimally processed (NOVA $nova)",
                "Low processing level — closer to whole food.",
                Sentiment.GOOD
            )
        }
    }

    product.allergens?.takeIf { it.isNotBlank() }?.let {
        insights += HealthInsight(
            Icons.Outlined.ReportProblem, "Contains allergens",
            "Review the allergens section below before consuming.",
            Sentiment.WARN
        )
    }

    return insights
}


data class ScoreInfo(val label: String, val color: Color)

fun nutriScoreInfo(grade: String?) = when (grade?.lowercase()) {
    "a" -> ScoreInfo("Excellent", Color(0xFF2E7D32))
    "b" -> ScoreInfo("Good", Color(0xFF558B2F))
    "c" -> ScoreInfo("Average", Color(0xFFF9A825))
    "d" -> ScoreInfo("Poor", Color(0xFFE65100))
    "e" -> ScoreInfo("Bad", Color(0xFFB71C1C))
    else -> ScoreInfo("N/A", Color(0xFF9E9E9E))
}

fun ecoScoreInfo(grade: String?) = when (grade?.lowercase()) {
    "a" -> ScoreInfo("Very low", Color(0xFF2E7D32))
    "b" -> ScoreInfo("Low", Color(0xFF558B2F))
    "c" -> ScoreInfo("Moderate", Color(0xFFF9A825))
    "d" -> ScoreInfo("High", Color(0xFFE65100))
    "e" -> ScoreInfo("Very high", Color(0xFFB71C1C))
    else -> ScoreInfo("N/A", Color(0xFF9E9E9E))
}

fun novaInfo(group: Int?) = when (group) {
    1 -> ScoreInfo("Unprocessed", Color(0xFF2E7D32))
    2 -> ScoreInfo("Minimal proc.", Color(0xFF558B2F))
    3 -> ScoreInfo("Processed", Color(0xFFF9A825))
    4 -> ScoreInfo("Ultra-proc.", Color(0xFFB71C1C))
    else -> ScoreInfo("N/A", Color(0xFF9E9E9E))
}