package com.aditya1875.baskit.product.presentation.utils

import androidx.compose.ui.graphics.Color
import com.aditya1875.baskit.product.data.local.Product
import com.aditya1875.baskit.product.presentation.model.VerdictInfo
import com.aditya1875.baskit.ui.theme.GreenAccent
import kotlin.compareTo

fun calculateScore(product: Product): Int {
    var score = 50
    when (product.nutritionGrade?.lowercase()) {
        "a" -> score += 22; "b" -> score += 14; "c" -> score += 0
        "d" -> score -= 14; "e" -> score -= 22
    }
    when (product.novaGroup) {
        1 -> score += 15; 2 -> score += 8; 3 -> score -= 8; 4 -> score -= 22
    }
    product.nutriments?.sugars100g?.let { s ->
        if (s > 22.5) score -= 10 else if (s < 5) score += 5
    }
    product.nutriments?.saturatedFat100g?.let { f ->
        if (f > 5) score -= 8 else if (f < 1.5) score += 4
    }
    product.nutriments?.proteins100g?.let { p -> if (p >= 10) score += 5 }
    product.nutriments?.fiber100g?.let { f -> if (f >= 6) score += 5 }
    return score.coerceIn(0, 100)
}

fun scoreColor(score: Int): Color = when {
    score >= 70 -> Color(0xFF00E676)
    score >= 50 -> Color(0xFFFFAA00)
    score >= 30 -> Color(0xFFFF6B00)
    else -> Color(0xFFFF4444)
}

fun scoreLabel(score: Int): String = when {
    score >= 70 -> "Excellent"
    score >= 50 -> "Good"
    score >= 35 -> "Fair"
    else -> "Poor"
}

fun buildVerdict(product: Product): VerdictInfo {
    val score = calculateScore(product)
    return when {
        score >= 72 -> VerdictInfo("Premium Choice", GreenAccent)
        score >= 52 -> VerdictInfo("Decent Pick", Color(0xFFFFAA00))
        score >= 35 -> VerdictInfo("Use Caution", Color(0xFFFF6B00))
        else -> VerdictInfo("Avoid If Possible", Color(0xFFFF4444))
    }
}

fun verdictTagline(score: Int): String = when {
    score >= 72 -> "Nutrient-rich, minimally processed"
    score >= 52 -> "Reasonable balance with a few caveats"
    score >= 35 -> "Notable concerns — eat in moderation"
    else -> "Multiple red flags — consider alternatives"
}

fun gradeColor(grade: String?): Color = when (grade?.lowercase()) {
    "a" -> Color(0xFF00C462)
    "b" -> Color(0xFF7ED957)
    "c" -> Color(0xFFFFC93C)
    "d" -> Color(0xFFFF8A3D)
    "e" -> Color(0xFFFF4444)
    else -> Color(0xFF666666)
}

fun novaColor(group: Int?): Color = when (group) {
    1 -> Color(0xFF00C462)
    2 -> Color(0xFF7ED957)
    3 -> Color(0xFFFF8A3D)
    4 -> Color(0xFFFF4444)
    else -> Color(0xFF666666)
}