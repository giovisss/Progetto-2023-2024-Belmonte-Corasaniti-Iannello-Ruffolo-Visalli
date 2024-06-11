package com.example.jokiandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jokiandroid.model.CartItem

//classe per gestire lo stato del carrello aggiungendo e rimuovendo giochi
class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    fun addItem(item: CartItem) {
        val currentList = _cartItems.value ?: emptyList()
        _cartItems.value = currentList + item
    }

    fun removeItem(item: CartItem) {
        val currentList = _cartItems.value ?: emptyList()
        _cartItems.value = currentList - item
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}