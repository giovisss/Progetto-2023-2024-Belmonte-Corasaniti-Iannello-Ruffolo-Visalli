import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jokiandroid.model.CartItem
import com.example.jokiandroid.viewmodel.CartViewModel

@Composable
fun CartActivity(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val totalPrice by cartViewModel.totalPrice.observeAsState(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Carrello",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            IconButton(onClick = { navController.popBackStack()}) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "cartBack",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            Log.d("CartActivity", "cartItems size: ${cartItems.size}")
            items(cartItems.size) { game ->
                CartItem(
                    cartItem = cartItems[game],
                    onRemove = {
                        cartViewModel.removeGame(cartItems[game])
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
                text = "Prezzo totale: €${String.format("%.2f", totalPrice)}",
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
    Log.d("CartItem", "Title: ${cartItem.title}")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = cartItem.title, fontSize = 18.sp)
        Button(onClick = onRemove) {
            Text(text = "Rimuovi")
        }
    }
}
