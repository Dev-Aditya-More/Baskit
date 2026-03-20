package com.aditya1875.baskit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.aditya1875.baskit.core.presentation.screens.home.HomeScreen
import com.aditya1875.baskit.core.presentation.screens.onboarding.OnboardingScreen
import com.aditya1875.baskit.core.presentation.screens.product.ProductDetailScreen
import com.aditya1875.baskit.core.presentation.screens.product.components.ProductLoadingScreen
import com.aditya1875.baskit.core.presentation.screens.product.components.ProductNotFoundScreen
import com.aditya1875.baskit.core.presentation.screens.splash.SplashScreen
import com.aditya1875.baskit.mlkit.ScanScreen
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Scan : Screen("scan")

    object ProductGraph : Screen("product_graph/{code}") {
        fun pass(code: String) = "product_graph/$code"
    }
    object ProductLoading : Screen("loading/{code}")

    object ProductDetail : Screen("detail")

    object ProductNotFound : Screen("notfound/{code}") {
        fun pass(code: String) = "notfound/$code"
    }
}

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onFetchBarcode = { code ->
                    navController.navigate(Screen.ProductGraph.pass(code))
                },
                onOpenCamera = {
                    navController.navigate(Screen.Scan.route)
                }
            )
        }

        composable(Screen.Scan.route) {
            ScanScreen(
                onBarcodeDetected = { code ->
                    navController.navigate(Screen.ProductGraph.pass(code)) {
                        popUpTo(Screen.Scan.route) { inclusive = true }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        navigation(
            route = Screen.ProductGraph.route,
            startDestination = Screen.ProductLoading.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) {
            composable(Screen.ProductLoading.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.ProductGraph.route)
                }
                val viewModel: ProductViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
                val code = backStackEntry.arguments?.getString("code") ?: ""

                ProductLoadingScreen(
                    viewModel = viewModel,
                    navController = navController,
                    code = code,
                    onProductNotFound = {
                        navController.navigate(Screen.ProductNotFound.pass(code)) {
                            popUpTo(Screen.ProductLoading.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.ProductDetail.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.ProductGraph.route)
                }
                val viewModel: ProductViewModel = koinViewModel(viewModelStoreOwner = parentEntry)

                ProductDetailScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Screen.ProductNotFound.route,
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""
            ProductNotFoundScreen(
                code = code,
                onBack = { navController.popBackStack() }
            )
        }
    }
}