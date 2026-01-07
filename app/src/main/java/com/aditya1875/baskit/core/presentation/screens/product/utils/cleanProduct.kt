package com.aditya1875.baskit.core.presentation.screens.product.utils

fun cleanProductName(name: String?): String {
    return name
        ?.replace(Regex("\\s+t\\d+"), "")
        ?.trim()
        ?: "Unknown product"
}
