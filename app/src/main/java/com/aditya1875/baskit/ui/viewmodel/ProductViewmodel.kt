package com.aditya1875.baskit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.baskit.core.data.repository.ProductRepository
import com.aditya1875.baskit.core.presentation.screens.home.utils.ProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val uiState: StateFlow<ProductUiState> = _uiState

    init {
        Log.d("ProductVM", "ViewModel created: $this")
    }

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading

            try {
                val product = repository.getProduct(barcode)

                _uiState.value = if (product != null) {
                    ProductUiState.Success(product)
                } else {
                    ProductUiState.NotFound
                }

            } catch (e: Exception) {
                _uiState.value =
                    ProductUiState.Error(
                        e.message ?: "Something went wrong"
                    )
            }
        }
    }
}