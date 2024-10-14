package com.example.jokiandroid.activity

import GameViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.utility.IPManager

@Composable
fun EditGamesActivity(navController: NavController, gameViewModel: GameViewModel, gameId: String) {
    val games = gameViewModel.games.observeAsState()

    if(games.value == null || games.value!!.isEmpty()) {
        AlertDialog(
            onDismissRequest = { navController.popBackStack() },
            title = { Text("No games found") },
            text = { Text("There are no games to show") },
            confirmButton = {
                Button(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("Go back")
                }
            }
        )
    }

    if (gameId == "") {
        LazyColumn {
            itemsIndexed(games.value!!) { index: Int, game: Game ->
                EditGameListItem(
                    item = game,
                    onGameClick = { navController.navigate("edit_games/${game.id}") }
                )
            }
        }
    } else {
        EditGameContent(navController, gameViewModel, gameId)
    }
}

@Composable
fun EditGameListItem(item : Game, onGameClick: (Game) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp)
            .clickable { onGameClick(item) }
    ) {
        Column(
            modifier = Modifier.weight(.75f)
        ) {
            var color = Color.White
            if (isSystemInDarkTheme()) color = Color.Black

            Text(
                text = item.title,
                fontSize = 20.sp,
                color = color
            )
            Text(
                text = item.description,
                fontSize = 15.sp,
                color = color
            )
        }
        Column(
            modifier = Modifier
                .weight(.25f)
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            AsyncImage(
                model = "${IPManager.BACKEND_IMAGES}/${item.url1}",
                contentDescription = item.title,
                //error = painterResource(id = R.drawable.games_image), // Uncomment and replace with your error image resource
                //placeholder = painterResource(id = R.drawable.games_image), // Uncomment and replace with your placeholder image resource
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGameContent(navController: NavController, gameViewModel: GameViewModel, gameId: String) {
    val game by gameViewModel.gameDetails.observeAsState()

    LaunchedEffect(gameId) {
        gameViewModel.fetchGameById(gameId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Modifica gioco")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (game != null) {
                // TODO: Implementa la modifica del gioco
                Text(
                    text = game!!.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else {
                // Gestisci il caso in cui il gioco non è stato trovato o si è verificato un errore
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Gioco non trovato o errore durante il caricamento")
                    Button(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Go back")
                    }
                }
            }
        }
    }
}