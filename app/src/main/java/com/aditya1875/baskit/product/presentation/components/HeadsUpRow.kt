package com.aditya1875.baskit.product.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.baskit.product.presentation.utils.flaggedIngredientDetail
import com.aditya1875.baskit.ui.theme.DangerRed
import com.aditya1875.baskit.ui.theme.TextSecondary

@Composable
fun HeadsUpRow(ingredient: String) {
    val detail = flaggedIngredientDetail(ingredient)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(
                width = 0.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .drawBehind {
                drawRect(
                    color = DangerRed,
                    size = Size(3.dp.toPx(), size.height)
                )
            }
            .padding(start = 12.dp, top = 10.dp, end = 12.dp, bottom = 10.dp)
    ) {
        Column {
            Text(
                buildString { append("HEADS UP: "); append(detail) },
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = TextSecondary
            )
        }
    }
}