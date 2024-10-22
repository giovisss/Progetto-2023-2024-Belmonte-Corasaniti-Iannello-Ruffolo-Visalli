package com.example.jokiandroid.service

import android.opengl.Visibility
import com.example.jokiandroid.model.Wishlist
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface WishlistApiService {
    companion object {
        private const val BASE_URL: String = "/api/v1/wishlists"
    }

    @GET(BASE_URL)
    suspend fun getWishlists(): Response<List<Wishlist>>

    @Headers("Content-Type: application/json")
    @POST(BASE_URL)
    suspend fun createWishlist(@Body wishlist: Wishlist): Response<String>

    @Headers("Content-Type: application/json")
    @DELETE("$BASE_URL/{wishlistName}")
    suspend fun deleteWishlist(@Path("wishlistName") wishlistName: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("$BASE_URL/{wishlistName}/{gameId}")
    suspend fun addGameToWishlist(@Path("wishlistName") wishlistName: String, @Path("gameId") gameId: String): Response<String>

    @Headers("Content-Type: application/json")
    @GET("$BASE_URL/{wishlistName}")
    suspend fun getWishlist(@Path("wishlistName") wishlistName: String): Response<Wishlist>

}

