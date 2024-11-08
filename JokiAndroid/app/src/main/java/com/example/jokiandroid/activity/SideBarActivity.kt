package com.example.jokiandroid.activity

import CartActivity
import GameViewModel
import TokenManager
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.jokiandroid.auth.AuthManager
import com.example.jokiandroid.utility.CustomToast
import com.example.jokiandroid.utility.ToastString
import com.example.jokiandroid.viewmodel.CartViewModel
import com.example.jokiandroid.viewmodel.UserViewModel
import com.example.jokiandroid.viewmodel.WishlistViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SideBarActivity {
    private val _composable = MutableLiveData<@Composable () -> Unit>()
    val composable: LiveData<@Composable () -> Unit> get() = _composable

    fun setContent(comp:@Composable () -> Unit) {
        _composable.value = comp
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicUI(navController: NavController, authManager: AuthManager, userViewModel: UserViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(DrawerValue.Closed) //crea e ricorda lo stato del drawer
    val coroutineScope = rememberCoroutineScope() //coroutine ( eseguire operazioni asincrone), questo crea un coroutineScope che è un contesto di esecuzione delle coroutine
    val localContext = LocalContext.current
    val composable by SideBarActivity.composable.observeAsState()
    val selectedItem = remember { mutableStateOf("home") } // Stato per l'elemento selezionato
    val isAdmin by userViewModel.isAdmin.observeAsState()
    val cartViewModel = CartViewModel()

    ToastString.setMessage("")
    CustomToast(localContext)

    ModalNavigationDrawer( //menu a tendina che si apre premendo l'icona del menu
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("MENU", modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary,)
                HorizontalDivider()
                if (TokenManager.isLoggedIn()) {
                    NavigationDrawerItem(
                        modifier = Modifier.padding(bottom = 16.dp),
                        label = { Text(text = "Logout") },
                        selected = selectedItem.value == "logout",
                        onClick = {
                            authManager.logout(localContext as Activity)
                            selectPage("home", coroutineScope, drawerState, navController, selectedItem)
                            TokenManager.clearToken()
                        }
                    )
                }else{
                    NavigationDrawerItem(
                    modifier = Modifier.padding(bottom = 16.dp),
                    label = { Text(text = "Login") },
                    selected = selectedItem.value == "login",
                    onClick = {
                        // Avvia il flusso di autorizzazione (crasha per network error)
                        authManager.startAuthorization(localContext as Activity)
                        selectPage("login", coroutineScope, drawerState, navController, selectedItem)
                        }
                    )
                }
                NavigationDrawerItem(
                    label = { Text(text = "Home") },
                    selected = selectedItem.value == "home",
                    onClick = { selectPage("home", coroutineScope, drawerState, navController, selectedItem) }
                )

                if(isAdmin == true) {
                    NavigationDrawerItem(
                        label = { Text(text = "Gestione Giochi") },
                        selected = selectedItem.value == "edit_games",
                        onClick = { selectPage("edit_games", coroutineScope, drawerState, navController, selectedItem) }
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Gestione Utenti") },
                        selected = selectedItem.value == "edit_users",
                        onClick = { selectPage("edit_users", coroutineScope, drawerState, navController, selectedItem) }
                    )
                }
                else {
                    NavigationDrawerItem(
                        label = { Text(text = "La tua Libreria") },
                        selected = selectedItem.value == "libreria",
                        onClick = { selectPage("libreria", coroutineScope, drawerState, navController, selectedItem) }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Le tue wishlists") },
                        selected = selectedItem.value == "wishlist",
                        onClick = { selectPage("wishlist", coroutineScope, drawerState, navController, selectedItem) }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Gestione Profilo") },
                        selected = selectedItem.value == "userinfo",
                        onClick = { selectPage("edit_current_user", coroutineScope, drawerState, navController, selectedItem) }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "About") },
                        selected = selectedItem.value == "about",
                        onClick = {
                            Log.d("About", "About clicked")
                            selectPage("about", coroutineScope, drawerState, navController, selectedItem)
                            Log.d("About", "About clicked after selectPage")
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            "Joki",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        /*Questa linea di codice avvia una nuova coroutine nel CoroutineScope che abbiamo creato e ricordato. All'interno di questa coroutine,
                            chiamiamo drawerState.open(). Questa è una funzione sospesa che apre il drawer. Le funzioni sospese possono essere chiamate solo all'interno
                            di una coroutine o di un'altra funzione sospesa, quindi abbiamo avvolto drawerState.open() in una coroutine.*/
                        IconButton(onClick = { coroutineScope.launch{ drawerState.open() } }) { // Apri il drawer quando si clicca sul pulsante del menu
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navigateToCart(navController, cartViewModel) }) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Cart"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding -> //innerPadding
            Column(modifier = Modifier.padding(innerPadding)) {
                composable?.invoke()
            }
        }
    }
}

fun selectPage(page:String, coroutineScope:CoroutineScope, drawerState: DrawerState, navController: NavController, selectedItem: MutableState<String>){
    selectedItem.value = page
    coroutineScope.launch {
        drawerState.close()
        navController.navigate(page)
    }
}

@Composable
fun SetGameListContent(gameViewModel: GameViewModel, cartViewModel: CartViewModel, navController: NavController) {
    SideBarActivity.setContent {
        GameListPage(gameViewModel = gameViewModel, cartViewModel = cartViewModel, navController = navController, wishlistViewModel = WishlistViewModel())
    }
}

@Composable
fun SetLoginContent(navController: NavController) {
    SideBarActivity.setContent {
        LoginActivity(navController = navController)
    }

}

@Composable
fun SetLibraryContent(navController: NavController, gameViewModel: GameViewModel) {
    SideBarActivity.setContent {
        LibraryActivity(navController, gameViewModel)
    }
}

@Composable
fun SetWishlistContent(navController: NavController, wishlistViewModel: WishlistViewModel) {
    SideBarActivity.setContent {
        WishlistsActivity(navController, wishlistViewModel)
    }
}

@Composable
fun SetSingleWishlistContent(navController: NavHostController, wishlistName: String, wishlistViewModel: WishlistViewModel, cartViewModel: CartViewModel) {
    SideBarActivity.setContent {
        SingleWishlistActivity(navController, wishlistName, wishlistViewModel, cartViewModel)
    }
}

@Composable
fun SetCartContent(navController: NavController, cartViewModel: CartViewModel) {
    SideBarActivity.setContent {
        CartActivity(navController, cartViewModel, true)
    }
}

@Composable
fun SetEditGameContent(navController: NavController, gameViewModel: GameViewModel, gameId: String = "") {
    SideBarActivity.setContent {
        EditGamesActivity(navController, gameViewModel, gameId)
    }
}

@Composable
fun SetAboutContent(navController: NavController) {
    SideBarActivity.setContent {
        AboutActivity(navController)
    }
}

@Composable
fun SetEditUserContent(navController: NavController, userViewModel: UserViewModel, gameId: String = "") {
    SideBarActivity.setContent {
        EditUserDataActivity(navController, userViewModel, gameId)
    }
}

fun navigateToCart(navController: NavController, cartViewModel: CartViewModel) {
    navController.navigate("cart")
    cartViewModel.loadCart() // Carica i dati del carrello

}
