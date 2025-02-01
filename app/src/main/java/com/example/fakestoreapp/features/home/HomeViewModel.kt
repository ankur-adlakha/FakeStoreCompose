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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsRepository: IProductsRepository, private val networkUtils: NetworkUtils
) : ViewModel() {
    private val _productsByCategoryMutableState: MutableState<List<Pair<String, List<Product>>>?> =
        mutableStateOf(null)
    val productsByCategoryData: State<List<Pair<String, List<Product>>>?> =
        _productsByCategoryMutableState

    private val _loadingMutableState: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> = _loadingMutableState

    private val _errorMutableState: MutableState<Pair<Boolean, String>> =
        mutableStateOf(Pair(false, ""))
    val error: State<Pair<Boolean, String>> = _errorMutableState

    fun getProductsWithCategories() {
        _loadingMutableState.value = true
        val categoriesWithProducts = mutableListOf<Pair<String, List<Product>>>()
        var categoriesList: List<String>? = mutableListOf<String>()
        val allProductList = mutableListOf<Product>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isConnectedToNetwork = networkUtils.isConnectedToNetwork()
                if (isConnectedToNetwork) {
                    val categoriesResponse = productsRepository.getAllCategories()
                    if (categoriesResponse.isSuccessful && !categoriesResponse.body()
                            .isNullOrEmpty()
                    ) {
                        categoriesList = categoriesResponse.body()!!
                        productsRepository.insertCategories(mutableListOf<CategoryEntity>().apply {
                            addAll(categoriesList!!.map { category -> CategoryEntity(category) })
                        })

                    }
                } else {
                    categoriesList =
                        productsRepository.getCategoriesFromCache().map { categoryEntity ->
                            categoryEntity.category
                        }
                }
                categoriesList?.forEach { category ->
                    var productsList = listOf<Product>()
                    if (isConnectedToNetwork) {
                        val productsResponse = productsRepository.getProductsByCategory(category, 3)
                        if (productsResponse.isSuccessful && !productsResponse.body()
                                .isNullOrEmpty()
                        ) {
                            productsList = productsResponse.body()!!
                            allProductList.addAll(productsList)
                        }
                    } else {
                        productsList = productsRepository.getCategoryProductsFromCache(category, 3)
                    }
                    categoriesWithProducts.add(Pair(category, productsList))
                }
                productsRepository.insertProducts(allProductList)
                _loadingMutableState.value = false
                _productsByCategoryMutableState.value = categoriesWithProducts
            }
        }
    }
}