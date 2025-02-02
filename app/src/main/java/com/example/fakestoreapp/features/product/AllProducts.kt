package com.example.fakestoreapp.features.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.fakestoreapp.features.common.*
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.models.toJson

/**
 * Composable function to display all products under a given category.
 * Fetches data using the [ProductViewModel] and displays it in a list.
 *
 * @param navController Navigation controller to handle navigation between screens.
 * @param category The category of products to display.
 */
@Composable
fun AllProductsUI(navController: NavHostController, category: String) {
    val viewModel = hiltViewModel<ProductViewModel>()

    // Fetch products for the given category if the list is empty
    if (viewModel.productList.value.isEmpty()) {
        LaunchedEffect(viewModel.getProductsForCategory(category)) {
            viewModel.getProductsForCategory(category)
        }
    }

    Scaffold(topBar = { FSTopAppBar(category, navController) }) { innerPadding ->
        ProductsList(
            Modifier.padding(innerPadding),
            viewModel.productList.value
        ) { product ->
            navController.navigate("ProductDetails?product=${product.toJson()}")
        }
    }
}

/**
 * Composable function to display a list of products.
 *
 * @param modifier Modifier for styling and layout.
 * @param products List of products to display.
 * @param onClick Callback function to handle product click events.
 */
@Composable
fun ProductsList(modifier: Modifier, products: List<Product>, onClick: (Product) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(products.size) { index ->
            val product = products[index]
            ProductCard(
                product = product,
                modifier = Modifier.clickable(true) { onClick(product) }
            )
        }
    }
}

/**
 * Composable function to display a single product card.
 *
 * @param product The product to display.
 * @param modifier Modifier for styling and layout.
 */
@Composable
fun ProductCard(product: Product, modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(196.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .width(196.dp)
                .height(196.dp),
            model = product.image,
            contentDescription = "Product Image"
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            ProductTitleText(product.title ?: "", modifier = Modifier.padding(top = 8.dp))
            ProductPriceText(product.price ?: "", modifier = Modifier.padding(top = 8.dp))
            ProductCategoryText(
                product.category?.uppercase() ?: "", modifier = Modifier.padding(top = 8.dp)
            )
            AddToCartButton(modifier = Modifier.padding(top = 8.dp))
        }
    }
}
