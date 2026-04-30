package com.aditya1875.baskit.product.domain.formatter

fun nutriLabel(score: String?): String =
    when (score?.lowercase()) {
        "a" -> "Very healthy"
        "b" -> "Healthy"
        "c" -> "Moderate"
        "d" -> "Unhealthy"
        "e" -> "Very unhealthy"
        else -> "Unknown"
    }
