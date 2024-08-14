package com.example.jokiandroid.model

import com.example.jokiandroid.service.ApiService
import com.example.jokiandroid.utility.IPManager
import retrofit2.Call
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val BACKEND_IP = IPManager.BACKEND_IP
    private val BASE_URL = "http://${BACKEND_IP}/" // Sostituisci con l'indirizzo IP del tuo server

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}
