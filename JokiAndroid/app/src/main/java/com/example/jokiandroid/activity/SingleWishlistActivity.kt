package com.example.jokiandroid.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.jokiandroid.viewmodel.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleWishlistActivity(navController: NavController, wishlistName: String, wishlistViewModel: WishlistViewModel) {
    val singleWishlist = wishlistViewModel.singleWishlist.observeAsState()

    LaunchedEffect(Unit) {
        wishlistViewModel.getSingleWishlist(wishlistName)
    }

    singleWishlist.value?.games?.forEach() {
        Text(text = it.title)
    }

}