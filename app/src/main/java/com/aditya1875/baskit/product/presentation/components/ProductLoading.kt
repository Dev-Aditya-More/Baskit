package com.aditya1875.baskit.product.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.aditya1875.baskit.core.navigation.Screen
import com.aditya1875.baskit.home.presentation.utils.HomeUiState
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel

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
            is HomeUiState.Success -> {
                navController.navigate(Screen.ProductDetail.route){
                    popUpTo(Screen.ProductLoading.route) { inclusive = true }
                }
            }
            HomeUiState.NotFound -> {
                onProductNotFound()
            }
            else -> Unit
        }
    }

    when (uiState) {
        HomeUiState.Loading, HomeUiState.Idle -> LoadingState()
        is HomeUiState.Error -> {
            val msg = (uiState as HomeUiState.Error).message
            ErrorState(msg)
        }
        else -> { /* Success/NotFound handled above via LaunchedEffect */ }
    }
}

