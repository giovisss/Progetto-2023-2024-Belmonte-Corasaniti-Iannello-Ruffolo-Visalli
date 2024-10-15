package com.example.jokiandroid.activity

import GameViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.utility.Date
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
    var title: String by remember { mutableStateOf("") }
    var description: String by remember { mutableStateOf("") }
    var price: Double by remember { mutableStateOf(0.00) }
    var stock: Int by remember { mutableStateOf(0) }
    var genre: String by remember { mutableStateOf("") }
    var developer: String by remember { mutableStateOf("") }
    var publisher: String by remember { mutableStateOf("") }
    var releaseDateDay: Int by remember { mutableStateOf(1) }
    var releaseDateMonth: Int by remember { mutableStateOf(1) }
    var releaseDateYear: Int by remember { mutableStateOf(1000) }

    LaunchedEffect(gameId) {
        gameViewModel.fetchGameById(gameId)
    }

    LaunchedEffect(game) {
        game?.let {
            title = it.title
            description = it.description
            price = it.price
            stock = it.stock
            genre = it.genre
            developer = it.developer
            publisher = it.publisher
            releaseDateDay = it.formattedReleaseDate.day
            releaseDateMonth = it.formattedReleaseDate.month
            releaseDateYear = it.formattedReleaseDate.year
        }
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
                .padding(16.dp)
        ) {
            if (game != null) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Formatta correttamente il prezzo dato che le librerie non funzionano
                var out = price.toString()
                val pointIndex = out.indexOf('.')
                if (pointIndex != -1) {
                    val decimalPart = out.substring(pointIndex + 1)
                    if (decimalPart.isEmpty()) {
                        out += "0"
                    }
                    if (decimalPart.length < 2) {
                        out += "0"
                    }
                } else {
                    out = "$out.00"
                }
                TextField(
                    value = out,
                    onValueChange = { it ->
                        Log.e("PORCO", it)
                        val acceptedValues = "0123456789."
                        if (it.all { acceptedValues.contains(it) }) {
                            val pointIndex = it.indexOf('.')

                            if (pointIndex != -1) {
                                val decimalPart = it.substring(pointIndex + 1)
                                if (decimalPart.length > 2) {
                                    price = it.substring(0, pointIndex+3).toDouble()
                                } else {
                                    price = it.toDouble()
                                }
                            } else {
                                price = it.toDouble()
                            }
                        }

                    },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = stock.toString(),
                    onValueChange = {
                        if (it.isEmpty()) stock = 0
                        else {
                            val old = stock

                            try {
                                stock = it.toInt()
                            } catch (e: Exception) {
                                stock = old
                            }
                        }
                    },
                    label = { Text("Stock") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = developer,
                    onValueChange = { developer = it },
                    label = { Text("Developer") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = publisher,
                    onValueChange = { publisher = it },
                    label = { Text("Publisher") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    TextField(
                        value = releaseDateDay.toString(),
                        onValueChange = {
                            if (it.isEmpty()) releaseDateDay = 1
                            else {
                                val old = releaseDateDay

                                try {
                                    val new = it.toInt()

                                    if(new > 31) releaseDateDay = 31
                                    else if (new < 1) releaseDateDay = 1
                                    else releaseDateDay = new
                                } catch (e: Exception) {
                                    releaseDateDay = old
                                }
                            }
                        },
                        label = { Text("Day") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = releaseDateMonth.toString(),
                        onValueChange = {
                            if (it.isEmpty()) releaseDateMonth = 1
                            else {
                                val old = releaseDateMonth

                                try {
                                    val new = it.toInt()

                                    if(new > 12) releaseDateMonth = 12
                                    else if (new < 1) releaseDateMonth = 1
                                    else releaseDateMonth = new
                                } catch (e: Exception) {
                                    releaseDateMonth = old
                                }
                            }
                        },
                        label = { Text("Month") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = releaseDateYear.toString(),
                        onValueChange = {
                            if (it.isEmpty()) releaseDateYear = 2000
                            else {
                                val old = releaseDateYear

                                try {
                                    releaseDateYear = it.toInt()
                                } catch (e: Exception) {
                                    releaseDateYear = old
                                }
                            }
                        },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        gameViewModel.updateGame(Game(game!!.id, title, price, description, game!!.imagePath, stock, genre, developer, publisher, Date(releaseDateDay, releaseDateMonth, releaseDateYear).toString()))
                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            } else {
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