package com.aditya1875.baskit.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.baskit.ui.theme.NavActive
import com.aditya1875.baskit.ui.theme.NavBg
import com.aditya1875.baskit.ui.theme.NavInactive
import com.aditya1875.baskit.ui.theme.ScanGreen

data class NavItem(val route: String, val icon: ImageVector, val label: String)

val navItems = listOf(
    NavItem(Screen.Home.route, Icons.Outlined.GridView, "Home"),
    NavItem(Screen.History.route, Icons.Outlined.History, "History"),
    NavItem(Screen.Insights.route, Icons.AutoMirrored.Outlined.ShowChart, "Insights"),
    NavItem(Screen.Profile.route, Icons.Outlined.Person, "Profile")
)

@Composable
fun BaskitBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 12.dp)
            .height(90.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // The main navigation bar background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = Color.Black.copy(alpha = 0.5f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .clip(RoundedCornerShape(36.dp))
                .background(NavBg)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // First two items
                navItems.take(2).forEach { item ->
                    NavTab(
                        item = item,
                        isActive = currentRoute == item.route,
                        onClick = { onNavigate(item.route) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Middle spacer for FAB to sit over
                Spacer(Modifier.weight(0.8f))

                // Last two items
                navItems.drop(2).forEach { item ->
                    NavTab(
                        item = item,
                        isActive = currentRoute == item.route,
                        onClick = { onNavigate(item.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Floating Action Button (Scan)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 2.dp)
                .size(62.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = ScanGreen.copy(alpha = 0.6f),
                    spotColor = ScanGreen.copy(alpha = 0.4f)
                )
                .clip(CircleShape)
                .background(ScanGreen)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onScanClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.QrCodeScanner,
                contentDescription = "Scan",
                tint = Color(0xFF0A0A0F),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun NavTab(
    item: NavItem,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = if (isActive) NavActive else NavInactive.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = item.label,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isActive) NavActive else NavInactive.copy(alpha = 0.8f),
            letterSpacing = 0.1.sp
        )
    }
}
