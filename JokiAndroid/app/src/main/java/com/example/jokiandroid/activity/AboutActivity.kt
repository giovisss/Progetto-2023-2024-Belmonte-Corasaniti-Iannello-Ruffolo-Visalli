package com.example.jokiandroid.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.jokiandroid.R

@Composable
fun AboutActivity(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Conosci Joki?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Joki è l'applicazione dell'omonimo shop online " +
                    "che ti permette di acquistare i tuoi giochi preferiti" +
                    " a prezzi scontati!",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TeamMember(
            imageResId = R.drawable.giovanni_visalli,
            name = "Giovanni Visalli",
            role = "Sviluppatore"
        )
        TeamMember(
            imageResId = R.drawable.noemi_ruffolo,
            name = "Noemi Ruffolo",
            role = "Sviluppatrice"
        )
        TeamMember(
            imageResId = R.drawable.marco_iannello,
            name = "Marco Iannello",
            role = "Sviluppatore"
        )
        TeamMember(
            imageResId = R.drawable.kevin_corasaniti,
            name = "Kevin Corasaniti",
            role = "Sviluppatore"
        )
        TeamMember(
            imageResId = R.drawable.andrea_belmonte,
            name = "Andrea Belmonte",
            role = "Sviluppatore"
        )
    }
}

@Composable
fun TeamMember(imageResId: Int, name: String, role: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = role, color = Color.Gray, fontSize = 14.sp)
        }
    }
}