package com.aditya1875.baskit.core.data.repository

import com.aditya1875.baskit.core.data.models.Product
import com.aditya1875.baskit.core.data.remote.RetrofitInstance

class ProductRepository {

    suspend fun getProduct(barcode: String): Product? {
        val response = RetrofitInstance.api.getProductByBarcode(barcode)
        return if (response.status == 1) response.product else null
    }
}