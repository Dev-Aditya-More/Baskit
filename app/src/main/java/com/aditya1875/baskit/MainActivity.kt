package com.aditya1875.baskit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aditya1875.baskit.ui.theme.BaskitTheme
import com.aditya1875.baskit.utils.PreferencesManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val preferencesManager: PreferencesManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaskitTheme {

                val isOnboardingDone = preferencesManager.isOnboardingDone.collectAsState(initial = false)
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    startDestination = if (isOnboardingDone.value) Screen.Home.route else Screen.Onboarding.route
                )
            }
        }
    }
}