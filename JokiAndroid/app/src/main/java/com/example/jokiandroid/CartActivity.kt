import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.viewmodel.CartViewModel

@Composable
fun CartActivity(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    val cart by cartViewModel.cart.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Carrello",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(cart.games) { game ->
                CartItem(
                    game = game,
                    onRemove = {
                        cartViewModel.removeFromCart(game)
                    }
                )
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Prezzo totale: €${"%.2f".format(cart.totalPrice)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { /* Logica di pagamento */ }) {
                Text(text = "Paga")
            }
        }
    }
}

@Composable
fun CartItem(game: Game, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = game.title, fontSize = 18.sp)
        Button(onClick = onRemove) {
            Text(text = "Rimuovi")
        }
    }
}
