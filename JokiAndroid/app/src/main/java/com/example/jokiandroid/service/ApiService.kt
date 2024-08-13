package com.example.jokiandroid.service

import com.example.jokiandroid.model.Game
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/games")
    suspend fun getGames(): Response<List<Game>>

    @GET("api/games/{id}") // Assumiamo che questo sia l'endpoint per recuperare i dettagli di un gioco specifico
    suspend fun getGameById(@Path("id") id: String): Response<Game>
}
