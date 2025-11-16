package com.aditya1875.baskit.core.presentation.screens.onboarding.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import kotlin.random.Random

@Composable
fun AuroraBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 200f,
        targetValue = -200f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00FF7F).copy(alpha = 0.45f),
                    Color.Transparent
                ),
                center = Offset(width * 0.3f + offsetX, height * 0.3f + offsetY),
                radius = width * 1.2f
            )
        )

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.22f),
                    Color.Transparent
                ),
                center = Offset(width * 0.55f - offsetX, height * 0.6f - offsetY),
                radius = width * 1.4f
            )
        )

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00FF7F).copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = Offset(width * 0.8f, height * 0.2f),
                radius = width * 1.3f
            )
        )
    }
}

@Composable
fun NoiseOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val noisePaint = Paint().apply {
            this.color = Color.White
            this.alpha = 8f
            this.blendMode = BlendMode.Overlay
        }

        for (i in 0..3000) {
            val x = Random.nextFloat() * size.width
            val y = Random.nextFloat() * size.height

            drawCircle(
                color = Color.White.copy(alpha = 0.02f),
                radius = 1.2f,
                center = Offset(x, y)
            )
        }
    }
}
