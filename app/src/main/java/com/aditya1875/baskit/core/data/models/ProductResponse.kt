package com.aditya1875.baskit.core.data.models

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("code")
    val code: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("status_verbose")
    val statusVerbose: String,

    @SerializedName("product")
    val product: Product?
)
