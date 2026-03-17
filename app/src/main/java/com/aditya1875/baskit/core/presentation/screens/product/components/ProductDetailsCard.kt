package com.aditya1875.baskit.core.presentation.screens.product.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.aditya1875.baskit.core.data.local.Product
import com.aditya1875.baskit.core.presentation.screens.product.utils.cleanProductName
import com.aditya1875.baskit.core.presentation.screens.product.utils.nutriLabel

@Composable
fun ProductDetailsCard(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val product =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Product>("product")

    if (product == null) {
        EmptyState("No product loaded")
        return
    }

    val productName = cleanProductName(product.productName)
    val nutriLabel = nutriLabel(product.nutritionGrade)
    val ecoLabel = nutriLabel(product.ecoscoreGrade)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .then(modifier)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.12f)
                )
        ) {
            AsyncImage(
                model = product.imageFrontUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Fit
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            ),
                            startY = 200f
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            Text(
                text = productName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            product.brand?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            product.categories
                ?.split(",")
                ?.firstOrNull()
                ?.replace("en:", "")
                ?.replace("-", " ")
                ?.replaceFirstChar { it.uppercase() }
                ?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
                    )
                }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Quick insights",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScoreChip("Nutrition", nutriLabel)
                ScoreChip("Eco impact", ecoLabel)
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
