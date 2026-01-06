package com.aditya1875.baskit.core.presentation.screens.product.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aditya1875.baskit.core.presentation.screens.home.components.ErrorState
import com.aditya1875.baskit.core.presentation.screens.home.components.LoadingState
import com.aditya1875.baskit.core.presentation.screens.home.utils.ProductUiState
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel

@Composable
fun ProductLoadingScreen(
    code: String,
    onProductFound: () -> Unit,
    onProductNotFound: () -> Unit
) {
    val viewModel: ProductViewModel = viewModel()
    val product by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchProduct(code)
    }

    when (product) {
        ProductUiState.Loading -> LoadingState()

        is ProductUiState.Success -> onProductFound()

        ProductUiState.NotFound -> onProductNotFound()

        is ProductUiState.Error -> ErrorState("Failed to load product")

        ProductUiState.Idle -> {}
    }
}
