package com.example.jokiandroid.service

import com.example.jokiandroid.model.Game
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/v1/games")
    suspend fun getGames(): Response<CollectionModel>

    @GET("api/v1/games/{id}")
    suspend fun getGameById(@Path("id") id: String): Response<EntityModel>

    @GET("api/v1/users/user/library")
    suspend fun getGamesByUser(): Response<List<Game>>

    data class CollectionModel(val _embedded: Embedded)
    data class Embedded(val gameModelList: List<GameModel>)
    data class GameModel(val gameDto: Game)
    data class EntityModel(val gameDto: Game)
}