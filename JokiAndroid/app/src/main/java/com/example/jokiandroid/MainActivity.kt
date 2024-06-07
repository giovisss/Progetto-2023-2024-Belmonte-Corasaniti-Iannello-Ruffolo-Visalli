package com.example.jokiandroid

import CartActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jokiandroid.ui.theme.JokiAndroidTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JokiAndroidTheme {
                Surface {
                        MainScreen()
                    }
                }
            }
        }
    }


@Composable
fun MainScreen() { //navController per gestire la navigazione tra le varie pagine
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { JokiHome(navController) }
        composable("cart") { CartActivity(navController) }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokiHome( navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(DrawerValue.Closed) //crea e ricorda lo stato del drawer
    val coroutineScope = rememberCoroutineScope() //coroutine ( eseguire operazioni asincrone), questo crea un coroutineScope che è un contesto di esecuzione delle coroutine

    ModalNavigationDrawer( //menu a tendina che si apre premendo l'icona del menu
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("MENU'", modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary,)
                Divider()
                NavigationDrawerItem(
                    modifier = Modifier.padding(bottom = 16.dp),
                    label = { Text(text = "Login") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Home") },
                    selected = true,
                    onClick = { /*TODO*/ }
                )
                NavigationDrawerItem(
                    label = { Text(text = "La tua libreria") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Impostazioni") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                NavigationDrawerItem(
                    label = { Text(text = "About") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
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
                        IconButton(onClick = { navController.navigate("cart")}) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Carrello"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding -> innerPadding
            Surface(modifier = Modifier.padding(innerPadding)) {
                GameListPage()
            }
        }
    }
}