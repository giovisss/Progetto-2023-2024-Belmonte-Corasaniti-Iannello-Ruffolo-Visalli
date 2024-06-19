import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.jokiandroid.viewmodel.GameViewModel

@Composable
fun GameDetailScreen(gameId: Int, viewModel: GameViewModel) {
    val game = viewModel.games.observeAsState(emptyList()).value.find { it.id == gameId }

    game?.let {
        Column {
            Text(text = "Title: ${it.title}")
            Text(text = "Description: ${it.description}")
            Text(text = "Price: ${it.price}")
            Text(text = "image: ${it.image}") }
    } ?: run {
        Text(text = "Game not found")
    }
}