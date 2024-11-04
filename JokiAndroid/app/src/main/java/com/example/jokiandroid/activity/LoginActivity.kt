package com.example.jokiandroid.activity

import TokenManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun LoginActivity(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (isLoading) {
            delay(500) // Simula un ritardo di 2 secondi
            if (TokenManager.isLoggedIn()){
                isLoading = false
            }
        }
        navController.navigate("home")
    }

    Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

    }
}