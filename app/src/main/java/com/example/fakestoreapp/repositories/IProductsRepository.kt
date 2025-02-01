package com.example.fakestoreapp.repositories

import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.storage.entities.CategoryEntity
import retrofit2.Response

interface IProductsRepository {
    suspend fun getAllCategories(): Response<List<String>>
    suspend fun getProductsByCategory(category: String, limit: Int? = null): Response<List<Product>>
    suspend fun getProductDetails(productId: String): Response<Product>
    suspend fun getCategoryProductsFromCache(category: String, limit: Int? = null): List<Product>
    suspend fun getCategoriesFromCache(): List<CategoryEntity>
    suspend fun insertProducts(products: List<Product>)
    suspend fun insertCategories(categories: List<CategoryEntity>)
}