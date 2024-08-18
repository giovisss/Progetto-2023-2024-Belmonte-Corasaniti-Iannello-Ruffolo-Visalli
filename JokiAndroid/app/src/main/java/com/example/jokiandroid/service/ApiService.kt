package com.example.jokiandroid.service

import com.example.jokiandroid.model.Game
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/v1/games")
    suspend fun getGames(): Response<List<Game>>

    @GET("api/v1/games/{id}") // Assumiamo che questo sia l'endpoint per recuperare i dettagli di un gioco specifico
    suspend fun getGameById(@Path("id") id: String): Response<Game>

    @GET("api/v1/users/user/library")
    suspend fun getGamesByUser(): Response<List<Game>>

}
