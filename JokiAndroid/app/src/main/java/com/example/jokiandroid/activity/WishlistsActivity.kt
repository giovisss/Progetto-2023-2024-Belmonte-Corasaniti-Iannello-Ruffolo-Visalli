package com.example.jokiandroid.activity

import RetrofitInstance
import TokenManager
import android.annotation.SuppressLint
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jokiandroid.R
import com.example.jokiandroid.model.Wishlist
import com.example.jokiandroid.service.WishlistApiService
import com.example.jokiandroid.viewmodel.WishlistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Qui dobbiamo visualizzare le wishlist dell'utente
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WishlistsActivity(navController: NavController, wishlistViewModel: WishlistViewModel) {

    val wishlists by wishlistViewModel.wishlists.observeAsState(emptyList())
    var showModal by remember { mutableStateOf(false) }
    val isLoggedIn by TokenManager.token.collectAsState()

    LaunchedEffect(Unit) {
        wishlistViewModel.loadWishlists()
    }

    Column(modifier = Modifier.fillMaxSize()
        .padding(8.dp)
        .clip(RoundedCornerShape(15.dp))
    ) {
        if (isLoggedIn == null) {
            LoginRequiredDialog(
                onDismissRequest = { /* Non fare nulla, l'utente deve effettuare il login */ },
                onConfirmClick = { navController.popBackStack() })
        } else {

            LazyColumn(
                modifier = Modifier.weight(1f)


            ) {
                itemsIndexed(wishlists) { index, wishlist ->
                    WishlistsItem(
                        wishlistViewModel = wishlistViewModel,
                        item = wishlist,
                        onWishlistClick = {
                            navController.navigate("wishlists/${wishlist.wishlistName}")
                        }
                    )
                }
            }

            Button(
                onClick = { showModal = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                Text(text = "Crea nuova Wishlist")
            }

            if (showModal) {
                CreaWishlistModal(
                    wishlistViewModel = wishlistViewModel,
                    onDismiss = { showModal = false },
                    onCreate = { nome, visibilita ->
                        Log.d("WishlistsActivity", "Creazione wishlist: $nome, $visibilita")
    //                                wishlistViewModel.createWishlist(nome, visibilita)
                        // Qui gestisci la creazione della wishlist
                        // con i dati ricevuti (nome, visibilita)
                    }
                )

            }
        }
    }
}




//singola wishlist visualizzata nella schermata delle wishlist
@Composable
fun WishlistsItem(wishlistViewModel: WishlistViewModel, item : Wishlist, onWishlistClick: (Wishlist) -> Unit = {}) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp)
            .clickable { onWishlistClick(item) }
    ){
        Column(
            modifier = Modifier
                .weight(.75f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = item.wishlistName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = isSystemInDarkTheme().let { if (it) Color.Black else Color.White }
            )
            val visibility = when (item.visibility) {
                0 -> "Privata"
                1 -> "Pubblica"
                2 -> "Solo Amici"
                else -> "Sconosciuta"
            }
            Text(text = "Questa wishlist è $visibility", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.align(Alignment.CenterHorizontally), color = isSystemInDarkTheme().let { if (it) Color.DarkGray else Color.LightGray })
            Text(text = "Contiene ${item.games.size} giochi ", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.align(Alignment.CenterHorizontally), color = isSystemInDarkTheme().let { if (it) Color.DarkGray else Color.LightGray })
        }
        Column(
            modifier = Modifier
                .weight(.25f)
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            Button(
                onClick = {
                    wishlistViewModel.viewModelScope.launch {
                        val response = RetrofitInstance.createApi(
                            WishlistApiService::class.java,
                            TokenManager.getToken()
                        ).deleteWishlist(item.wishlistName)
                        if (response.isSuccessful) {
                            Log.d(
                                "WishlistsActivity",
                                "Wishlist eliminata: ${item.wishlistName}"
                            )
                            wishlistViewModel.loadWishlists()
                        } else {
                            Log.e(
                                "WishlistsActivity",
                                "Errore eliminazione wishlist: ${
                                    response.errorBody()?.string()
                                }"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            ) {
                AsyncImage(
                    model = R.drawable.delete,
                    contentDescription = "Elimina Wishlist",
                )
            }
        }
    }
}

@Composable
fun CreaWishlistModal(
    wishlistViewModel: WishlistViewModel,
    onDismiss: () -> Unit, // Funzione per chiudere il modal
    onCreate: (String, Int) -> Unit // Funzione per creare la wishlist
) {
    var nomeWishlist by remember { mutableStateOf("") }
    var visibilita by remember { mutableIntStateOf(1) }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Crea Nuova Wishlist", style = MaterialTheme.typography.bodyMedium)

                OutlinedTextField(
                    value = nomeWishlist,
                    onValueChange = { nomeWishlist = it },
                    label = { Text("Nome Wishlist") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = visibilita == 0,
                            onClick = { visibilita = 0 }
                        )
                        Text("Privata")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = visibilita == 2,
                            onClick = { visibilita = 2 }
                        )
                        Text("Solo Amici")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = visibilita == 1,
                            onClick = { visibilita = 1 }
                        )
                        Text("Pubblica")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth().align(Alignment.End)) {
                    TextButton(onClick = onDismiss) {
                        Text("Annulla")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = {
                        // Avvia una coroutine per la chiamata API
                            wishlistViewModel.viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                            val response = RetrofitInstance.createApi(WishlistApiService::class.java, TokenManager.getToken()).createWishlist(wishlist = Wishlist(nomeWishlist, visibilita))
                            response.body()?.let {
                                Log.d("WishlistsActivity", "Wishlist creata: $it")
                            }
                        }

                            onCreate(nomeWishlist, visibilita)
                            wishlistViewModel.loadWishlists()
                            onDismiss() // Chiudi il modal dopo la creazione
                        }
                    }) {
                        Text("Crea")
                    }
                }
            }
        }
    }
}



