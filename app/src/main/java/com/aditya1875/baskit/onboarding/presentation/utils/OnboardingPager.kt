package com.aditya1875.baskit.onboarding.presentation.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.baskit.onboarding.presentation.components.OnboardingPage
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun OnboardingPager(
    pages: List<OnboardingPage>,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.lastIndex

    Column(
        modifier = modifier.padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(56.dp))
        Text(
            text = "baskit",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp,
                color = Color.White
            )
        )

        Spacer(Modifier.weight(0.5f))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                pagerState = pagerState,
                pageIndex = page
            )
        }

        Spacer(Modifier.weight(0.3f))

        PremiumPagerIndicator(
            count = pages.size,
            pagerState = pagerState
        )

        Spacer(Modifier.height(40.dp))

        AnimatedContent(
            targetState = isLastPage,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
            label = "cta"
        ) { last ->
            Button(
                onClick = {
                    if (last) {
                        onDone()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0A0A0F)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = if (last) "Get started" else "Continue",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }
        }

        AnimatedVisibility(visible = !isLastPage) {
            TextButton(
                onClick = onDone,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    "Skip",
                    color = Color.White.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    pagerState: PagerState,
    pageIndex: Int
) {
    val pageOffset = (pagerState.currentPage - pageIndex) +
            pagerState.currentPageOffsetFraction

    val alpha = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
    val translationX = pageOffset * 60f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.alpha = alpha
                this.translationX = translationX
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(40.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            page.icon()
        }

        Spacer(Modifier.height(40.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1.5).sp,
                color = Color.White
            ),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.55f),
                lineHeight = 26.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 36.dp)
        )
    }
}


@Composable
fun PremiumPagerIndicator(
    count: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeWidth: Dp = 28.dp,
    inactiveWidth: Dp = 8.dp,
    height: Dp = 8.dp,
    gap: Dp = 6.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { i ->
            val isActive = i == pagerState.currentPage
            val width by animateDpAsState(
                targetValue = if (isActive) activeWidth else inactiveWidth,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "dot_width"
            )
            val color by animateColorAsState(
                targetValue = if (isActive) Color.White else Color.White.copy(alpha = 0.3f),
                animationSpec = tween(300),
                label = "dot_color"
            )
            Box(
                modifier = Modifier
                    .height(height)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
