package com.aditya1875.baskit.product.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.baskit.ui.theme.TextSecondary

@Composable
fun ScoreRing(score: Int, color: Color, size: Dp) {
    val animatedProgress by animateFloatAsState(
        targetValue = score / 100f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "score"
    )

    Box(
        modifier = Modifier
            .size(size)
            .drawBehind {
                val strokeWidth = 10.dp.toPx()
                val radius = (size.toPx() / 2f) - strokeWidth / 2f
                val center = Offset(this.size.width / 2f, this.size.height / 2f)

                // Outer glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color.copy(alpha = 0.18f), Color.Transparent),
                        center = center,
                        radius = radius * 1.35f
                    ),
                    radius = radius * 1.35f,
                    center = center
                )
                // Inner subtle fill
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color.copy(alpha = 0.08f), Color.Transparent),
                        center = center,
                        radius = radius
                    ),
                    radius = radius - strokeWidth,
                    center = center
                )
                // Track
                drawCircle(
                    color = Color.White.copy(alpha = 0.06f),
                    radius = radius,
                    style = Stroke(width = strokeWidth)
                )
                // Arc
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(color.copy(alpha = 0.6f), color, color),
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-2).sp
                ),
                color = color
            )
            Text(
                "OUT OF 100",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.4.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 9.sp
                ),
                color = TextSecondary
            )
        }
    }
}
