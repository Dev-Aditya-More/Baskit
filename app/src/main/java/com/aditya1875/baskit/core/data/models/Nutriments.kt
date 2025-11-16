package com.aditya1875.baskit.core.data.models

import com.google.gson.annotations.SerializedName

data class Nutriments(
    @SerializedName("energy-kcal_100g")
    val energyKcal100g: Double?,

    @SerializedName("fat_100g")
    val fat100g: Double?,

    @SerializedName("saturated-fat_100g")
    val saturatedFat100g: Double?,

    @SerializedName("carbohydrates_100g")
    val carbohydrates100g: Double?,

    @SerializedName("sugars_100g")
    val sugars100g: Double?,

    @SerializedName("fiber_100g")
    val fiber100g: Double?,

    @SerializedName("proteins_100g")
    val proteins100g: Double?,

    @SerializedName("salt_100g")
    val salt100g: Double?,

    @SerializedName("sodium_100g")
    val sodium100g: Double?
)
