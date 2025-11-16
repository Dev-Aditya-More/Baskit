package com.aditya1875.baskit.core.data.models

data class Category(
    val name: String,
    val hierarchy: List<String>
)

data class Brand(
    val name: String,
    val tag: String
)

