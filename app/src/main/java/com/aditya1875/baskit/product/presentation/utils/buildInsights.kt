package com.aditya1875.baskit.product.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Warning
import com.aditya1875.baskit.product.data.local.Product
import com.aditya1875.baskit.product.presentation.model.HealthInsight
import com.aditya1875.baskit.product.presentation.model.Sentiment

fun buildInsights(product: Product): List<HealthInsight> {
    val list = mutableListOf<HealthInsight>()
    val n = product.nutriments
    n?.sugars100g?.let { s ->
        list += when {
            s > 22.5 -> HealthInsight(Icons.Outlined.Warning, "High sugar", "${fmt(s)}g per 100g.", Sentiment.BAD)
            s > 12 -> HealthInsight(Icons.Outlined.Info, "Moderate sugar", "${fmt(s)}g per 100g.", Sentiment.WARN)
            else -> HealthInsight(Icons.Outlined.CheckCircle, "Low sugar", "Only ${fmt(s)}g per 100g.", Sentiment.GOOD)
        }
    }
    n?.saturatedFat100g?.let { f ->
        list += when {
            f > 5 -> HealthInsight(Icons.Outlined.Warning, "High saturated fat", "${fmt(f)}g per 100g.", Sentiment.BAD)
            f > 1.5 -> HealthInsight(Icons.Outlined.Info, "Moderate saturated fat", "${fmt(f)}g per 100g.", Sentiment.WARN)
            else -> HealthInsight(Icons.Outlined.CheckCircle, "Low saturated fat", "${fmt(f)}g per 100g.", Sentiment.GOOD)
        }
    }
    n?.proteins100g?.let { p ->
        if (p >= 10) list += HealthInsight(Icons.Outlined.FitnessCenter, "Good protein source", "${fmt(p)}g per 100g.", Sentiment.GOOD)
    }
    n?.fiber100g?.let { f ->
        if (f >= 6) list += HealthInsight(Icons.Outlined.Spa, "High fibre", "${fmt(f)}g per 100g.", Sentiment.GOOD)
        else if (f >= 3) list += HealthInsight(Icons.Outlined.Spa, "Good fibre", "${fmt(f)}g per 100g.", Sentiment.GOOD)
    }
    n?.energyKcal100g?.let { k ->
        if (k > 450) list += HealthInsight(Icons.Outlined.LocalFireDepartment, "Energy dense", "${k.toInt()} kcal per 100g.", Sentiment.WARN)
    }
    product.novaGroup?.let { nova ->
        when (nova) {
            4 -> list += HealthInsight(Icons.Outlined.Science, "Ultra-processed (NOVA 4)", "Regular consumption linked to poorer health.", Sentiment.BAD)
            3 -> list += HealthInsight(Icons.Outlined.Science, "Processed (NOVA 3)", "Moderate consumption advised.", Sentiment.WARN)
            1, 2 -> list += HealthInsight(Icons.Outlined.Eco, "Minimally processed (NOVA $nova)", "Close to whole food.", Sentiment.GOOD)
        }
    }
    return list
}

fun fmt(d: Double): String =
    if (d == d.toLong().toDouble()) d.toLong().toString()
    else "%.1f".format(d)
