package com.example.fakestoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakestoreapp.features.home.HomeUI
import com.example.fakestoreapp.features.product.AllProductsUI
import com.example.fakestoreapp.features.product.ProductDetails
import com.example.fakestoreapp.models.toProduct
import com.example.fakestoreapp.ui.theme.FakeStoreAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FakeStoreAppTheme {
                FsApp(PaddingValues(0.dp))
            }
        }
    }

    @Composable
    private fun FsApp(innerPadding: PaddingValues) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "Home") {
            composable("Home") {
                HomeUI(navController, Modifier.padding(innerPadding))
            }
            composable(
                "AllProducts?category={category}",
                arguments = listOf(navArgument("category", builder = {
                    type =
                        NavType.StringType
                }))
            ) { backStateEntry ->
                AllProductsUI(navController, backStateEntry.arguments?.getString("category") ?: "")
            }
            composable(
                "ProductDetails?product={product}",
                arguments = listOf(navArgument("product", builder = {
                    type = NavType.StringType
                }))
            ) { backStateEntry ->
                ProductDetails(
                    navController,
                    Modifier.padding(innerPadding),
                    (backStateEntry.arguments?.getString("product") ?: "").toProduct()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FakeStoreAppTheme {
    }
}