package com.example.jokiandroid.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.jokiandroid.config.AuthConfig
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class AuthManager(private val context: Context) {

    private val authService: AuthorizationService = AuthorizationService(context)

    fun startAuthorization(activity: Activity) {
        AuthorizationServiceConfiguration.fetchFromUrl(
            Uri.parse(AuthConfig.DISCOVERY_URI)
        ) { serviceConfig, ex ->
            if (serviceConfig != null) {
                val authRequestBuilder = AuthorizationRequest.Builder(
                    serviceConfig,
                    AuthConfig.CLIENT_ID,
                    ResponseTypeValues.CODE,
                    Uri.parse(AuthConfig.REDIRECT_URI)
                )
                val authRequest = authRequestBuilder.build()

                val customTabsIntent = authService.createCustomTabsIntentBuilder().build()
                val authIntent = authService.getAuthorizationRequestIntent(authRequest, customTabsIntent)   //authIntent è un Intent esplicito creato utilizzando la libreria AppAuth. È utilizzato per avviare il flusso di autorizzazione dell'utente,
                                                                                                            // aprendo un'interfaccia di autenticazione (come un browser o una WebView) che permette all'utente di inserire le proprie credenziali e autorizzare l'applicazione.
                activity.startActivityForResult(authIntent, RC_AUTH)
            } else {
                Log.e("Auth", "Failed to fetch configuration", ex)
            }
        }
    }

    fun handleAuthorizationResponse(requestCode: Int, resultCode: Int, data: Intent?, onTokenReceived: (String?, String?) -> Unit) {
        if (requestCode == RC_AUTH) {
            val response = data?.let { AuthorizationResponse.fromIntent(it) }
            val ex = AuthorizationException.fromIntent(data)

            if (response != null) {
                val tokenRequest = response.createTokenExchangeRequest()
                authService.performTokenRequest(tokenRequest) { tokenResponse, exception ->
                    if (tokenResponse != null) {
                        onTokenReceived(tokenResponse.accessToken, tokenResponse.idToken)
                    } else {
                        Log.e("Auth", "Token Exchange failed", exception)
                        onTokenReceived(null, null)
                    }
                }
            } else {
                Log.e("Auth", "Authorization failed", ex)
                onTokenReceived(null, null)
            }
        }
    }

    companion object {
        const val RC_AUTH = 100  // Codice di richiesta univoco per la richiesta di autenticazione
    }
}