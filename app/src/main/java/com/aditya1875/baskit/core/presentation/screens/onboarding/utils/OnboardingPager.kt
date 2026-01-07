package com.aditya1875.baskit.core.presentation.screens.onboarding.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aditya1875.baskit.core.presentation.screens.onboarding.components.OnboardingPage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                    pagerState.scrollToPage(0)
                }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize(),
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
                    style = MaterialTheme.typography.headlineMedium,
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    if (pagerState.currentPage < pages.lastIndex) "Next" else "Get Started"
                )
            }
        }
    }
}
