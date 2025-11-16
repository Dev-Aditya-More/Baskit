package com.aditya1875.baskit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.baskit.core.data.models.Product
import com.aditya1875.baskit.core.data.remote.RetrofitInstance
import com.aditya1875.baskit.core.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = RetrofitInstance.api.getProductByBarcode(barcode)
                Log.d("API", "status = ${response.status}, verbose = ${response.statusVerbose}")
                Log.d("API", "product = ${response.product}")

                if (response.status == 1) {
                    _product.value = response.product
                } else {
                    _product.value = null
                }
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}")
            }

            _isLoading.value = false
        }
    }
}