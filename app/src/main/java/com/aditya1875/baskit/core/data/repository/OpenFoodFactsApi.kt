package com.aditya1875.baskit.core.data.repository

import com.aditya1875.baskit.core.data.models.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): ProductResponse
}