package com.example.jokiandroid.service

import com.example.jokiandroid.model.Game
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("games")
    suspend fun getGames(): Response<List<Game>>
}
