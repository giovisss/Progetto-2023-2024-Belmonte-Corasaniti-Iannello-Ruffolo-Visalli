package com.example.jokiandroid.repository

import TokenManager
import android.util.Log
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.service.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val token: String) {
    private val api: ApiService = RetrofitInstance.createApi(ApiService::class.java,token)
    private val gson = Gson()

    suspend fun getUserCart(): List<Game> {
        withContext(Dispatchers.IO) {
            val response = api.getUserCart()
            Log.d("CartRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val jsonString = response.body() ?: "[]"
                val type = object : TypeToken<List<Game>>() {}.type
                gson.fromJson(jsonString, type)
            } else {
                throw Exception("Impossibile ottenere il carrello utente: ${response.code()}")
            }
        }
    }

//    suspend fun clearUserCart(): Boolean {
//        return withContext(Dispatchers.IO) {
//            val response = api.clearUserCart()
//            response.isSuccessful
//        }
//    }
//
//    suspend fun addGameToCart(gameId: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            val response = api.addGameToCart(gameId)
//            response.isSuccessful
//        }
//    }
//
//    suspend fun removeGameFromCart(gameId: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            val response = api.removeGameFromCart(gameId)
//            response.isSuccessful
//        }
//    }
}