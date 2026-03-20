package com.aditya1875.baskit.core.data.repository

import android.util.Log
import com.aditya1875.baskit.core.data.local.Product
import com.aditya1875.baskit.core.data.remote.RetrofitInstance

class ProductRepository {
    suspend fun getProduct(barcode: String): Product? {
        val response = RetrofitInstance.api.getProductByBarcode(barcode)
        Log.d("API_DEBUG", "status=${response.status}, product=${response.product}")
        return if (response.status == 1) response.product else null
    }
}