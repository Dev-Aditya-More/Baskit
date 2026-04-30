package com.aditya1875.baskit.core.navigation

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

    object History : Screen("history")
    object Insights : Screen("insights")

    object Profile : Screen("profile")
}

val bottomNavRoutes = setOf(
    Screen.Home.route,
    Screen.History.route,
    Screen.Insights.route,
    Screen.Profile.route
)