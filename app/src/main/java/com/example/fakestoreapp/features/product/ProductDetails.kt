package com.example.fakestoreapp.features.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.fakestoreapp.features.common.FSTopAppBar
import com.example.fakestoreapp.features.common.ProductPriceText
import com.example.fakestoreapp.features.common.ProductTitleText
import com.example.fakestoreapp.models.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetails(navController: NavHostController, modifier: Modifier, product: Product) {
    Scaffold(topBar = { FSTopAppBar(product.title ?: "", navController) }) { innerPadding ->
        ProductDetailsUI(product, modifier.padding(innerPadding))
    }
}

@Composable
fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ErrorUI() {
}

@Composable
fun ProductDetailsUI(product: Product, modifier: Modifier) {
    val scrollState = rememberScrollState()
    Box {
        Column(
            modifier = modifier
                .padding(bottom = 64.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = product.image,
                contentScale = ContentScale.Inside,
                contentDescription = null,
                modifier = Modifier
                    .height(
                        (LocalConfiguration.current.screenHeightDp / 2).dp
                    )
                    .fillMaxWidth()
            )
            ProductTitleText(
                text = product.title ?: "",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            )
            ProductPriceText(
                price = product.price ?: "",
                modifier = Modifier
                    .padding(top = 4.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )
//        Text(product.rating ?: "",modifier= Modifier.padding(top = 4.dp))
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
            Text(
                product.description ?: "",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            )
        }
        ElevatedButton(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Text("Add to cart")
        }
    }
}