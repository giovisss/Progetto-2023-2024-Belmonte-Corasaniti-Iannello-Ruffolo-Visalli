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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.viewmodel.CartViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import com.example.jokiandroid.activity.LoginRequiredDialog

@SuppressLint("DefaultLocale")
@Composable
fun CartActivity(navController: NavController, cartViewModel: CartViewModel, shouldReload: Boolean = false) {
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val isLoading by cartViewModel.isLoading.observeAsState()
    val checkoutStatus by cartViewModel.checkoutStatus.observeAsState()

    LaunchedEffect(shouldReload) {
        if (shouldReload) {
            cartViewModel.loadCart()
        }
    }

    LaunchedEffect(checkoutStatus) {
        when (checkoutStatus) {
            true -> {
                // Checkout riuscito
                // Puoi mostrare un messaggio di successo o navigare alla libreria
                Log.d("CartActivity", "Checkout riuscito")

            }
            false -> {
                // Checkout fallito
                // Mostra un messaggio di errore
                Log.e("CartActivity", "Checkout fallito")
            }
            null -> {
                // Nessun checkout ancora tentato
            }
        }
    }

    Log.d("CartActivity created", "cartItems size: ${cartItems.size}")

    Column(modifier = Modifier.fillMaxSize()) {
        if (TokenManager.getToken() == null) {
            LoginRequiredDialog(
                onDismissRequest = { /* Non fare nulla, l'utente deve effettuare il login */ },
                onConfirmClick = { navController.popBackStack() }
            )
        } else {
            if (isLoading == true) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(cartItems) { index: Int, game: Game ->
                        GameItem(game = game, viewModel = cartViewModel)
                    }
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

            Row {
                Button(
                    onClick = {
                        cartViewModel.checkout()
                        cartViewModel.loadCart()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Paga ora")
                }
            }

            Row {
                Button(
                    onClick = {
                        cartViewModel.removeAllGamesFromCart()
                        cartViewModel.loadCart()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Svuota carrello")
                }
            }
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
            Text("Rimuovi")
        }
    }
}

