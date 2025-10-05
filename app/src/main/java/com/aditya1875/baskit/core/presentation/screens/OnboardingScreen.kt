package com.aditya1875.baskit.core.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aditya1875.baskit.core.presentation.components.OnboardingPage
import com.aditya1875.baskit.core.presentation.components.getOnboardingPages
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {

    val pages = getOnboardingPages()
    OnboardingPager(pages = pages, onDone = onFinished)
}

@Composable
fun OnboardingPager(
    pages : List<OnboardingPage>,
    onDone: () -> Unit,
    autoScrollDelay: Long = 2000L
) {

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        while (true) {
            delay(autoScrollDelay)
            scope.launch {
                if (pagerState.currentPage < pages.lastIndex) {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                } else {
                    pagerState.scrollToPage(0) // loop to start
                }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFD4FC79), Color(0xFF96E6A1))
                )
            )
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier.weight(1f),
            userScrollEnabled = true
        ) { page ->

            val onboardingPage = pages[page]
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                onboardingPage.icon()
                Spacer(Modifier.height(24.dp))
                Text(
                    onboardingPage.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
                Text(onboardingPage.description, style = MaterialTheme.typography.bodyMedium)
            }
        }

        AnimatedPagerDots(
            count = pages.size,
            currentPage = pagerState.currentPage
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {
                scope.launch { pagerState.scrollToPage(pages.lastIndex) }
            }) {
                Text("Skip")
            }
            Button(onClick = {
                scope.launch {
                    if (pagerState.currentPage < pages.lastIndex)
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    else
                        onDone()
                }
            },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.height(48.dp)
            ) {
                Text(if (pagerState.currentPage < pages.lastIndex) "Next" else "Get Started")
            }
        }
    }
}


@Composable
fun AnimatedPagerDots(count: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        for (i in 0 until count) {
            // Animate size
            val size by animateDpAsState(targetValue = if (i == currentPage) 12.dp else 8.dp)
            // Animate color
            val color by animateColorAsState(
                targetValue = if (i == currentPage) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f)
            )

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(size)
                    .background(color, CircleShape)
            )
        }
    }
}

