package com.example.fakestoreapp.features.product

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.repositories.ProductsRepository
import com.example.fakestoreapp.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val networkUtils: NetworkUtils
) :
    ViewModel() {
    private val _productDetailsMutableState = mutableStateOf<Product?>(null)
    val productDetails: State<Product?> = _productDetailsMutableState

    private val _productListMutableState = mutableStateOf<List<Product>>(emptyList())
    val productList: State<List<Product>> = _productListMutableState

    private val _loadingMutableState = mutableStateOf<Boolean>(false)
    val loading: State<Boolean> = _loadingMutableState

    private val _errorMutableState = mutableStateOf<String?>(null)
    val error: State<String?> = _errorMutableState

    fun getProductsForCategory(category: String) {
        _loadingMutableState.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var products: List<Product> = mutableListOf<Product>()
                if (networkUtils.isConnectedToNetwork()) {
                    val response = productsRepository.getProductsByCategory(category)
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        products = response.body()!!
                        productsRepository.insertProducts(products)
                    } else {
                        _errorMutableState.value = "Unable to fetch list"
                    }
                } else {
                    products = productsRepository.getCategoryProductsFromCache(category)
                }
                _productListMutableState.value = products
                _loadingMutableState.value = false
            }
        }
    }

    fun getProductDetails(productId: String) {
        _loadingMutableState.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = productsRepository.getProductDetails(productId)
                _loadingMutableState.value = false
                if (response.isSuccessful && response.body() != null) {
                    _productDetailsMutableState.value = response.body()
                } else {
                    _errorMutableState.value = "Error fetching details"
                }
            }
        }
    }
}