package com.example.jokiandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jokiandroid.model.Cart
import com.example.jokiandroid.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/*classe per gestire lo stato del carrello aggiungendo e rimuovendo giochi
class CartViewModel {
    private val _cartList = MutableStateFlow<List<Game>>(emptyList())
    val cartListCurrentState: StateFlow<List<Game>> get() = _cartList  //variabile che può essere modificata solo in questo viewModel

    //e rappresenta lo stato corrente della lista di giochi del carrello
    //appena questo viene modificato la lista viene aggioranata
    fun removeFromCart(game: Game) {
        val updatedList = _cartList.value.toMutableList()
        updatedList.remove(game)
        _cartList.value = updatedList
    }

    fun addToCart(game: Game) {
        val updatedList = _cartList.value.toMutableList()
        updatedList.add(game)
        _cartList.value = updatedList
    }
}*/
//classe per gestire lo stato del carrello aggiungendo e rimuovendo giochi
class CartViewModel : ViewModel() {
    private val _cart = MutableStateFlow(Cart())
    val cart: StateFlow<Cart> get() = _cart

    fun removeFromCart(game: Game) {
        _cart.update { currentCart ->
            currentCart.apply { remove(game) }
        }
    }

    fun addToCart(game: Game) {
        _cart.update { currentCart ->
            currentCart.apply { add(game) }
        }
    }
}