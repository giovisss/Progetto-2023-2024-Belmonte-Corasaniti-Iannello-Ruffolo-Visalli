package com.example.jokiandroid.activity

import CartActivity
import GameDetailsActivity
import GameViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jokiandroid.auth.AuthManager
import com.example.jokiandroid.ui.theme.JokiAndroidTheme
import com.example.jokiandroid.utility.IPManager
import com.example.jokiandroid.viewmodel.CartViewModel
import com.example.jokiandroid.viewmodel.WishlistViewModel

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        hideSystemUI()
        super.onCreate(savedInstanceState)
        IPManager.setIps(this)
        authManager = AuthManager(this)
        setContent {
            JokiAndroidTheme {
                Surface {
                    MainScreen(authManager)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Auth", "onActivityResult")
        Log.d("Auth", "Request Code: $requestCode")
        Log.d("Auth", "Result Code: $resultCode")
        Log.d("Auth", "Data: $data")

        if (requestCode == AuthManager.RC_AUTH) {
            if (data != null) {
                authManager.handleAuthorizationResponse(
                    requestCode,
                    resultCode,
                    data
                ) { accessToken, idToken ->
                    if (accessToken != null && idToken != null) {
                        // Usa i token come necessario
                        Log.d("Auth", "Access Token: $accessToken")
                        Log.d("Auth", "ID Token: $idToken")
                    } else {
                        // Gestisci l'errore di autenticazione
                        Log.e("Auth", "Authentication failed")
                    }
                }
            } else {
                Log.e("Auth", "Intent data is null")
            }
        } else {
            Log.d("Auth", "Unknown request code: $requestCode")
        }
    }
}

@Composable
fun MainScreen(authManager: AuthManager) { //navController per gestire la navigazione tra le varie pagine
    val navController = rememberNavController()
    val cartViewModel = remember { CartViewModel() }
    val gameViewModel = remember { GameViewModel() }
    val wishlistViewModel = remember { WishlistViewModel() }

    BasicUI(navController = navController, authManager = authManager)

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { SetGameListContent(gameViewModel, cartViewModel, navController) }
        composable("libreria") { SetLibraryContent(navController, gameViewModel) }
        composable("wishlist") { SetWishlistContent(navController, wishlistViewModel) }
        composable("cart") { CartActivity(navController, cartViewModel) }
        composable("login") { SetLoginContent(navController) }
        composable("game_detail/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")
            gameId?.let { GameDetailsActivity(gameId = it, viewModel = gameViewModel, navController) }
        }
    }

}