package com.example.fakestoreapp.features.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.repositories.IProductsRepository
import com.example.fakestoreapp.storage.entities.CategoryEntity
import com.example.fakestoreapp.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for managing the home screen data.
 * This ViewModel fetches products by categories, handles caching, and manages UI state.
 *
 * @param productsRepository Repository for fetching product data.
 * @param networkUtils Utility class for checking network connectivity.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsRepository: IProductsRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    // Mutable state to hold the list of products grouped by category.
    private val _productsByCategoryMutableState: MutableState<List<Pair<String, List<Product>>>?> =
        mutableStateOf(null)

    // Public immutable state to expose category-product data.
    val productsByCategoryData: State<List<Pair<String, List<Product>>>?> =
        _productsByCategoryMutableState

    // Mutable state to track loading status.
    private val _loadingMutableState: MutableState<Boolean> = mutableStateOf(false)

    // Public immutable state for loading.
    val loading: State<Boolean> = _loadingMutableState

    // Mutable state to track errors (Boolean flag and error message).
    private val _errorMutableState: MutableState<Pair<Boolean, String>> =
        mutableStateOf(Pair(false, ""))

    // Public immutable state for error tracking.
    val error: State<Pair<Boolean, String>> = _errorMutableState

    /**
     * Fetches product categories and their respective products.
     * This function:
     * - Checks for network connectivity.
     * - Fetches categories from API (if online) or cache (if offline).
     * - Retrieves a limited number of products per category.
     * - Stores fetched data in the database for caching.
     * - Updates UI states (loading, data, error).
     */
    fun getProductsWithCategories() {
        _loadingMutableState.value = true  // Start loading
        val categoriesWithProducts = mutableListOf<Pair<String, List<Product>>>()
        var categoriesList: List<String>? = mutableListOf<String>()
        val allProductList = mutableListOf<Product>()

        // Launch coroutine to fetch data in the background
        viewModelScope.launch {
            withContext(Dispatchers.IO) {  // Perform network and database operations on IO thread
                val isConnectedToNetwork = networkUtils.isConnectedToNetwork()

                // Fetch categories based on network availability
                if (isConnectedToNetwork) {
                    val categoriesResponse = productsRepository.getAllCategories()
                    if (categoriesResponse.isSuccessful && !categoriesResponse.body().isNullOrEmpty()) {
                        categoriesList = categoriesResponse.body()!!

                        // Cache categories in the local database
                        productsRepository.insertCategories(mutableListOf<CategoryEntity>().apply {
                            addAll(categoriesList!!.map { category -> CategoryEntity(category) })
                        })
                    }
                } else {
                    // Fetch categories from the local cache if offline
                    categoriesList = productsRepository.getCategoriesFromCache().map { categoryEntity ->
                        categoryEntity.category
                    }
                }

                // Fetch products for each category
                categoriesList?.forEach { category ->
                    var productsList = listOf<Product>()

                    if (isConnectedToNetwork) {
                        // Fetch limited products from API (e.g., 3 per category)
                        val productsResponse = productsRepository.getProductsByCategory(category, 3)
                        if (productsResponse.isSuccessful && !productsResponse.body().isNullOrEmpty()) {
                            productsList = productsResponse.body()!!
                            allProductList.addAll(productsList)  // Add to full product list for caching
                        }
                    } else {
                        // Fetch limited products from local cache if offline
                        productsList = productsRepository.getCategoryProductsFromCache(category, 3)
                    }

                    // Store the category with its products
                    categoriesWithProducts.add(Pair(category, productsList))
                }

                // Cache all fetched products in the database
                productsRepository.insertProducts(allProductList)

                // Update UI states
                _loadingMutableState.value = false  // Stop loading
                _productsByCategoryMutableState.value = categoriesWithProducts  // Update UI with new data
            }
        }
    }
}
