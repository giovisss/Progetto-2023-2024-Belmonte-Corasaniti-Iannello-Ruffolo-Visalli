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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jokiandroid.R
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.model.Wishlist
import com.example.jokiandroid.service.WishlistApiService
import com.example.jokiandroid.utility.IPManager
import com.example.jokiandroid.viewmodel.CartViewModel
import com.example.jokiandroid.viewmodel.WishlistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GameListPage(wishlistViewModel: WishlistViewModel, gameViewModel: GameViewModel, cartViewModel: CartViewModel, navController: NavController) {
    val games by gameViewModel.games.observeAsState(emptyList())
    var showModal by remember { mutableStateOf(false) }
    var selectedGame by remember { mutableStateOf<Game?>(null) }

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
                        onAddToCart = { cartViewModel.addGame(it) },
                        itemAddToWishlist = {
                            showModal = true
                            selectedGame = it},
                        onGameClick = { navController.navigate("game_detail/${game.id}") }
                    )
                    if (showModal) {
                        selectedGame?.id?.let {
                            AvalibleWishlistsModal(
                                wishlistViewModel = wishlistViewModel,
                                onDismiss = { showModal = false },
                                gameId = it,
                                modalAddToWishlist = { wishlistName, gameID: String ->
                                    wishlistViewModel.addGameToWishlist(wishlistName, gameID)
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun GameItem(item : Game, onAddToCart: (Game) -> Unit = {}, itemAddToWishlist: (Game) -> Unit = {}, onGameClick: (Game) -> Unit = {}){
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
        Column(
            modifier = Modifier
                .weight(.75f)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = "${IPManager.BACKEND_IMAGES}/${item.url1}",
                contentDescription = item.title,
                //error = painterResource(id = R.drawable.games_image), // Uncomment and replace with your error image resource
                //placeholder = painterResource(id = R.drawable.games_image), // Uncomment and replace with your placeholder image resource
                modifier = Modifier
                    .fillMaxSize(.5f)
                    .align(Alignment.CenterHorizontally)
            )
            Row {
                Button(
                    onClick = { onAddToCart(item) },
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = R.drawable.add_shopping_cart,
                        contentDescription = "Aggiungi al carrello",
                    )
                }
                Button(
                    onClick = { itemAddToWishlist(item) },
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = R.drawable.heart,
                        contentDescription = "Aggiungi alla lista dei desideri",
                    )
                }
            }
        }
    }
}

@Composable
fun AvalibleWishlistsModal(
    gameId: String,
    wishlistViewModel: WishlistViewModel,
    onDismiss: () -> Unit, // Funzione per chiudere il modal
    modalAddToWishlist: (String, String) -> Unit // Funzione per aggiungere il gioco alla wishlist
) {
    wishlistViewModel.loadWishlists()
    val wishlists by wishlistViewModel.wishlists.observeAsState(emptyList())
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Log.d("porcodio", "Game ID: $gameId")
                Text("Aggiungi alla Wishlist", style = MaterialTheme.typography.bodyMedium)

                LazyColumn(
                    content = {
                        itemsIndexed(wishlists) { index: Int, wishlist: Wishlist ->
                            Text(
                                text = wishlist.wishlistName,
                                modifier = Modifier.clickable { modalAddToWishlist(wishlist.wishlistName, gameId) }
                            )
                        }
                    }
                )
            }
        }
    }
}