package com.aditya1875.baskit.core.presentation.screens.onboarding.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedPagerDots(
    count: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { i ->
            val size by animateDpAsState(
                if (i == currentPage) 12.dp else 8.dp,
                label = ""
            )
            val color by animateColorAsState(
                if (i == currentPage)
                    MaterialTheme.colorScheme.secondary
                else
                    Color.Gray.copy(alpha = 0.4f),
                label = ""
            )

            Box(
                Modifier
                    .padding(4.dp)
                    .size(size)
                    .background(color, CircleShape)
            )
        }
    }
}