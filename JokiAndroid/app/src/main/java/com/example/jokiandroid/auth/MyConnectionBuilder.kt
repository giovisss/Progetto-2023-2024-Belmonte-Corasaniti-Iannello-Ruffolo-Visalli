package com.example.jokiandroid.auth

import android.net.Uri
import net.openid.appauth.connectivity.ConnectionBuilder
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class MyConnectionBuilder private constructor() : ConnectionBuilder {
    @Throws(IOException::class)
    override fun openConnection(uri: Uri): HttpURLConnection {
        val conn = URL(uri.toString()).openConnection() as HttpURLConnection
        conn.connectTimeout = CONNECTION_TIMEOUT_MS
        conn.readTimeout = READ_TIMEOUT_MS
        conn.instanceFollowRedirects = false
        return conn
    }

    companion object {
        val INSTANCE: MyConnectionBuilder = MyConnectionBuilder()

        private val CONNECTION_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(15).toInt()
        private val READ_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(10).toInt()

    }
}