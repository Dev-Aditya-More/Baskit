package com.aditya1875.baskit.product.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class HealthInsight(
    val icon: ImageVector, val label: String,
    val description: String, val sentiment: Sentiment
)