package com.example.fakestoreapp.models

import com.google.gson.annotations.SerializedName

data class ProductsByCategory(
    val productsList: List<Product>? = null
)