package com.example.jokiandroid.auth

object TokenManager {
    private var token: String? = null

    fun setToken(newToken: String) {
        token = newToken
    }

    fun clearToken() {
        token = null
    }

    fun getToken(): String? = token
}