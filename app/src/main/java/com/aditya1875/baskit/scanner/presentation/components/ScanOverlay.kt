package com.aditya1875.baskit.scanner.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.aditya1875.baskit.ui.theme.OverlayDark
import com.aditya1875.baskit.ui.theme.ScanGreen

@Composable
fun ScanOverlay(scanLineY: Float) {
    val frameWidth = 260.dp
    val frameHeight = 300.dp
    val bracketLen = 36.dp
    val bracketStroke = 3.5.dp
    val cornerRadius = 16.dp

    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        val fw = frameWidth.toPx()
        val fh = frameHeight.toPx()
        val cx = size.width / 2f
        val cy = size.height / 2f - 20.dp.toPx()
        val left = cx - fw / 2f
        val top = cy - fh / 2f
        val right = cx + fw / 2f
        val bottom = cy + fh / 2f
        val bLen = bracketLen.toPx()
        val bStroke = bracketStroke.toPx()
        val cr = cornerRadius.toPx()

        // Top
        drawRect(
            OverlayDark.copy(alpha = 0.72f),
            size = androidx.compose.ui.geometry.Size(size.width, top)
        )
        // Bottom
        drawRect(
            OverlayDark.copy(alpha = 0.72f),
            topLeft = Offset(0f, bottom),
            size = androidx.compose.ui.geometry.Size(size.width, size.height - bottom)
        )
        // Left
        drawRect(
            OverlayDark.copy(alpha = 0.72f),
            topLeft = Offset(0f, top),
            size = androidx.compose.ui.geometry.Size(left, fh)
        )
        // Right
        drawRect(
            OverlayDark.copy(alpha = 0.72f),
            topLeft = Offset(right, top),
            size = androidx.compose.ui.geometry.Size(size.width - right, fh)
        )

        val stroke = Stroke(width = bStroke, cap = StrokeCap.Round)
        val green = ScanGreen

        // Top-left
        drawLine(green, Offset(left + cr, top), Offset(left + bLen, top), bStroke, StrokeCap.Round)
        drawLine(green, Offset(left, top + cr), Offset(left, top + bLen), bStroke, StrokeCap.Round)
        // Top-right
        drawLine(
            green,
            Offset(right - bLen, top),
            Offset(right - cr, top),
            bStroke,
            StrokeCap.Round
        )
        drawLine(
            green,
            Offset(right, top + cr),
            Offset(right, top + bLen),
            bStroke,
            StrokeCap.Round
        )
        // Bottom-left
        drawLine(
            green,
            Offset(left, bottom - bLen),
            Offset(left, bottom - cr),
            bStroke,
            StrokeCap.Round
        )
        drawLine(
            green,
            Offset(left + cr, bottom),
            Offset(left + bLen, bottom),
            bStroke,
            StrokeCap.Round
        )
        // Bottom-right
        drawLine(
            green,
            Offset(right, bottom - bLen),
            Offset(right, bottom - cr),
            bStroke,
            StrokeCap.Round
        )
        drawLine(
            green,
            Offset(right - bLen, bottom),
            Offset(right - cr, bottom),
            bStroke,
            StrokeCap.Round
        )

        val lineY = top + (fh * scanLineY)
        val scanLineBrush = Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                green.copy(alpha = 0.7f),
                green,
                green.copy(alpha = 0.7f),
                Color.Transparent
            ),
            startX = left + 20f,
            endX = right - 20f
        )
        drawLine(
            brush = scanLineBrush,
            start = Offset(left + 8f, lineY),
            end = Offset(right - 8f, lineY),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}