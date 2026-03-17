package com.aditya1875.baskit.core.presentation.screens.product

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aditya1875.baskit.core.data.local.Nutriments
import com.aditya1875.baskit.core.data.local.Product
import com.aditya1875.baskit.core.presentation.screens.product.components.EmptyState
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val product by viewModel.currentProduct.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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

        val currentProduct = product
        if (currentProduct != null) {
            ProductDetailsCard(
                product = currentProduct,
                modifier = Modifier.padding(padding)
            )
        } else {
            EmptyState("No product loaded")
        }
    }
}

@Composable
fun ProductDetailsCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    val productName = cleanProductName(product.productName)
    val nutriLabel = nutriLabel(product.nutritionGrade)
    val ecoLabel = ecoLabel(product.ecoscoreGrade)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            AsyncImage(
                model = product.imageFrontUrl,
                contentDescription = product.productName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            // Gradient fade into background
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
        }

        // Product info
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Name
            Text(
                text = productName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Brand
            product.brand?.takeIf { it.isNotBlank() }?.let { brand ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = brand,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Category tag
            product.categories
                ?.split(",")
                ?.firstOrNull()
                ?.replace("en:", "")
                ?.replace("-", " ")
                ?.trim()
                ?.replaceFirstChar { it.uppercase() }
                ?.takeIf { it.isNotBlank() }
                ?.let { category ->
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

            Spacer(Modifier.height(28.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            Spacer(Modifier.height(24.dp))

            // Scores section
            Text(
                text = "Scores",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ScoreCard(
                    label = "Nutrition",
                    grade = product.nutritionGrade?.uppercase() ?: "?",
                    scoreInfo = nutriLabel,
                    modifier = Modifier.weight(1f)
                )
                ScoreCard(
                    label = "Eco Impact",
                    grade = product.ecoscoreGrade?.uppercase() ?: "?",
                    scoreInfo = ecoLabel,
                    modifier = Modifier.weight(1f)
                )
            }

            // Nutrients section
            product.nutriments?.let { n ->
                Spacer(Modifier.height(28.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Nutrition facts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(12.dp))

                NutritionFactsTable(nutriments = n)
            }

            // Ingredients
            product.ingredientsText?.takeIf { it.isNotBlank() }?.let { ingredients ->
                Spacer(Modifier.height(28.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = ingredients,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                    lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ScoreCard(
    label: String,
    grade: String,
    scoreInfo: ScoreLabel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = scoreInfo.color.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(scoreInfo.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = grade,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = scoreInfo.color
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = scoreInfo.label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = scoreInfo.color
            )
        }
    }
}

@Composable
private fun NutritionFactsTable(nutriments: Nutriments) {
    val rows = buildList {
        nutriments.energyKcal100g?.let { add("Energy" to "${it.toInt()} kcal") }
        nutriments.fat100g?.let { add("Fat" to "${it}g") }
        nutriments.saturatedFat100g?.let { add("Saturated fat" to "${it}g") }
        nutriments.carbohydrates100g?.let { add("Carbohydrates" to "${it}g") }
        nutriments.sugars100g?.let { add("Sugars" to "${it}g") }
        nutriments.proteins100g?.let { add("Protein" to "${it}g") }
        nutriments.salt100g?.let { add("Salt" to "${it}g") }
        nutriments.fiber100g?.let { add("Fibre" to "${it}g") }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        rows.forEachIndexed { index, (name, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            if (index < rows.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
            }
        }
    }
}

// --- Helpers ---

data class ScoreLabel(val label: String, val color: Color)

fun nutriLabel(grade: String?): ScoreLabel = when (grade?.lowercase()) {
    "a" -> ScoreLabel("Excellent", Color(0xFF2E7D32))
    "b" -> ScoreLabel("Good", Color(0xFF558B2F))
    "c" -> ScoreLabel("Average", Color(0xFFF9A825))
    "d" -> ScoreLabel("Poor", Color(0xFFE65100))
    "e" -> ScoreLabel("Bad", Color(0xFFC62828))
    else -> ScoreLabel("Unknown", Color(0xFF9E9E9E))
}

fun ecoLabel(grade: String?): ScoreLabel = when (grade?.lowercase()) {
    "a" -> ScoreLabel("Very low", Color(0xFF2E7D32))
    "b" -> ScoreLabel("Low", Color(0xFF558B2F))
    "c" -> ScoreLabel("Moderate", Color(0xFFF9A825))
    "d" -> ScoreLabel("High", Color(0xFFE65100))
    "e" -> ScoreLabel("Very high", Color(0xFFC62828))
    else -> ScoreLabel("Unknown", Color(0xFF9E9E9E))
}

fun cleanProductName(name: String?): String {
    if (name.isNullOrBlank()) return "Unknown Product"
    return name
        .replace(Regex("[_-]"), " ")
        .trim()
        .replaceFirstChar { it.uppercase() }
}

