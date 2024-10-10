package com.example.jokiandroid.service

import com.example.jokiandroid.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    companion object {
        private const val BASE_URL: String = "api/v1/users"
        private const val ADMIN_URL: String = "api/v1/admin/users"
    }

    @GET("api/v1/admin/isAdmin")
    suspend fun isAdmin(): Response<User>

    @GET(ADMIN_URL)
    suspend fun getUsers(): Response<List<User>>

    @GET("$BASE_URL/user")
    suspend fun getUser(): Response<User>
}