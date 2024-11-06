
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    val game by viewModel.gameDetails.observeAsState()

    LaunchedEffect(gameId) {
        viewModel.fetchGameById(gameId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Dettagli del gioco")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (game != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        AsyncImage(
                            model = "${IPManager.BACKEND_IMAGES}/${game?.url2}",
                            contentDescription = game?.title,
                            error = painterResource(id = R.drawable.games_image),
                            placeholder = painterResource(id = R.drawable.games_image),
                            modifier = Modifier
                                .weight(1f)
                                .height(150.dp)
                                .padding(5.dp)
                        )
                        AsyncImage(
                            model = "${IPManager.BACKEND_IMAGES}/${game?.url1}",
                            contentDescription = game?.title,
                            error = painterResource(id = R.drawable.games_image),
                            placeholder = painterResource(id = R.drawable.games_image),
                            modifier = Modifier
                                .weight(1f)
                                .height(150.dp)
                                .padding(5.dp)
                        )
                        AsyncImage(
                            model = "${IPManager.BACKEND_IMAGES}/${game?.url3}",
                            contentDescription = game?.title,
                            error = painterResource(id = R.drawable.games_image),
                            placeholder = painterResource(id = R.drawable.games_image),
                            modifier = Modifier
                                .weight(1f)
                                .height(150.dp)
                                .padding(5.dp)
                        )
                    }
                }
                item {
                    Text(
                        text = game?.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Descrizione:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = game?.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        Text(
//                            text = "Valutazione:",
//                            style = MaterialTheme.typography.titleMedium,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
                        // Add rating bar or stars here if available

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Genere: ${game?.genre}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Prezzo: €${String.format("%.2f", game?.price ?: 0.0)}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Developer: ${game?.developer}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Publisher: ${game?.publisher}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Data di uscita: ${game?.formattedReleaseDate}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }
        } else {
            // Gestisci il caso in cui il gioco non è stato trovato o si è verificato un errore
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Gioco non trovato o errore durante il caricamento")
                // Puoi aggiungere un pulsante per ricaricare i dati o tornare alla schermata precedente
            }
        }
    }
}