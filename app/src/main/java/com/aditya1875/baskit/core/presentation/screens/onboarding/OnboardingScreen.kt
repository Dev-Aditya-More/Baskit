package com.aditya1875.baskit.core.presentation.screens.onboarding

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.aditya1875.baskit.core.presentation.screens.onboarding.components.getOnboardingPages
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.AuroraBackground
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.NoiseOverlay
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.OnboardingPager

@Preview
@Composable
fun OnboardingScreen(onFinished: () -> Unit = {}) {

    Box(Modifier.fillMaxSize()) {
        AuroraBackground(Modifier.matchParentSize())
        NoiseOverlay(Modifier.matchParentSize())
        OnboardingPager(
            pages = getOnboardingPages(),
            onDone = onFinished
        )
    }
}