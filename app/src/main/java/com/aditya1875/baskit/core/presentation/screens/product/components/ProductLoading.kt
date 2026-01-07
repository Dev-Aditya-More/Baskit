package com.aditya1875.baskit.core.presentation.screens.product.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.aditya1875.baskit.Screen
import com.aditya1875.baskit.core.presentation.screens.home.utils.ProductUiState
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductLoadingScreen(
    viewModel: ProductViewModel = koinViewModel(),
    navController: NavHostController,
    code: String,
    onProductNotFound: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(code) {
        viewModel.fetchProduct(code)
    }

    var handled by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (handled) return@LaunchedEffect

        when (uiState) {
            is ProductUiState.Success -> {
                val product = (uiState as ProductUiState.Success).product

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("product", product)

                navController.navigate(Screen.ProductDetail.pass(product.code))
            }
            ProductUiState.NotFound -> {
                handled = true
                onProductNotFound()
            }
            else -> Unit
        }
    }

    when (uiState) {
        ProductUiState.Loading -> LoadingState()

        is ProductUiState.Error -> {
            ErrorState("Failed to load product")
        }

        else -> {
            // Idle / Success / NotFound
            // No UI needed here
        }
    }
}

