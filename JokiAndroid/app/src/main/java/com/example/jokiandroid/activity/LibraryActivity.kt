package com.example.jokiandroid.activity

import GameViewModel
import TokenManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jokiandroid.model.Game

@Composable
fun LibraryActivity(navController: NavController, gameViewModel: GameViewModel) {
    val gamesResponse by gameViewModel.libraryGames.observeAsState()
    val isLoading by gameViewModel.isLoading.collectAsState()
    var showLoginDialog by remember { mutableStateOf(false) }
    var showEmptyLibraryDialog by remember { mutableStateOf(false) }
    val isLoggedIn by TokenManager.token.collectAsState()

    if (isLoggedIn == null) {
        LoginRequiredDialog(
            onDismissRequest = { /* Non fare nulla, l'utente deve effettuare il login */ },
            onConfirmClick = { navController.popBackStack() }
        )
    } else {
        val canMakeDecisions = !isLoading && gamesResponse != null

        LaunchedEffect(Unit) {
            gameViewModel.fetchGamesByUser()
        }

        val gameList = gamesResponse?.let { response ->
            if (response.isSuccessful) {
                Log.d("LibraryActivity", "GameList: $response")
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } ?: emptyList()

        LaunchedEffect(canMakeDecisions) {
            if (canMakeDecisions) {
                showEmptyLibraryDialog = gameList.isEmpty()
            }
        }

        if (!canMakeDecisions) {
            LoadingIndicator()
        } else {
            if (showEmptyLibraryDialog) {
                EmptyListDialog(
                    onDismissRequest = { showEmptyLibraryDialog = false },
                    onConfirmClick = { navController.popBackStack() }
                )
            } else {
                GameList(gameList, navController)
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun GameList(gameList: List<Game>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        itemsIndexed(gameList) { _, game ->
            LibraryItem(
                item = game,
                onGameClick = { navController.navigate("game_detail/${game.id}") }
            )
        }
    }
}

@Composable
fun EmptyListDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Libreria vuota") },
        text = { Text("Non hai ancora aggiunto giochi alla tua libreria.") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmClick()
                    onDismissRequest()
                }
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun LoginRequiredDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Login richiesto") },
        text = { Text("Per visualizzare la pagina devi effettuare il login.") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmClick()
                    onDismissRequest()
                }
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun LibraryItem(item : Game, onGameClick: (Game) -> Unit = {}){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp)
            .clickable { onGameClick(item) }
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = isSystemInDarkTheme().let { if (it) Color.Black else Color.White },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.padding(5.dp))

            Text(
                text = item.description,
                fontSize = 15.sp,
                color = isSystemInDarkTheme().let { if (it) Color.DarkGray else Color.LightGray }
            )
        }
    }
}
