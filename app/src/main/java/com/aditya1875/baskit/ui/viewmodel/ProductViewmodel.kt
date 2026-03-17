package com.aditya1875.baskit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.baskit.core.data.repository.ProductRepository
import com.aditya1875.baskit.core.presentation.screens.home.utils.ProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val uiState: StateFlow<ProductUiState> = _uiState

    private val _currentProduct = MutableStateFlow<com.aditya1875.baskit.core.data.local.Product?>(null)
    val currentProduct: StateFlow<com.aditya1875.baskit.core.data.local.Product?> = _currentProduct

    init {
        Log.d("ProductVM", "ViewModel created: $this")
    }

    fun fetchProduct(barcode: String) {
        val current = _currentProduct.value
        if (current != null && current.code == barcode && _uiState.value is ProductUiState.Success) {
            return
        }

        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading

            try {
                val product = repository.getProduct(barcode)

                if (product != null) {
                    _currentProduct.value = product
                    _uiState.value = ProductUiState.Success(product)
                } else {
                    _uiState.value = ProductUiState.NotFound
                }

            } catch (e: Exception) {
                Log.e("ProductVM", "fetchProduct error", e)
                _uiState.value = ProductUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun reset() {
        _uiState.value = ProductUiState.Idle
        _currentProduct.value = null
    }
}