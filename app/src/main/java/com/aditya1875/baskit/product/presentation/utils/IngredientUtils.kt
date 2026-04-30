package com.aditya1875.baskit.product.presentation.utils

val flaggedMap = mapOf(
    "carrageenan" to "often used as a thickener but may cause digestive inflammation in sensitive individuals.",
    "natural flavors" to "a broad term that can include hundreds of synthetic chemicals.",
    "high fructose corn syrup" to "linked to obesity, diabetes, and metabolic issues when consumed regularly.",
    "aspartame" to "an artificial sweetener that some studies link to headaches and neurological symptoms.",
    "sodium nitrite" to "a preservative in processed meats linked to increased cancer risk.",
    "monosodium glutamate" to "may cause headaches and nausea in sensitive individuals.",
    "potassium bromate" to "a flour additive banned in many countries due to cancer risk.",
    "hydrogenated" to "contains trans fats linked to heart disease.",
    "artificial color" to "synthetic dyes linked to hyperactivity in children.",
    "bha" to "a preservative considered possibly carcinogenic.",
    "bht" to "a preservative linked to endocrine disruption."
)

fun detectFlaggedIngredients(ingredientsText: String?): List<String> {
    if (ingredientsText.isNullOrBlank()) return emptyList()
    val lower = ingredientsText.lowercase()
    return flaggedMap.keys.filter { lower.contains(it) }
}

fun flaggedIngredientDetail(ingredient: String): String =
    flaggedMap[ingredient.lowercase()] ?: "$ingredient may be worth researching before regular consumption."
