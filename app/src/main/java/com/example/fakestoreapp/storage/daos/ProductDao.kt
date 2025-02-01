package com.example.fakestoreapp.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.storage.entities.CategoryEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCategories(categories:List<CategoryEntity>)

    @Query("SELECT * FROM product where category=:category")
    suspend fun getAllProductsFromCategory(category: String): List<Product>

    @Query("SELECT * FROM product where category=:category limit :limit")
    suspend fun getAllProductsFromCategoryLimited(category: String,limit:Int): List<Product>

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>
}