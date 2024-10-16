package com.example.jokiandroid.service

import com.example.jokiandroid.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    companion object {
        private const val BASE_URL: String = "api/v1/users"
        private const val ADMIN_URL: String = "api/v1/admin/users"
    }

    @GET("api/v1/admin/isAdmin")
    suspend fun isAdmin(): Response<User>

    @GET(ADMIN_URL)
    suspend fun getUsers(): Response<List<User>>

    @GET("$ADMIN_URL/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): Response<User>

    @PUT("$ADMIN_URL/{username}")
    suspend fun updateUser(@Path("username") username: String, @Body user: User): Response<User>

    @GET("$BASE_URL/user")
    suspend fun getCurrentUser(): Response<User>
}