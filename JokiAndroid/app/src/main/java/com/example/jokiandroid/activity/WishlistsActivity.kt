package com.example.jokiandroid.activity

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jokiandroid.model.Wishlist
import com.example.jokiandroid.viewmodel.WishlistViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.window.Dialog

//Qui dobbiamo visualizzare le wishlist dell'utente
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistsActivity(navController: NavController, wishlistViewModel: WishlistViewModel) {

    val wishlists by wishlistViewModel.listOfWishlist.observeAsState(emptyList())
    var showModal by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            //Qui dobbiamo visualizzare il titolo della pagina
//            CenterAlignedTopAppBar(title = {
                Row {
                    Text(text = "Wishlists")
                    Button(onClick = { showModal = true }) {
                        Text(text = "Crea (Saverio!!) Wishlist")
                    }

                    if (showModal) {
                        CreaWishlistModal(
                            onDismiss = { showModal = false },
                            onCreate = { nome, visibilita ->
                                Log.d("WishlistsActivity", "Creazione wishlist: $nome, $visibilita")
                                wishlistViewModel.createWishlist(nome, visibilita)
                                // Qui gestisci la creazione della wishlist
                                // con i dati ricevuti (nome, visibilita)
                            }
                        )

                    }
                }

        },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)

            ) {
                itemsIndexed(wishlists) { index, wishlist ->
                    WishlistsItem(
                        item = wishlist,
                        onWishlistClick = {
                            navController.navigate("wishlists/${wishlist.name}")


                        }
                    )
                }
            }



            //Qui dobbiamo visualizzare la lista delle wishlist

        }
    )

}
@Composable
fun WishlistsItem(item : Wishlist, onWishlistClick: (Wishlist) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp)
            .clickable { onWishlistClick(item) }
    ){
        Column {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Games: ${item.games.size}", style = MaterialTheme.typography.bodyMedium)
        }
}


        //Qui dobbiamo visualizzare la singola wishlist


}

@Composable
fun CreaWishlistModal(
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
                    RadioButton(
                        selected = visibilita == 1,
                        onClick = { visibilita = 1 }
                    )
                    Text("Pubblica")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = visibilita == 0,
                        onClick = { visibilita = 0 }
                    )
                    Text("Privata")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = visibilita == 2,
                        onClick = { visibilita = 2 }
                    )
                    Text("Solo Amici")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Annulla")
                    }

                    TextButton(onClick = {
                        onCreate(nomeWishlist, visibilita)
                        onDismiss() // Chiudi il modal dopo la creazione
                    }) {
                        Text("Crea")
                    }
                }
            }
        }
    }
}
