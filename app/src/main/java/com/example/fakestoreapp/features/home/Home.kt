package com.example.fakestoreapp.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.fakestoreapp.R
import com.example.fakestoreapp.features.common.FSTopAppBar
import com.example.fakestoreapp.features.common.ProductPriceText
import com.example.fakestoreapp.features.common.ProductTitleText
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.models.toJson

@OptIn(ExperimentalMaterial3Api::class)
/**
 * Home screen UI that displays product categories and products.
 *
 * @param navController Used for navigation between screens.
 * @param modifier Modifier for styling.
 */
@Composable
fun HomeUI(navController: NavHostController, modifier: Modifier) {
    val viewModel = hiltViewModel<HomeViewModel>()

    // Fetch product categories and associated products when the UI is launched
    if (viewModel.productsByCategoryData.value.isNullOrEmpty()) {
        LaunchedEffect(viewModel.productsByCategoryData) {
            viewModel.getProductsWithCategories()
        }
    }

    val loading = viewModel.loading
    val productsByCategory = viewModel.productsByCategoryData.value

    when {
        loading.value -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        viewModel.error.value.first || productsByCategory.isNullOrEmpty() -> {
            // TODO: Display an error message here
        }

        else -> {
            Scaffold(topBar = {
                FSTopAppBar(stringResource(R.string.app_name))
            }) { innerPadding ->
                CategoriesUI(navController, modifier.padding(innerPadding), productsByCategory)
            }
        }
    }
}

/**
 * Displays a list of product categories with horizontal scrolling products.
 *
 * @param navController Used for navigation.
 * @param modifier Modifier for styling.
 * @param productsByCategoryList List of category-product pairs.
 */
@Composable
fun CategoriesUI(
    navController: NavHostController,
    modifier: Modifier,
    productsByCategoryList: List<Pair<String, List<Product>>>
) {
    LazyColumn(
        modifier = modifier,
        content = {
            items(productsByCategoryList) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.first.uppercase(),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold, letterSpacing = 0.8.sp
                            ),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 40.dp)
                                .align(Alignment.CenterVertically)
                        )
                        TextButton(onClick = {
                            navController.navigate("AllProducts?category=${item.first}")
                        }) {
                            Text(
                                text = "See all",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .padding(end = 16.dp, top = 40.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 16.dp, top = 8.dp),
                        content = {
                            items(item.second) { product ->
                                Column(
                                    modifier = Modifier
                                        .width(240.dp)
                                        .wrapContentHeight()
                                        .clickable(true, onClick = {
                                            navController.navigate("ProductDetails?product=${product.toJson()}")
                                        })
                                ) {
                                    AsyncImage(
                                        model = product.image,
                                        contentScale = ContentScale.FillBounds,
                                        error = painterResource(android.R.drawable.stat_notify_error),
                                        placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(240.dp)
                                            .width(240.dp)
                                            .shadow(elevation = 2.dp, RoundedCornerShape(4.dp))
                                            .clipToBounds()
                                    )
                                    ProductTitleText(
                                        product.title ?: "",
                                        Modifier.padding(top = 4.dp)
                                    )
                                    ProductPriceText(
                                        product.price ?: "",
                                        Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        },
                        userScrollEnabled = true,
                    )
                }
            }
        },
    )
}
