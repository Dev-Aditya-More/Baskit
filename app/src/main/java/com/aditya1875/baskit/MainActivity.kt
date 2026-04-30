package com.aditya1875.baskit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.aditya1875.baskit.core.navigation.AppNavGraph
import com.aditya1875.baskit.core.navigation.Screen
import com.aditya1875.baskit.ui.theme.BaskitTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            BaskitTheme {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    startDestination = Screen.Splash.route
                )
            }
        }
    }
}