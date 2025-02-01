package com.example.fakestoreapp.network

import com.example.fakestoreapp.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FSApi {
    @GET("products/categories")
    fun getAllCategories(): Call<List<String>>

    @GET("products/category/{category}")
    fun getProductsByCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int? = null
    ): Call<List<Product>>

    @GET("products/{productId}")
    fun getProductDetails(@Path("productId") productId: String): Call<Product>
}