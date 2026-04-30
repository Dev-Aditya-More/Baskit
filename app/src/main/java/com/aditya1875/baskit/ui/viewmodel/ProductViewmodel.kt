package com.aditya1875.baskit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.baskit.product.data.repository.ProductRepository
import com.aditya1875.baskit.home.presentation.utils.HomeUiState
import com.aditya1875.baskit.product.data.local.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _currentProduct = MutableStateFlow<Product?>(null)
    val currentProduct: StateFlow<Product?> = _currentProduct

    init {
        Log.d("ProductVM", "ViewModel created: $this")
    }

    fun fetchProduct(barcode: String) {
        val current = _currentProduct.value
        if (current != null && current.code == barcode && _uiState.value is HomeUiState.Success) {
            return
        }

        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                val product = repository.getProduct(barcode)

                if (product != null) {
                    _currentProduct.value = product
                    _uiState.value = HomeUiState.Success(product)
                } else {
                    _uiState.value = HomeUiState.NotFound
                }

            } catch (e: Exception) {
                Log.e("ProductVM", "fetchProduct error", e)
                _uiState.value = HomeUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun reset() {
        _uiState.value = HomeUiState.Idle
        _currentProduct.value = null
    }
}