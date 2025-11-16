package com.aditya1875.baskit.core.data.models

data class ProductResponse(
    val code: String,
    val product: Product?,
    val status: Int?,       // 1 = found, 0 = not found
    val statusVerbose: String?
)
