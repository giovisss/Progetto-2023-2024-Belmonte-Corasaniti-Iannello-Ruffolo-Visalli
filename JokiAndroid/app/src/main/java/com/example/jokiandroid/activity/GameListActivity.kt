package com.example.jokiandroid.activity

import GameViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.viewmodel.CartViewModel

@Composable
fun GameListPage(gameViewModel: GameViewModel, cartViewModel: CartViewModel, navController: NavController) {
    val games by gameViewModel.games.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        LazyColumn(
            content = {
                itemsIndexed(games) { index: Int, game: Game ->
                    GameItem(
                        item = game,
//                        onAddToCart = { cartViewModel.addGame(it) },
                        onGameClick = { navController.navigate("game_detail/${game.id}") }
                    )
                }
            }
        )
    }
}

@Composable
fun GameItem(item : Game, onAddToCart: (Game) -> Unit = {}, onGameClick: (Game) -> Unit = {}){
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
            if (isSystemInDarkTheme()) {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    color = Color.Black
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
                    color = Color.White
                )
                Text(
                    text = item.description,
                    fontSize = 15.sp,
                    color = Color.LightGray
                )
            }
        }
        Button(onClick = { onAddToCart(item) }) {
            Text(text = "Aggiungi al carrello")
        }
    }
}