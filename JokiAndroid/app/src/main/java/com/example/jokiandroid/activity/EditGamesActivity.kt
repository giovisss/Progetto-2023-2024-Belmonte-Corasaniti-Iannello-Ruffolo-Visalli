package com.example.jokiandroid.activity

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EditGamesActivity(navController: NavController) {
    Surface {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            text = "Edit Games"
        )
    }
}