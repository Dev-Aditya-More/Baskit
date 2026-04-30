package com.aditya1875.baskit.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ProfileScreen() {
    Box(Modifier.fillMaxSize().background(Color(0xFF0D0D0D)), Alignment.Center) {
        Text("Profile", color = Color.White, fontWeight = FontWeight.Bold)
    }
}