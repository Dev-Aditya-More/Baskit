package com.aditya1875.baskit.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.aditya1875.baskit.history.presentation.screens.HistoryScreen
import com.aditya1875.baskit.home.presentation.screen.HomeScreen
import com.aditya1875.baskit.insights.presentation.screens.InsightsScreen
import com.aditya1875.baskit.onboarding.presentation.components.OnboardingScreen
import com.aditya1875.baskit.product.presentation.components.ProductLoadingScreen
import com.aditya1875.baskit.product.presentation.components.ProductNotFoundScreen
import com.aditya1875.baskit.product.presentation.screens.ProductDetailScreen
import com.aditya1875.baskit.profile.presentation.screens.ProfileScreen
import com.aditya1875.baskit.scanner.presentation.screens.ScanScreen
import com.aditya1875.baskit.splash.presentation.screens.SplashScreen
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BaskitBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onScanClick = { navController.navigate(Screen.Scan.route) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else Dp(
                        0f
                    )
                )
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
                    onFetchBarcode = { code -> navController.navigate(Screen.ProductGraph.pass(code)) }
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    onScanClick = { scan ->
                        navController.navigate(Screen.ProductGraph.pass(scan.barcode))
                    }
                )
            }
            composable(Screen.Insights.route) { InsightsScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }

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
                    val viewModel: ProductViewModel =
                        koinViewModel(viewModelStoreOwner = parentEntry)
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
                    val viewModel: ProductViewModel =
                        koinViewModel(viewModelStoreOwner = parentEntry)
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
                ProductNotFoundScreen(code = code, onBack = { navController.popBackStack() })
            }
        }
    }
}