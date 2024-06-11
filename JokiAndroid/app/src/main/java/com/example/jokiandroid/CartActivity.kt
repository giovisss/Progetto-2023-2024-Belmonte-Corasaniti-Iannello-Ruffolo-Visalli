import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jokiandroid.model.CartItem
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.viewmodel.CartViewModel

@Composable
fun CartActivity(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())

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
                items(cartItems.size) { cartItems ->
                    CartItem(
                        cartItem = cartItems[cartItem],
                        onRemove = {
                            cartViewModel.removeFromCart(game)
                        }
                    )
                    Divider()
                }
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
fun CartItem(cartItem: CartItem, onRemove: () -> Unit) {
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
