package com.example.jokiandroid.service

import com.example.jokiandroid.model.Wishlist
import retrofit2.Response
import retrofit2.http.GET

interface WishlistApiService {
    companion object {
        private const val BASE_URL: String = "/api/v1/wishlists"
    }

    @GET(BASE_URL)
    suspend fun getWishlists(): Response<List<Wishlist>>

}