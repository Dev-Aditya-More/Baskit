package com.aditya1875.baskit.core.presentation.screens.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aditya1875.baskit.core.data.local.Product
import com.aditya1875.baskit.core.presentation.screens.home.components.EmptyState
import com.aditya1875.baskit.core.presentation.screens.home.components.ProductDetailsCard
import com.aditya1875.baskit.core.presentation.screens.home.utils.ProductUiState
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen() {
    val viewModel: ProductViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ProductUiState.Success -> {
            val product = (uiState as ProductUiState.Success).product
            ProductDetailsCard(product)
        }

        else -> {
            // Defensive fallback
            EmptyState("No product loaded")
        }
    }
}