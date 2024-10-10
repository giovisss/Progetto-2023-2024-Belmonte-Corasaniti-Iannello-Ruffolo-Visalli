package com.example.jokiandroid.service

import com.example.jokiandroid.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("api/v1/users")
    suspend fun getUsers(): Response<List<User>>
}