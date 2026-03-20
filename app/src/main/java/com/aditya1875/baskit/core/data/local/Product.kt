package com.aditya1875.baskit.core.data.local

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product(

    @SerializedName("code")
    val code: String?,

    @SerializedName("abbreviated_product_name")
    val productName: String?,

    @SerializedName("generic_name")
    val genericName: String?,

    @SerializedName("brands")
    val brand: String?,

    @SerializedName("quantity")
    val quantity: String?,

    @SerializedName("categories")
    val categories: String?,

    @SerializedName("ingredients_text")
    val ingredientsText: String?,

    @SerializedName("allergens")
    val allergens: String?,

    @SerializedName("nutriments")
    val nutriments: Nutriments?,

    @SerializedName("nutrition_grades")
    val nutritionGrade: String?,

    @SerializedName("ecoscore_grade")
    val ecoscoreGrade: String?,

    @SerializedName("image_front_url")
    val imageFrontUrl: String?,

    @SerializedName("image_ingredients_url")
    val imageIngredientsUrl: String?,

    @SerializedName("image_nutrition_url")
    val imageNutritionUrl: String?,

    @SerializedName("image_packaging_url")
    val imagePackagingUrl: String?,

    @SerializedName("labels")
    val labels: String?,

    @SerializedName("packaging")
    val packaging: String?,

    @SerializedName("stores")
    val stores: String?,

    @SerializedName("countries")
    val countries: String?,

    @SerializedName("nova_group")
    val novaGroup: Int?,

    @SerializedName("additives_tags")
    val additivesTags: List<String>?
) : Parcelable

