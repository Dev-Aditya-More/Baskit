package com.aditya1875.baskit.core.presentation.screens.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InfoChip(label: String, value: String?) {

    val chipColor =
        when (value?.lowercase()) {
            "a", "b" -> Color.Green
            "c" -> Color.Yellow
            "d", "e" -> Color.Red
            else -> MaterialTheme.colorScheme.primary
        }

    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor.copy(alpha = 0.18f)
    ) {
        Text(
            text = "$label: ${value ?: "N/A"}",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}