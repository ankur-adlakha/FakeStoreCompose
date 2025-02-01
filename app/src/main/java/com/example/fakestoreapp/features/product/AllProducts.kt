package com.example.fakestoreapp.features.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.fakestoreapp.features.common.AddToCartButton
import com.example.fakestoreapp.features.common.FSTopAppBar
import com.example.fakestoreapp.features.common.ProductCategoryText
import com.example.fakestoreapp.features.common.ProductPriceText
import com.example.fakestoreapp.features.common.ProductTitleText
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.models.toJson

@Composable
fun AllProductsUI(navController: NavHostController, category: String) {
    val viewModel = hiltViewModel<ProductViewModel>()
    if (viewModel.productList.value.isEmpty()) {
        LaunchedEffect(viewModel.getProductsForCategory(category)) {
            viewModel.getProductsForCategory(category)
        }
    }
    Scaffold(topBar = { FSTopAppBar(category, navController) }) { innerPadding ->
        ProductsList(Modifier.padding(innerPadding), viewModel.productList.value) { product ->
            navController.navigate("ProductDetails?product=${product.toJson()}")
        }
    }
}

@Composable
fun ProductsList(modifier: Modifier, products: List<Product>, onClick: (Product) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(products.size) { index ->
            val product = products[index]
            ProductCard(
                product = product, modifier = Modifier.clickable(true, onClick = {
                    onClick.invoke(product)
                })
            )
        }

    }
}

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
            contentDescription = "ProductImage"
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