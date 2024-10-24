package com.example.jokiandroid.repository

import android.util.Log
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.model.Wishlist
import com.example.jokiandroid.service.WishlistApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishlistRepository {
    private val gson = Gson()

    suspend fun getWishlists(): List<Wishlist> {
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.createApi(WishlistApiService::class.java, TokenManager.getToken()).getWishlists()
            Log.d("WishlistRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val responseBody: List<Wishlist>? = response.body()
                if (responseBody != null) {
                    val jsonString = gson.toJson(responseBody)
                    Log.d("WishlistRepository", "Response body: $jsonString")
                    responseBody
                } else {
                    Log.e("WishlistRepository", "Response body is null")
                    emptyList()
                }
            } else {
                Log.e("WishlistRepository", "Error response: ${response.errorBody()?.string()}")
                emptyList()
            }
        }
    }

    suspend fun addGameToWishlist(wishlistName: String ,gameId: String): Boolean {

        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.createApi(WishlistApiService::class.java, TokenManager.getToken()).addGameToWishlist(wishlistName,gameId)
            Log.d("WishlistRepository", "Response code: ${response.code()}")
            response.isSuccessful
        }
    }

    suspend fun getSingleWishlist(wishlistName: String): Wishlist? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.createApi(WishlistApiService::class.java, TokenManager.getToken()).getWishlist(wishlistName)
            Log.d("WishlistRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val responseBody: Wishlist? = response.body()
                if (responseBody != null) {
                    val jsonString = gson.toJson(responseBody)
                    Log.d("WishlistRepository", "Response body: $jsonString")
                    responseBody
                } else {
                    Log.e("WishlistRepository", "Response body is null")
                    null
                }
            } else {
                Log.e("WishlistRepository", "Error response: ${response.errorBody()?.string()}")
                null
            }
        }
    }

    suspend fun removeGameFromWishlist(wishlistName: String, gameId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.createApi(WishlistApiService::class.java, TokenManager.getToken()).removeGameFromWishlist(wishlistName, gameId)
            Log.d("WishlistRepository", "Response code: ${response.code()}")
            response.isSuccessful
        }
    }
}