package com.example.jokiandroid.service

import com.example.jokiandroid.model.Game
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("api/v1/games")
    suspend fun getGames(): Response<CollectionModel>

    @GET("api/v1/games/{id}")
    suspend fun getGameById(@Path("id") id: String): Response<EntityModel>

    @PUT("api/v1/admin/games/{id}")
    suspend fun updateGame(@Path("id") id: String, @Body game: Game): Response<String>

    @GET("api/v1/users/user/library")
    suspend fun getGamesByUser(): Response<List<Game>>

    @GET("api/v1/users/user/cart")
    suspend fun getUserCart(): Response<List<Game>>


    data class CollectionModel(val _embedded: Embedded)
    data class Embedded(val modelList: List<GameModel>)
    data class GameModel(val model: Game)
    data class EntityModel(val model: Game)
}