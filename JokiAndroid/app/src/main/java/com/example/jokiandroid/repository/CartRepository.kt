package com.example.jokiandroid.repository

import android.util.Log
import com.example.jokiandroid.model.Admin
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.service.ApiService
import com.google.gson.Gson
import com.google.gson.annotations.JsonAdapter
import com.google.gson.internal.bind.DateTypeAdapter
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class CartRepository(private val token: String) {
    private val api: ApiService = RetrofitInstance.createApi(ApiService::class.java,token)
    private val gson = Gson()

    suspend fun getUserCart(): List<Game> {
        return withContext(Dispatchers.IO) {
            val response = api.getUserCart()
            Log.d("CartRepository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val responseBody: List<Game>? = response.body()
                if (responseBody != null) {
                    val jsonString = responseBody.toString()
                    Log.d("CartRepository", "Response body: $jsonString")

                    try {
                        val type = object : TypeToken<List<GameResponse>>() {}.type
                        val gameResponses: List<GameResponse> = gson.fromJson(jsonString, type)

                        gameResponses.map { gameResponse ->
                            Game(
                                id = gameResponse.id,
                                title = gameResponse.title,
                                description = gameResponse.description,
                                price = gameResponse.price,
                                imagePath = gameResponse.imagePath,
                                genre = gameResponse.genre,
                                developer = gameResponse.developer,
                                publisher = gameResponse.publisher,
                                releaseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(gameResponse.releaseDate),
                                stock = gameResponse.stock,
                                admin = Admin(
                                    id = gameResponse.admin.id,
                                    username = gameResponse.admin.username,
                                    email = gameResponse.admin.email
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("CartRepository", "Error parsing JSON", e)
                        emptyList()
                    }
                } else {
                    Log.e("CartRepository", "Response body is null")
                    emptyList()
                }
            } else {
                Log.e("CartRepository", "Error response: ${response.errorBody()?.string()}")
                throw Exception("Impossibile ottenere il carrello utente: ${response.code()}")
            }
        }
    }
}

data class GameResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imagePath: String,
    val genre: String,
    val developer: String,
    val publisher: String,
    @JsonAdapter(DateTypeAdapter::class)
    val releaseDate: String,
    val stock: Int,
    val admin: AdminResponse
)

data class AdminResponse(
    val id: String,
    val username: String,
    val email: String
)