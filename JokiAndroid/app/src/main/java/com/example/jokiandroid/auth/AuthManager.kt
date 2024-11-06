package com.example.jokiandroid.auth

import TokenManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.example.jokiandroid.config.AuthConfig
import com.example.jokiandroid.utility.IPManager
import com.example.jokiandroid.viewmodel.UserViewModel
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
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthManager(private val context: Context, private val userViewModel: UserViewModel) {

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
                    val authIntent =
                        authService.getAuthorizationRequestIntent(authRequest, customTabsIntent)
                    activity.startActivityForResult(authIntent, RC_AUTH)
                } else {
                    Log.e("AuthManager", "Failed to fetch configuration", ex)
                }
            },
            MyConnectionBuilder.INSTANCE
        )
    }

    fun handleAuthorizationResponse(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onTokenReceived: (String?, String?) -> Unit
    ) {
        if (requestCode == RC_AUTH) {
            val response = data?.let { AuthorizationResponse.fromIntent(it) }
            val ex = AuthorizationException.fromIntent(data)

            if (response != null) {
                Log.d("Auth", "Access Token: ${response.accessToken}")
                response.accessToken?.let { TokenManager.setToken(it) }
                onTokenReceived(response.accessToken, response.idToken)
                val tokenRequest = response.createTokenExchangeRequest()
                authService.performTokenRequest(tokenRequest) { tokenResponse, exception ->
                    if (tokenResponse != null) {
//                        authState.update(tokenResponse, exception) //

                        Log.d("Auth", "Access Token: ${tokenResponse.accessToken}")
                        Log.d("Auth", "ID Token: ${tokenResponse.idToken}")
                        onTokenReceived(tokenResponse.accessToken, tokenResponse.idToken)

                        userViewModel.reloadUser()
//                        callApiWithAuthorization(tokenResponse.accessToken)
                    } else {
                        Log.e("AuthManager", "Token Exchange failed", exception)
                        onTokenReceived(null, null)
                    }
                }
            } else {
                Log.e("AuthManager", "Authorization failed")
                onTokenReceived(null, null)
                throw RuntimeException("Authorization failed")
            }
        }
    }

    companion object {
        const val RC_AUTH = 100  // Codice di richiesta univoco per la richiesta di autenticazione

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

    fun logout(activity: Activity) {
        val accessToken = TokenManager.getToken()

        if (accessToken != null) {
            val realmBaseUrl = "http://${IPManager.KEYCLOAK_IP}/realms/JokiRealm"
            val logoutUrl = "$realmBaseUrl/protocol/openid-connect/logout"

            val client = OkHttpClient()

            val formBody = FormBody.Builder()
                .add("client_id", AuthConfig.CLIENT_ID)
                .build()

            val request = Request.Builder()
                .url(logoutUrl)
                .addHeader("Authorization", "Bearer $accessToken")
                .post(formBody)
                .build()

            Log.d("AuthManager", "Attempting logout at URL: $logoutUrl")

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("AuthManager", "Failed to revoke access token", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.d("AuthManager", "Access token revoked successfully")
                        TokenManager.clearToken()
                        val logoutUri = Uri.parse("http://${IPManager.KEYCLOAK_IP}/realms/JokiRealm/protocol/openid-connect/logout")
                        val logoutIntent = Intent(Intent.ACTION_VIEW, logoutUri)
                        activity.startActivity(logoutIntent)
                        TokenManager.clearToken()
                        userViewModel.reloadUser()

                    } else {
                        Log.e("AuthManager", "Failed to revoke access token: ${response.code}")
                        response.body?.string()?.let {
                            Log.e("AuthManager", "Error response: $it")
                        }
                    }
                }
            })
        } else {
            Log.d("AuthManager", "No access token to revoke")
        }
    }


    private fun revokeAccessToken() {
        val accessToken = TokenManager.getToken()

        if (accessToken != null) {
            val realmBaseUrl = "http://${IPManager.KEYCLOAK_IP}/realms/JokiRealm"
            val logoutUrl = "$realmBaseUrl/protocol/openid-connect/logout"

            val client = OkHttpClient()

            val formBody = FormBody.Builder()
                .add("client_id", AuthConfig.CLIENT_ID)
                .build()

            val request = Request.Builder()
                .url(logoutUrl)
                .addHeader("Authorization", "Bearer $accessToken")
                .post(formBody)
                .build()

            Log.d("AuthManager", "Attempting logout at URL: $logoutUrl")

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("AuthManager", "Failed to revoke access token", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.d("AuthManager", "Access token revoked successfully")
                        // Pulisci il token solo dopo una risposta di successo
                        TokenManager.clearToken()
                    } else {
                        Log.e("AuthManager", "Failed to revoke access token: ${response.code}")
                        // Log the response body for debugging
                        response.body?.string()?.let {
                            Log.e("AuthManager", "Error response: $it")
                        }
                    }
                }
            })
        } else {
            Log.d("AuthManager", "No access token to revoke")
        }
    }
}
