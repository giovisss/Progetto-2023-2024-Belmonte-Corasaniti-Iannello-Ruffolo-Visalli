import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jokiandroid.R

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
                modifier = Modifier.fillMaxHeight()
                    .padding(innerPadding)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.games_image),
                        contentDescription = "Immagine di ${it.title}",
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
                        Text("Genere: ")
                    }
                }
            }
        }
    } ?: run { // Se il gioco non è stato trovato, visualizza un messaggio di errore
        Text("Gioco non trovato o errore durante il caricamento")
    }
}
