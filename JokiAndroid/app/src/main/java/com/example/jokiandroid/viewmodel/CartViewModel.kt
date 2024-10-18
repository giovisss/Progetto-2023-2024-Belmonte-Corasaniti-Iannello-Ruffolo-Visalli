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

    // LiveData per osservare i cambiamenti del carrello
    val cartItems: LiveData<List<Game>> get() = _cartItems
    val totalPrice: LiveData<Double> = _totalPrice
    val isLoading: LiveData<Boolean> get() = _isLoading

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
            }
            _isLoading.value = false
        }
    }

    fun addGame(game: Game) {
        viewModelScope.launch {
            try {
                val response = cartRepository.addGameToCart(game)
                if (response) {
                    Log.d("CartViewModel", "Game aggiunto al carrello: Response = ${game.title}")
                    loadCart()
                } else {
                    Log.e("CartViewModel", "Errore nell'aggiunta del gioco: Response = $response")
//                    _error.value = response.toString()
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Errore nell'aggiunta del gioco al carrello", e)
            }
        }

    }

    fun removeGame(game: Game) {
        viewModelScope.launch {
            try {
                val response = cartRepository.removeGameFromCart(game.id)
                if (response) {
                    Log.d("CartViewModel", "Gioco rimosso dal carrello: Response = ${game.title}")

                } else {
                    Log.e("CartViewModel", "Errore nella rimozione del gioco: Response = $response")
                }
                loadCart()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Errore nella rimozione del gioco dal carrello")
                loadCart()
            }
        }
    }


    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
    }

    fun getTotalPrice(): Double {
        val currentList = _cartItems.value ?: emptyList()
        return currentList.sumOf { it.price }
    }
}
