import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jokiandroid.R
import com.example.jokiandroid.model.CartItem
import com.example.jokiandroid.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartActivity(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val totalPrice by cartViewModel.totalPrice.observeAsState(0.0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Carrello") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
}




@Composable
fun CartItem(cartItem: CartItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Colonna per immagine, titolo e prezzo
        Column {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.games_image),
                    contentDescription = "game image",
                    modifier = Modifier.size(80.dp)
                )
            }

            Text(text = cartItem.title, fontSize = 18.sp)
            Text(text = "Prezzo: €${String.format("%.2f", cartItem.price)}")
        }

        // Spazio tra colonna e icona
        Spacer(modifier = Modifier.width(16.dp))

        // Icona del cestino (usa un Box per centrarla)
        Box(modifier = Modifier.align(Alignment.CenterVertically)) { // Centra verticalmente l'icona
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Rimuovi dal carrello",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

