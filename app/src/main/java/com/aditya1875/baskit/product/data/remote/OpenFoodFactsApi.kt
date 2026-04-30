package com.aditya1875.baskit.product.data.remote

import com.aditya1875.baskit.product.data.local.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): ProductResponse
}