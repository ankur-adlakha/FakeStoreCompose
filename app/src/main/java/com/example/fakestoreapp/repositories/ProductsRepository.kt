package com.example.fakestoreapp.repositories

import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.network.FSApi
import com.example.fakestoreapp.storage.daos.ProductDao
import com.example.fakestoreapp.storage.entities.CategoryEntity
import retrofit2.Response
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val fsApi: FSApi,
    private val productDao: ProductDao
) : IProductsRepository {
    override suspend fun getAllCategories(): Response<List<String>> {
        return fsApi.getAllCategories().execute()
    }

    override suspend fun getProductsByCategory(
        category: String,
        limit: Int?
    ): Response<List<Product>> {
        return fsApi.getProductsByCategory(category, limit).execute()
    }

    override suspend fun getProductDetails(productId: String): Response<Product> {
        return fsApi.getProductDetails(productId).execute()
    }

    override suspend fun getCategoryProductsFromCache(
        category: String,
        limit: Int?
    ): List<Product> {
        return if (limit != null) {
            productDao.getAllProductsFromCategoryLimited(category,limit)
        } else {
            productDao.getAllProductsFromCategory(category)
        }
    }

    override suspend fun getCategoriesFromCache(): List<CategoryEntity> {
        return productDao.getAllCategories()
    }

    override suspend fun insertProducts(products: List<Product>) {
        productDao.insertProducts(products)
    }

    override suspend fun insertCategories(categories: List<CategoryEntity>) {
        productDao.insertCategories(categories)
    }
}