package com.aditya1875.baskit.core.presentation.screens.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aditya1875.baskit.Screen
import com.aditya1875.baskit.core.presentation.screens.home.components.EmptyState
import com.aditya1875.baskit.core.presentation.screens.home.components.ErrorState
import com.aditya1875.baskit.core.presentation.screens.home.components.LoadingState
import com.aditya1875.baskit.core.presentation.screens.home.components.ProductDetailsCard
import com.aditya1875.baskit.core.presentation.screens.home.utils.ProductUiState
import com.aditya1875.baskit.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onScanRequested: () -> Unit
) {
    val viewModel: ProductViewModel = viewModel()

    var barcode by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    val scannedCode =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("barcode")

    LaunchedEffect(scannedCode) {
        scannedCode?.let { code ->
            barcode = code
            viewModel.fetchProduct(code)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Baskit",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = { Text("Enter barcode manually") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (barcode.isNotEmpty()) navController.navigate(Screen.ProductLoading.pass(barcode))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Fetch")
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (hasCameraPermission) onScanRequested()
                            else launcher.launch(Manifest.permission.CAMERA)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Scan")
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        when (uiState) {

            ProductUiState.Idle -> {
                EmptyState()
            }

            ProductUiState.Loading -> {
                LoadingState()
            }

            is ProductUiState.Success -> {
                val product = (uiState as ProductUiState.Success).product
                ProductDetailsCard(
                    product = product,
                    onProductClicked = {
                        navController.navigate(
                            Screen.ProductDetail.pass(product.code)
                        )
                    }
                )
            }

            ProductUiState.NotFound -> {
                EmptyState(
                    text = "Product not found. Try another barcode."
                )
            }

            is ProductUiState.Error -> {
                val message = (uiState as ProductUiState.Error).message
                ErrorState(message)
            }
        }
    }
}
