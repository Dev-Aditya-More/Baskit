package com.aditya1875.baskit.core.data.models

data class Product(
    val code: String,                       // Barcode (e.g. 3274080005003)
    val productName: String?,               // Product name
    val genericName: String?,               // Generic name like "Spring water"
    val brand: String?,                     // Brand name
    val quantity: String?,                  // e.g. "1.5 L"
    val categories: String?,                // Category chain (e.g. Water, Beverages)
    val ingredientsText: String?,           // Ingredient list (raw text)
    val allergens: String?,                 // Allergen info
    val nutriments: Nutriments?,            // Nutrition details (nested model)
    val nutritionGrade: String?,            // e.g. "A", "B", "C" etc.
    val ecoscoreGrade: String?,             // Environmental score grade
    val imageFrontUrl: String?,             // Product image
    val imageIngredientsUrl: String?,       // Ingredients image
    val imageNutritionUrl: String?,         // Nutrition label image
    val imagePackagingUrl: String?          // Packaging image
)
