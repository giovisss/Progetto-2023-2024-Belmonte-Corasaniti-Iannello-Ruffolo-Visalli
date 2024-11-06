package com.example.jokiandroid.activity

import GameViewModel
import TokenManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jokiandroid.R
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.model.Wishlist
import com.example.jokiandroid.utility.IPManager
import com.example.jokiandroid.utility.ToastString
import com.example.jokiandroid.viewmodel.CartViewModel
import com.example.jokiandroid.viewmodel.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListPage(wishlistViewModel: WishlistViewModel, gameViewModel: GameViewModel, cartViewModel: CartViewModel, navController: NavController) {
    val games by gameViewModel.games.observeAsState(emptyList())
    var showModal by remember { mutableStateOf(false) }
    var selectedGame by remember { mutableStateOf<Game?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Cerca") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        val filteredGames = games.filter { it.title.contains(searchQuery, ignoreCase = true) }

        LazyColumn(
            content = {
                itemsIndexed(filteredGames) { _: Int, game: Game ->
                    GameItem(
                        item = game,
                        onAddToCart = { cartViewModel.addGame(it) },
                        itemAddToWishlist = {
                            showModal = true
                            selectedGame = it
                        },
                        onGameClick = { navController.navigate("game_detail/${game.id}") }
                    )
                }
            }
        )

            if (showModal) {
                if (TokenManager.isLoggedIn()){
                selectedGame?.id?.let {
                    AvalibleWishlistsModal(
                        gameId = it,
                        wishlistViewModel = wishlistViewModel,
                        onDismiss = { showModal = false },
                        modalAddToWishlist = { wishlistName, gameID: String ->
                            wishlistViewModel.addGameToWishlist(wishlistName, gameID)
                            showModal = false
                        }
                    )
                }
            } else{
                ToastString.setMessage("Devi essere loggato per aggiungere un gioco alla wishlist")
                showModal = false
                }
        }
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
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 5.dp)
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
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 5.dp)
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
    var showModal2 by remember { mutableStateOf(false) }

    if (showModal2) {
        CreaWishlistModal(
            wishlistViewModel = wishlistViewModel,
            onDismiss = { showModal2 = false },
            onCreate = { wishlistName, visibility ->
                Log.d("WishlistsActivity", "Creazione wishlist: $wishlistName, $visibility")
            }
        )
    }
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(16.dp))
            {

                // Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "X",
                        modifier = Modifier
                            .clickable { onDismiss() },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text("Aggiungi il gioco ad una delle tue Wishlist: ", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp))

                LazyColumn(
                    content = {
                        itemsIndexed(wishlists) { _: Int, wishlist: Wishlist ->
                            Button(
                                onClick = { modalAddToWishlist(wishlist.wishlistName, gameId) },
                                modifier = Modifier.fillMaxWidth(1f)
                            ) {
                                Text(wishlist.wishlistName)
                            }
                        }
                    }
                )

                Text(modifier = Modifier.padding(5.dp)
                    , text = "Se non hai una wishlist, creane una!", style = MaterialTheme.typography.bodyMedium)

                Button(
                    onClick = { showModal2 = true },
                    modifier = Modifier.fillMaxWidth(1f)
                ) {
                    Text("Crea Nuova Wishlist")
                }

                Text(modifier = Modifier.padding(3.dp)
                    , text = "*Il gioco verrà aggiunto alla wishlist appena creata", style = MaterialTheme.typography.bodySmall)

            }
        }
    }
}