package com.aditya1875.baskit.core.presentation.screens.splash

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.AuroraBackground
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.NoiseOverlay
import kotlinx.coroutines.delay

private val SplashBg = Color(0xFF0A0A0F)
private val SplashGreen = Color(0xFF00E676)
private val SplashText = Color(0xFFF5F5F5)

private const val PREFS_NAME = "baskit_prefs"
private const val KEY_ONBOARDING_DONE = "onboarding_done"

@Preview(showBackground = true)
@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current

    val screenAlpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        delay(1400)

        screenAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(300)
        )

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false)

        if (onboardingDone) onNavigateToHome()
        else onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(screenAlpha.value)
            .background(SplashBg),
        contentAlignment = Alignment.Center
    ) {

        AuroraBackground(Modifier.matchParentSize())
        NoiseOverlay(Modifier.matchParentSize())

        Box(
            modifier = Modifier
                .size(300.dp)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SplashGreen.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        Text(
            text = "baskit",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = (-2.5).sp
            ),
            color = SplashText
        )
    }
}