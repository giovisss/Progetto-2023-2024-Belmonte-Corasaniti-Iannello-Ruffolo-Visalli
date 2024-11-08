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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.jokiandroid.utility.IPManager
import coil.compose.AsyncImage


@Composable
fun LibraryActivity(navController: NavController, gameViewModel: GameViewModel) {
    val games by gameViewModel.libraryGames.observeAsState()
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
        val canMakeDecisions = !isLoading && games != null

        LaunchedEffect(Unit) {
            gameViewModel.fetchGamesByUser()
        }


        LaunchedEffect(canMakeDecisions) {
            if (canMakeDecisions) {
                showEmptyLibraryDialog = games?.isEmpty() ?: false
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
                games?.let { GameList(it, navController) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameList(gameList: List<Game>, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }


    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("Cerca") },
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

    val filteredGames = gameList.filter { it.title.contains(searchQuery, ignoreCase = true) }


    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        itemsIndexed(filteredGames) { _, game ->
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
fun LibraryItem(item: Game, onGameClick: (Game) -> Unit = {}) {
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
            modifier = Modifier
                .weight(.75f)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = "${IPManager.BACKEND_IMAGES}/${item.url1}",
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
                    .padding(end = 10.dp)
            )

        }

        Column(
            modifier = Modifier.weight(1f)
        ) {



            if (isSystemInDarkTheme()) {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp)
                )
                Text(
                    text = item.description,
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )
            } else {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp)
                )
                Text(
                    text = item.description,
                    fontSize = 15.sp,
                    color = Color.LightGray
                )
            }
        }

    }
}

