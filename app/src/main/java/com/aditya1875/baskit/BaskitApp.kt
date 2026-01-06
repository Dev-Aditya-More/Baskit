package com.aditya1875.baskit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aditya1875.baskit.core.presentation.screens.home.HomeScreen
import com.aditya1875.baskit.core.presentation.screens.onboarding.OnboardingScreen
import com.aditya1875.baskit.core.presentation.screens.product.ProductDetailScreen
import com.aditya1875.baskit.core.presentation.screens.product.components.ProductLoadingScreen
import com.aditya1875.baskit.core.presentation.screens.product.components.ProductNotFoundScreen
import com.aditya1875.baskit.mlkit.ScanScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")

    object Scan : Screen("screen")

    object ProductLoading : Screen("loading/{code}") {
        fun pass(code: String) = "loading/$code"
    }

    object ProductDetail : Screen("detail/{code}") {
        fun pass(code: String) = "detail/$code"
    }

    object ProductNotFound : Screen("notfound/{code}") {
        fun pass(code: String) = "notfound/$code"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding ->

        NavHost(navController, startDestination, Modifier.padding(padding)) {

            composable(Screen.Onboarding.route) {
                OnboardingScreen(

                    onFinished = {
                        navController.navigate(Screen.Home.route)
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    onScanRequested = {
                        navController.navigate(Screen.Scan.route)
                    }
                )
            }

            composable(Screen.Scan.route) {
                ScanScreen(
                    onBarcodeDetected = { code ->
                        navController.navigate(Screen.ProductLoading.pass(code))
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(Screen.ProductLoading.route) { backStackEntry ->
                val code = backStackEntry.arguments?.getString("code")!!
                ProductLoadingScreen(
                    code = code,
                    onProductFound = {
                        navController.navigate(Screen.ProductDetail.pass(code)) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onProductNotFound = {
                        navController.navigate(Screen.ProductNotFound.pass(code)) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            composable(Screen.ProductDetail.route) { backStackEntry ->
                ProductDetailScreen()
            }

            composable(Screen.ProductNotFound.route) { backStackEntry ->
                val code = backStackEntry.arguments?.getString("code")!!
                ProductNotFoundScreen(code)
            }
        }
    }
}