package com.example.jokiandroid.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jokiandroid.R
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.viewmodel.CartViewModel
import com.example.jokiandroid.viewmodel.WishlistViewModel

@Composable
fun SingleWishlistActivity(navController: NavController, wishlistName: String, wishlistViewModel: WishlistViewModel, cartViewModel: CartViewModel) {
    val singleWishlist = wishlistViewModel.singleWishlist.observeAsState()
    var gameList = singleWishlist.value?.games ?: emptyList()


    LaunchedEffect(Unit) {
        wishlistViewModel.getSingleWishlist(wishlistName)
        gameList = singleWishlist.value?.games ?: emptyList()
    }

    Column (
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)

    ){

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            itemsIndexed(gameList) { _: Int, game: Game ->
                WishlistsGameItem(
                    item = game,
                    wishlistViewModel = wishlistViewModel,
                    wishlistName = wishlistName,
                    onAddToCart = { cartViewModel.addGame(it) }
                )
            }
        }


    }

}

@Composable
fun WishlistsGameItem(item : Game, onGameClick: (Game) -> Unit = {}, onAddToCart: (Game) -> Unit = {}, wishlistViewModel: WishlistViewModel, wishlistName: String) {



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

            Spacer(modifier = Modifier.padding(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {

                Button(
                    onClick = {
                        wishlistViewModel.removeGameFromWishlist(wishlistName, item.id)
                        wishlistViewModel.getSingleWishlist(wishlistName)
                    },
                    modifier = Modifier
                        .weight(.5f)
                ) {
                    AsyncImage(
                        model = R.drawable.delete,
                        contentDescription = "Rimuovi gioco",
                    )
                }

                Button(
                    onClick = { onAddToCart(item) },
                    modifier = Modifier
                        .weight(.5f)
                ) {
                    AsyncImage(
                        model = R.drawable.add_shopping_cart,
                        contentDescription = "Aggiungi al carrello",
                    )
                }

            }

        }
    }
}
