package com.example.jokiandroid.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.jokiandroid.config.AuthConfig
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

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
                        Log.d("Auth", "Access Token: ${tokenResponse.accessToken}")
                        Log.d("Auth", "ID Token: ${tokenResponse.idToken}")
                        onTokenReceived(tokenResponse.accessToken, tokenResponse.idToken)
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
    }
}
