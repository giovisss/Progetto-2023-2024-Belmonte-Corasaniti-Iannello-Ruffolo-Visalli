package com.example.jokiandroid

import androidx.compose.foundation.background
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.model.getFakeGames
import com.example.jokiandroid.viewmodel.CartViewModel

@Composable
fun GameListPage(cartViewModel: CartViewModel = viewModel()) {
    val gameList = getFakeGames()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ){
        LazyColumn(
            content = {
                itemsIndexed(gameList){index: Int, item: Game ->
                    GameItem(item = item, onAddToCart = {
                        cartViewModel.addToCart(item)
                    })
                }
            }
        )
    }
}

@Composable
fun GameItem(item : Game, onAddToCart: (Game) -> Unit = {}){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp)
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color.LightGray
            )
            Text(
                text = item.description,
                fontSize = 15.sp,
                color = Color.White
            )
        }
        Button(onClick = { onAddToCart(item) }) {
            Text(text = "Aggiungi al carrello")
        }
    }
}