package com.example.jokiandroid.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jokiandroid.config.AuthConfig
import com.example.jokiandroid.utility.IPManager
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthManager(private val context: Context) {

    private val authService: AuthorizationService
    private lateinit var authState: AuthState

    init {
        val appAuthConfiguration = AppAuthConfiguration.Builder()
            .setConnectionBuilder(MyConnectionBuilder.INSTANCE)
            .setSkipIssuerHttpsCheck(true)
            .build()

        Log.d("AuthManager", "AppAuthConfiguration created: $appAuthConfiguration")
        authService = AuthorizationService(context, appAuthConfiguration)
    }

    fun startAuthorization(activity: Activity) {

        authState = AuthState()

        AuthorizationServiceConfiguration.fetchFromUrl(
            Uri.parse(AuthConfig.DISCOVERY_URI),
            { serviceConfig, ex ->
                if (serviceConfig != null) {
                    val authRequestBuilder = AuthorizationRequest.Builder(
                        serviceConfig,
                        AuthConfig.CLIENT_ID,
                        ResponseTypeValues.CODE,
                        Uri.parse(AuthConfig.REDIRECT_URI)
                    ).setScopes("openid")
                    val authRequest = authRequestBuilder.build()

                    Log.d("AuthManager", "AuthorizationRequest created: $authRequest")

                    val customTabsIntent = authService.createCustomTabsIntentBuilder().build()
                    val authIntent = authService.getAuthorizationRequestIntent(authRequest, customTabsIntent)
                    activity.startActivityForResult(authIntent, RC_AUTH)
                } else {
                    Log.e("AuthManager", "Failed to fetch configuration", ex)
                }
            },
            MyConnectionBuilder.INSTANCE
        )
    }

    fun handleAuthorizationResponse(requestCode: Int, resultCode: Int, data: Intent?, onTokenReceived: (String?, String?) -> Unit) {
        if (requestCode == RC_AUTH) {
            val response = data?.let { AuthorizationResponse.fromIntent(it) }
            val ex = AuthorizationException.fromIntent(data)

            if (response != null) {
                val tokenRequest = response.createTokenExchangeRequest()
                authService.performTokenRequest(tokenRequest) { tokenResponse, exception ->
                    if (tokenResponse != null) {
//                        authState.update(tokenResponse, exception) // TODO: capire come funonzia

                        Log.d("Auth", "Access Token: ${tokenResponse.accessToken}")
                        Log.d("Auth", "ID Token: ${tokenResponse.idToken}")
                        onTokenReceived(tokenResponse.accessToken, tokenResponse.idToken)
                        callApiWithAuthorization(tokenResponse.accessToken)
                    } else {
                        Log.e("AuthManager", "Token Exchange failed", exception)
                        onTokenReceived(null, null)
                    }
                }
            } else {
                Log.e("AuthManager", "Authorization failed", ex)
                onTokenReceived(null, null)
            }
        }
    }

    companion object {
        const val RC_AUTH = 100  // Codice di richiesta univoco per la richiesta di autenticazione

//        private val _token = MutableLiveData<String>()
//        val token: LiveData<String> get() = _token
//
//        fun setToken(t: String) {
//            _token.value = t
//        }
//
//        private val _isLogged = MutableLiveData<Boolean>()
//        val isLogged: LiveData<Boolean> get() = _isLogged
//
//        fun setContent(b:Boolean) {
//            _isLogged.value = b
//        }
    }

    fun callApiWithAuthorization(accessToken: String?) {
        val url = "http://${IPManager.BACKEND_IP}/api/carts/bbb"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API", "Request failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.e("API", "Unexpected code $response")
                    } else {
                        Log.d("API", "Response: ${it.body?.string()}")
                    }
                }
            }
        })
    }
}
