package com.aditya1875.baskit.scanner.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.baskit.ui.theme.NavBg
import com.aditya1875.baskit.ui.theme.ScanGreen

@Composable
fun ScanBottomBar(onCancel: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(NavBg)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Home" to Icons.Outlined.GridView, "History" to Icons.Outlined.History).forEach { (label, icon) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onCancel() },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(icon, label, tint = Color(0xFF666666), modifier = Modifier.size(22.dp))
                        Spacer(Modifier.height(3.dp))
                        Text(label, fontSize = 10.sp, color = Color(0xFF666666))
                    }
                }
                Spacer(Modifier.weight(1f))
                listOf("Insights" to Icons.AutoMirrored.Outlined.ShowChart, "Profile" to Icons.Outlined.Person).forEach { (label, icon) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onCancel() },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(icon, label, tint = Color(0xFF666666), modifier = Modifier.size(22.dp))
                        Spacer(Modifier.height(3.dp))
                        Text(label, fontSize = 10.sp, color = Color(0xFF666666))
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 6.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(ScanGreen)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* already on scan */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.QrCodeScanner, "Scan", tint = Color(0xFF0A0A0F), modifier = Modifier.size(26.dp))
        }
    }
}