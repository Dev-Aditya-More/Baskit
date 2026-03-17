package com.aditya1875.baskit.core.presentation.screens.onboarding.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import kotlin.random.Random

@Composable
fun AuroraBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")

    val offset1X by infiniteTransition.animateFloat(
        initialValue = -300f, targetValue = 300f,
        animationSpec = infiniteRepeatable(tween(7000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "o1x"
    )
    val offset1Y by infiniteTransition.animateFloat(
        initialValue = 200f, targetValue = -200f,
        animationSpec = infiniteRepeatable(tween(9000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "o1y"
    )
    val offset2X by infiniteTransition.animateFloat(
        initialValue = 150f, targetValue = -250f,
        animationSpec = infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Reverse),
        label = "o2x"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(5000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Primary green blob
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00FF7F).copy(alpha = 0.35f),
                    Color.Transparent
                ),
                center = Offset(w * 0.25f + offset1X, h * 0.25f + offset1Y),
                radius = w * pulse
            )
        )

        // Secondary teal blob
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00E5FF).copy(alpha = 0.18f),
                    Color.Transparent
                ),
                center = Offset(w * 0.75f + offset2X, h * 0.65f),
                radius = w * 1.1f
            )
        )

        // Accent white shimmer
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.10f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f - offset1X * 0.3f, h * 0.5f),
                radius = w * 0.9f
            )
        )
    }
}


@Composable
fun NoiseOverlay(modifier: Modifier = Modifier) {
    // Pre-generate noise positions once
    val noisePoints = remember {
        (0..2000).map {
            Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat() * 0.025f + 0.005f)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        noisePoints.forEach { (xFrac, yFrac, alpha) ->
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = 1f,
                center = Offset(xFrac * size.width, yFrac * size.height),
                blendMode = BlendMode.Overlay
            )
        }
    }
}
