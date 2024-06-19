import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jokiandroid.R
import com.example.jokiandroid.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsActivity(gameId: Int, viewModel: GameViewModel, navController: NavController) {
    val game = viewModel.games.observeAsState(emptyList()).value.find { it.id == gameId }

    game?.let { //se il gioco non è null allora mostro i dettagli altrimenti si esegue il codice in run

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
                        contentDescription = "Immagine di ${game.title}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                item {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Descrizione: ${game.description}")
                        Text("Valutazione:")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Genere: ")
                    }
                }
            }
        }
    }
}
