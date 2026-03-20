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
    viewModel: ProductViewModel,
    navController: NavHostController,
    code: String,
    onProductNotFound: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(code) {
        viewModel.fetchProduct(code)
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is ProductUiState.Success -> {
                navController.navigate(Screen.ProductDetail.route){
                    popUpTo(Screen.ProductLoading.route) { inclusive = true }
                }
            }
            ProductUiState.NotFound -> {
                onProductNotFound()
            }
            else -> Unit
        }
    }

    when (uiState) {
        ProductUiState.Loading, ProductUiState.Idle -> LoadingState()
        is ProductUiState.Error -> {
            val msg = (uiState as ProductUiState.Error).message
            ErrorState(msg)
        }
        else -> { /* Success/NotFound handled above via LaunchedEffect */ }
    }
}

