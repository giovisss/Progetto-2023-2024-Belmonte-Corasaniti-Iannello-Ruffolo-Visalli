package com.example.jokiandroid.activity

import GameDetailsActivity
import GameViewModel
import TokenManager
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jokiandroid.auth.AuthManager
import com.example.jokiandroid.repository.CartRepository
import com.example.jokiandroid.ui.theme.JokiAndroidTheme
import com.example.jokiandroid.utility.IPManager
import com.example.jokiandroid.viewmodel.CartViewModel
import com.example.jokiandroid.viewmodel.UserViewModel
import com.example.jokiandroid.viewmodel.WishlistViewModel

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager
    private lateinit var gameViewModel: GameViewModel
    private lateinit var cartRepository: CartRepository
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
//        hideSystemUI()
        super.onCreate(savedInstanceState)
        IPManager.setIps(this)
        authManager = AuthManager(this)

        // Inizializziamo sempre il GameViewModel
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // Inizializziamo il CartRepository
        cartRepository = CartRepository()

//        if (TokenManager.getToken() == null) {
//            authManager.startAuthorization(this)
//        } else {
//            initializeContent()
//        }
        initializeContent()
    }

    private fun initializeContent() {
        setContent {
            JokiAndroidTheme {
                Surface {
                    MainScreen(authManager, gameViewModel, cartRepository, userViewModel)
                }
            }
        }
        // Forziamo un refresh dei dati
//        gameViewModel.refreshData()
    }

    override fun onResume() {
        super.onResume()
//        hideSystemUI()
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
        if (requestCode == AuthManager.RC_AUTH) {
            authManager.handleAuthorizationResponse(requestCode, resultCode, data) { accessToken, idToken ->
                if (accessToken != null && idToken != null) {
                    TokenManager.setToken(accessToken)
                    runOnUiThread {
                        initializeContent()
//                        gameViewModel.refreshData() // Forziamo un refresh dei dati dopo l'autenticazione
                    }
                } else {
                    Log.e("Auth", "Authentication failed")
                    // Gestisci l'errore di autenticazione
                }
            }
        }
    }
}

@Composable
fun MainScreen(authManager: AuthManager, gameViewModel: GameViewModel, cartRepository: CartRepository, userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val cartViewModel = remember { CartViewModel() }
    val wishlistViewModel = remember { WishlistViewModel() }

    BasicUI(navController = navController, authManager = authManager, userViewModel = userViewModel)

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { SetGameListContent(gameViewModel, cartViewModel, navController) }
        composable("libreria") { SetLibraryContent(navController, gameViewModel) }
        composable("wishlist") { SetWishlistContent(navController, wishlistViewModel) }
        composable("wishlists/{wishlistName}") { backStackEntry ->
            val wishlistName = backStackEntry.arguments?.getString("wishlistName")
            wishlistName?.let { SetSingleWishlistContent() }
        }
        composable("cart") { SetCartContent(navController, cartViewModel) }
        composable("login") { SetLoginContent(navController) }
        composable("edit_games") { SetEditGameContent(navController, gameViewModel) }
        composable("about") { SetAboutContent() }

        composable("edit_games/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")
            gameId?.let { EditGamesActivity(gameId = it, gameViewModel = gameViewModel, navController = navController) }
        }

        composable("edit_users") { SetEditUserContent(navController, userViewModel) }
        composable("edit_users/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            username?.let { EditUserDataActivity(searchedUsername = it, userViewModel = userViewModel, navController = navController) }
        }

        composable("game_detail/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")
            gameId?.let { GameDetailsActivity(gameId = it, viewModel = gameViewModel, navController) }
        }

    }
}