package com.aditya1875.baskit.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aditya1875.baskit.R
import com.aditya1875.baskit.onboarding.presentation.utils.AuroraBackground
import com.aditya1875.baskit.onboarding.presentation.utils.NoiseOverlay
import com.aditya1875.baskit.onboarding.presentation.utils.OnboardingPager
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: @Composable () -> Unit
)

fun getOnboardingPages(): List<OnboardingPage> = listOf(
    OnboardingPage(
        title = "Scan it",
        description = "Scan any barcode instantly",
        icon = {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.qrscanner))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(240.dp)
            )
        }
    ),
    OnboardingPage(
        title = "Know it",
        description = "See nutrition, ingredients, and score",
        icon = {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.info))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(240.dp)
            )
        }
    ),
    OnboardingPage(
        title = "Bask it",
        description = "Save products and shop smarter",
        icon = {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.grocery))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(240.dp)
            )
        }
    )
)


@Preview
@Composable
fun OnboardingScreen(onFinished: () -> Unit = {}) {
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .matchParentSize()
                .background(Color(0xFF0A0A0F))
        )
        AuroraBackground(Modifier.matchParentSize())
        NoiseOverlay(Modifier.matchParentSize())

        OnboardingPager(
            pages = getOnboardingPages(),
            onDone = onFinished,
            modifier = Modifier.fillMaxSize()
        )
    }
}