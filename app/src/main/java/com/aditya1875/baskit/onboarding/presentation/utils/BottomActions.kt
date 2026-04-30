package com.aditya1875.baskit.onboarding.presentation.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomActions(
    currentPage: Int,
    lastIndex: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextButton(
            onClick = onSkip,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        ) {
            Text("Skip")
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth(0.5f),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                if (currentPage < lastIndex) "Next" else "Get Started"
            )
        }
    }
}