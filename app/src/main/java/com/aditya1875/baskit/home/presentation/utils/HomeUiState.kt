package com.aditya1875.baskit.home.presentation.utils

import com.aditya1875.baskit.product.data.local.Product

sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val product: Product) : HomeUiState()
    object NotFound : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}