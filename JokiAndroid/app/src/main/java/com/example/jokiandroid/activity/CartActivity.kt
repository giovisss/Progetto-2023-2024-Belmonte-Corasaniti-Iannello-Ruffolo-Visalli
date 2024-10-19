import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp

@SuppressLint("DefaultLocale")
@Composable
fun CartActivity(navController: NavController, viewModel: CartViewModel, shouldReload: Boolean = false) {
    val cartItems by viewModel.cartItems.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState()

    LaunchedEffect(shouldReload) {
        if (shouldReload) {
            viewModel.loadCart()
        }
    }

    Log.d("CartActivity created blablabla", "cartItems size: ${cartItems.size}")

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading == true) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(cartItems) { index: Int, game: Game ->
                    GameItem(game = game, viewModel = viewModel)}
                }
            }
            Text(
                text = "Totale: €${String.format("%.2f", cartItems.sumOf { it.price })}",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    viewModel.removeAllGamesFromCart()
                    viewModel.loadCart()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Clear Cart")
            }
        }
    }


@Composable
fun GameItem(game: Game, viewModel: CartViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = game.title, fontSize = 18.sp)
            Text(text = "Prezzo: €${String.format("%.2f", game.price)}")
        }
        Button(onClick = { viewModel.removeGame(game) }
        ) {
            Text("Remove")
        }
    }
}

