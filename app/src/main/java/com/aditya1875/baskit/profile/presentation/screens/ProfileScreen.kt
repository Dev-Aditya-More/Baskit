package com.aditya1875.baskit.profile.presentation.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aditya1875.baskit.history.presentation.viewmodel.ScanHistoryViewModel
import com.aditya1875.baskit.history.presentation.viewmodel.WeeklyStats
import com.aditya1875.baskit.profile.presentation.viewmodel.AuthViewModel
import com.aditya1875.baskit.ui.theme.*
import com.google.firebase.auth.FirebaseUser
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    historyViewModel: ScanHistoryViewModel = koinViewModel()
) {
    val user by authViewModel.currentUser.collectAsStateWithLifecycle()
    val monthly by historyViewModel.monthCount.collectAsStateWithLifecycle()
    val weekly by historyViewModel.weeklyStats.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            authViewModel.handleSignInResult(result.data)
        }
    }

    Box(Modifier.fillMaxSize().background(Color(0xFF0D0D0D))) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            if (user != null) {
                SignedInContent(
                    user = user!!,
                    monthly = monthly,
                    weekly = weekly,
                    onSignOut = { authViewModel.signOut() }
                )
            } else {
                SignedOutContent(
                    onSignIn = { launcher.launch(authViewModel.getSignInIntent()) }
                )
            }
        }
    }
}

@Composable
private fun SignedInContent(
    user: FirebaseUser,
    monthly: Int,
    weekly: WeeklyStats,
    onSignOut: () -> Unit
) {
    // Header with avatar
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(GreenAccent.copy(alpha = 0.10f), Color.Transparent),
                    center = Offset(540f, 0f),
                    radius = 700f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(24.dp))
            Box(
                Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(2.dp, GreenAccent.copy(alpha = 0.45f), CircleShape)
            ) {
                if (user.photoUrl != null) {
                    AsyncImage(
                        model = user.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        Modifier.fillMaxSize().background(CardBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            user.displayName?.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = GreenAccent
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                user.displayName ?: "User",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                user.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFF4285F4).copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    "Connected via Google",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = Color(0xFF4285F4)
                )
            }
        }
    }

    // Stats
    Spacer(Modifier.height(4.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileStatCard(
            value = monthly.toString(),
            label = "Scans This Month",
            modifier = Modifier.weight(1f)
        )
        ProfileStatCard(
            value = if (weekly.totalScans > 0) weekly.avgScore.toString() else "--",
            label = "Weekly Avg Score",
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(Modifier.height(32.dp))
    HorizontalDivider(
        Modifier.padding(horizontal = 20.dp),
        color = Color.White.copy(alpha = 0.06f)
    )
    Spacer(Modifier.height(24.dp))

    OutlinedButton(
        onClick = onSignOut,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF4444)),
        border = BorderStroke(1.dp, Color(0xFFFF4444).copy(alpha = 0.4f))
    ) {
        Text(
            "Sign Out",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
        )
    }
    Spacer(Modifier.height(32.dp))
}

@Composable
private fun SignedOutContent(onSignIn: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        Box(
            Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(GreenAccent.copy(alpha = 0.10f))
                .border(1.dp, GreenAccent.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Person, null,
                tint = GreenAccent,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "Your Health Profile",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Sign in to sync your scan history and track your nutrition journey across devices.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = onSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Box(
                Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4285F4)),
                contentAlignment = Alignment.Center
            ) {
                Text("G", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
            }
            Spacer(Modifier.width(12.dp))
            Text(
                "Sign in with Google",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF1F1F1F)
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileStatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = GreenAccent
            )
            Spacer(Modifier.height(2.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = TextSecondary
            )
        }
    }
}
