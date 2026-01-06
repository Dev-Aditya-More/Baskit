package com.aditya1875.baskit.core.presentation.screens.home.utils

import com.aditya1875.baskit.core.data.local.Product

sealed class ProductUiState {
    object Idle : ProductUiState()
    object Loading : ProductUiState()
    data class Success(val product: Product) : ProductUiState()
    object NotFound : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}
