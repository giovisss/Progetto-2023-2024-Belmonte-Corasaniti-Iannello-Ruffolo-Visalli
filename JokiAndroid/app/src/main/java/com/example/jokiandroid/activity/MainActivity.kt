package com.example.jokiandroid.activity

import GameDetailsActivity
import GameViewModel
import TokenManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
        super.onCreate(savedInstanceState)
        IPManager.setIps(this)

        if (!isInternetAvailable(this)) {
            showNoInternetDialog()
        }

        // Inizializziamo sempre il GameViewModel
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        authManager = AuthManager(this, userViewModel)

        // Inizializziamo il CartRepository
        cartRepository = CartRepository()

        initializeContent()
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("Nessuna connessione a Internet")
            .setMessage("Attiva la connessione a Internet e riprova.")
            .setPositiveButton("OK") { dialog, _ ->
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Runtime.getRuntime().exit(0)
            }
            .show()
    }

    private fun initializeContent(refresh: Boolean = false) {
        setContent {
            JokiAndroidTheme {
                Surface {
                    MainScreen(refresh, authManager, gameViewModel, cartRepository, userViewModel)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try{
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == AuthManager.RC_AUTH) {
                authManager.handleAuthorizationResponse(
                    requestCode,
                    resultCode,
                    data
                ) { accessToken, idToken ->
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
        catch (e: Exception){
            initializeContent(true)
        }
    }
}

@Composable
fun MainScreen(refresh: Boolean, authManager: AuthManager, gameViewModel: GameViewModel, cartRepository: CartRepository, userViewModel: UserViewModel) {
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
            wishlistName?.let { SetSingleWishlistContent(navController, it, wishlistViewModel, cartViewModel) }
        }
        composable("cart") { SetCartContent(navController, cartViewModel) }
        composable("login") { SetLoginContent(navController) }
        composable("edit_games") { SetEditGameContent(navController, gameViewModel) }
        composable("about") { SetAboutContent(navController) }

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

        composable("edit_current_user") { EditCurrentUserDataActivity(navController, userViewModel) }
    }

    if (refresh) {
        navController.navigate("home")
    }


}