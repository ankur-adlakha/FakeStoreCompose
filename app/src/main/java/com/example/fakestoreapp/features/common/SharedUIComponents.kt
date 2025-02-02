package com.example.fakestoreapp.features.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fakestoreapp.utils.AppConstants

/**
 * Custom top app bar with a centered title and optional back navigation button.
 *
 * @param titleText The title to be displayed in the top app bar.
 * @param navController The navigation controller for handling back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FSTopAppBar(titleText: String? = null, navController: NavController? = null) =
    CenterAlignedTopAppBar(
        title = {
            if (!titleText.isNullOrEmpty()) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = titleText,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        },
        navigationIcon = {
            if (navController != null) {
                Icon(
                    modifier = Modifier.clickable(true, onClick = {
                        navController.navigateUp()
                    }),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )

/**
 * Displays the product title with optional text alignment.
 */
@Composable
fun ProductTitleText(text: String, modifier: Modifier, textAlign: TextAlign? = null) = Text(
    text = text,
    modifier = modifier,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis,
    textAlign = textAlign
)

/**
 * Displays the product price with currency symbol and styling.
 */
@Composable
fun ProductPriceText(price: String, modifier: Modifier, textAlign: TextAlign? = null) = Text(
    text = "${AppConstants.USER_CURRENCY}${price}",
    modifier = modifier,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    textAlign = textAlign
)

/**
 * Displays the product category with a smaller font size.
 */
@Composable
fun ProductCategoryText(category: String, modifier: Modifier, textAlign: TextAlign? = null) = Text(
    text = category,
    modifier = modifier,
    fontSize = 14.sp,
    textAlign = textAlign
)

/**
 * Button to add a product to the cart.
 */
@Composable
fun AddToCartButton(modifier: Modifier) =
    ElevatedButton(
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = {},
        modifier = modifier
    ) {
        Text("Add to cart")
    }