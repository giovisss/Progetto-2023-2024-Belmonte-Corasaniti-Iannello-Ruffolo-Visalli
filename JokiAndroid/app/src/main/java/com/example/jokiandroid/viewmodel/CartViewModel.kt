package com.example.jokiandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jokiandroid.model.CartItem
import com.example.jokiandroid.model.Game

//classe per gestire lo stato del carrello aggiungendo e rimuovendo giochi
class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    private val _totalPrice = MutableLiveData<Double>(0.0)

    // LiveData per osservare i cambiamenti del carrello
    val cartItems: LiveData<List<CartItem>> get() = _cartItems
    val totalPrice: LiveData<Double> = _totalPrice

    fun addGame(game: Game) {
        val currentList = _cartItems.value ?: emptyList()
        val item = currentList.find { it.id == game.id }
        if (item != null) {
            val updatedItem = item.copy(quantity = item.quantity + 1)
            _cartItems.value = currentList.map { if (it.id == game.id) updatedItem else it }
        } else {
            _cartItems.value = currentList + CartItem(game.id, game.title, 1, game.price)
        }
        Log.d("CartViewModel", "Added game: ${game.title}")
        Log.d("CartViewModel", "number of games: ${currentList.size}")
        _totalPrice.value = getTotalPrice()
    }

    fun removeGame(item: CartItem) {
        val currentList = _cartItems.value ?: emptyList()
        _cartItems.value = currentList - item
        _totalPrice.value = getTotalPrice()
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
    }

    fun getTotalPrice(): Double {
        val currentList = _cartItems.value ?: emptyList()
        return currentList.sumOf { it.price * it.quantity }
    }
}