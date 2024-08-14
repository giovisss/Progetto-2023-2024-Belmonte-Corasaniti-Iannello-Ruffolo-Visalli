import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jokiandroid.R
import com.example.jokiandroid.utility.IPManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsActivity(gameId: String, viewModel: GameViewModel, navController: NavController) {
    val gameDetailsResponse by viewModel.gameDetails.observeAsState()

    LaunchedEffect(gameId) {
        viewModel.fetchGameById(gameId)
    }

    // Estrai il gioco dalla risposta o null in caso di errore/assenza
    val game = gameDetailsResponse?.let { response ->
        if (response.isSuccessful) {
            response.body()
        } else {
            null // o gestisci l'errore in modo appropriato
        }
    }

    game?.let { // Se il gioco è stato trovato, visualizza i dettagli
        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Dettagli del gioco")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        },
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                item {
                    AsyncImage(
                        model = "http://"+ IPManager.BACKEND_IP + "/images/" + it.imagePath,
                        contentDescription = it.title,
                        error = painterResource(id = R.drawable.games_image),
                        placeholder = painterResource(id = R.drawable.games_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                item {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Descrizione: ${it.description}")
                        Text("Valutazione:")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Genere: ${it.genre}")
                        Text("Prezzo: €${String.format("%.2f", it.price)}")
                        Text("Developer: ${it.developer}")
                    }
                }
            }
        }
    }  ?: run {
        // Gestisci il caso in cui il gioco non è stato trovato o si è verificato un errore
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Gioco non trovato o errore durante il caricamento")
            // Puoi aggiungere un pulsante per ricaricare i dati o tornare alla schermata precedente
        }
    }
}
