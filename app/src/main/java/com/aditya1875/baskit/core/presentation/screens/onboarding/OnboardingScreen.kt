package com.aditya1875.baskit.core.presentation.screens.onboarding

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.aditya1875.baskit.core.presentation.screens.onboarding.components.getOnboardingPages
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.AuroraBackground
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.NoiseOverlay
import com.aditya1875.baskit.core.presentation.screens.onboarding.utils.OnboardingPager
import androidx.core.content.edit

@Preview
@Composable
fun OnboardingScreen(onFinished: () -> Unit = {}) {

    val context = LocalContext.current
    Box(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        AuroraBackground(Modifier.matchParentSize())
        NoiseOverlay(Modifier.matchParentSize())
        OnboardingPager(
            pages = getOnboardingPages(),
            onDone = {
                context.getSharedPreferences("baskit_prefs", Context.MODE_PRIVATE)
                    .edit {
                        putBoolean("onboarding_done", true)
                    }
                onFinished()
            },
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        )
    }
}