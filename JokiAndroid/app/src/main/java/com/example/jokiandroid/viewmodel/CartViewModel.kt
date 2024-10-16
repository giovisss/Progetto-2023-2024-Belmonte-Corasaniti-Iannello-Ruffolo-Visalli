package com.example.jokiandroid.viewmodel

import TokenManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.repository.CartRepository
import kotlinx.coroutines.launch

//classe per gestire lo stato del carrello aggiungendo e rimuovendo giochi
class CartViewModel() : ViewModel() {

    private val cartRepository = CartRepository()

    private val _cartItems = MutableLiveData<List<Game>>()
    private val _totalPrice = MutableLiveData<Double>(0.0)
    private val _isLoading = MutableLiveData(false)
    private val _error = MutableLiveData<String?>()

    // LiveData per osservare i cambiamenti del carrello
    val cartItems: LiveData<List<Game>> get() = _cartItems
    val totalPrice: LiveData<Double> = _totalPrice
    val isLoading: LiveData<Boolean> get() = _isLoading
    val error: LiveData<String?> get() = _error

    fun loadCart(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cartobj = cartRepository.getUserCart()
                val out = mutableListOf<Game>()
                for (game in cartobj) {
                    Log.d("CartViewModel", "Received cart game: $game")
                    out.add(Game(game))
                }

                Log.d("CartViewModel", "Received cart items: $out")
                _cartItems.value = out
                Log.d("CartViewModel", "Updated static cartItems: ${cartItems.value?.size}")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error loading cart", e)
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun addGame(game: Game) {
        val currentList = _cartItems.value ?: emptyList()
        val item = currentList.find { it.id == game.id }
        if (item == null) {
            _cartItems.value = currentList + game
        }
        Log.d("CartViewModel", "Gioco Aggiunto: ${game.title}")
        Log.d("CartViewModel", "Numero di Giochi: ${currentList.size}")
        _totalPrice.value = getTotalPrice()
    }

    fun removeGame(game: Game) {
        val currentList = _cartItems.value ?: emptyList()
        _cartItems.value = currentList - game
        _totalPrice.value = getTotalPrice()
    }

//    fun addGame(game: Game) {
//        val currentList = _cartItems.value ?: emptyList()
//        val item = currentList.find { it.id == game.id }
//        if (item == null) {
//            _cartItems.value = currentList + CartItem(game.id, game.title, game.imagePath ,game.price)
//        }
//        Log.d("CartViewModel", "Added game: ${game.title}")
//        Log.d("CartViewModel", "number of games: ${currentList.size}")
//        _totalPrice.value = getTotalPrice()
//    }
//
//    fun removeGame(item: CartItem) {
//        val currentList = _cartItems.value ?: emptyList()
//        _cartItems.value = currentList - item
//        _totalPrice.value = getTotalPrice()
//    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
    }

    fun getTotalPrice(): Double {
        val currentList = _cartItems.value ?: emptyList()
        return currentList.sumOf { it.price }
    }
}