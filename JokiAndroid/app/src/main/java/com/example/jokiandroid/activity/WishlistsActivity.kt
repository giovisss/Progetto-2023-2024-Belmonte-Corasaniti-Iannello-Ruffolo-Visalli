package com.example.jokiandroid.activity

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.jokiandroid.viewmodel.WishlistViewModel

//Qui dobbiamo visualizzare le wishlist dell'utente
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistsActivity(navController: NavController, wishlistViewModel: WishlistViewModel) {



    Scaffold(
        topBar = {
            //Qui dobbiamo visualizzare il titolo della pagina
            CenterAlignedTopAppBar(title = {
                Text(text = "Wishlists")
            })
        },
        content = {
            /*
            * ALLORA PRATICAMENTW QUI DOBBIAMO VISUALIZZARE LA LISTA DELLE WISHLIST DELL'UTENTE E DOBBIAMO CREARE UN COMPOSABLE DELLA SINGOLA WISHLIST
            * CHE VERRà POI VISUALIZZATA COME UNA LISTA DI WISHLIST
            *
            * */

            //Qui dobbiamo visualizzare la lista delle wishlist

        }
    )

}
