package com.aditya1875.baskit.product.domain.formatter

fun cleanProductName(name: String?): String {
    return name
        ?.replace(Regex("\\s+t\\d+"), "")
        ?.trim()
        ?: "Unknown product"
}
