package com.example.jokiandroid.activity

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.jokiandroid.viewmodel.WishlistViewModel

@Composable
fun SingleWishlistActivity(navController: NavController, wishlistName: String){
    Text(text = "Wishlist: $wishlistName")
}