package com.aditya1875.baskit.core.presentation.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aditya1875.baskit.core.data.local.Product
import com.aditya1875.baskit.core.presentation.screens.product.components.EmptyState
import com.aditya1875.baskit.core.presentation.screens.product.components.ProductDetailsCard
import com.aditya1875.baskit.core.presentation.screens.product.utils.cleanProductName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController
) {
    val product =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Product>("product")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = cleanProductName(product?.productName),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (product != null) {

            ProductDetailsCard(
                navController,
                Modifier.padding(padding)
            )
        } else {
            EmptyState("No product loaded")
        }
    }
}

