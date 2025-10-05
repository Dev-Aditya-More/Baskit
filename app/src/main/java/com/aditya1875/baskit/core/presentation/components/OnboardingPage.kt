package com.aditya1875.baskit.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aditya1875.baskit.R
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
        description = "Quickly scan product barcodes to fetch nutrition info.",
        icon = {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.qrscanner))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(300.dp)
            )
        }
    ),
    OnboardingPage(
        title = "Know it",
        description = "View facts, warnings, and tips instantly.",
        icon = {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.info))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(300.dp)
            )
        }
    ),
    OnboardingPage(
        title = "Bask it",
        description = "Save and shop smarter with your basket.",
        icon = {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.grocery))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(300.dp)
            )
        }
    )
)