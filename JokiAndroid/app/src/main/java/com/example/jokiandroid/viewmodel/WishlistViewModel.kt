package com.example.jokiandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.model.Wishlist
import com.example.jokiandroid.repository.WishlistRepository
import com.example.jokiandroid.utility.ToastString
import kotlinx.coroutines.launch

class WishlistViewModel : ViewModel() {
    private val _wishlists: MutableLiveData<List<Wishlist>> = MutableLiveData()
    val wishlists: LiveData<List<Wishlist>> get() = _wishlists

    private val _singleWishlist: MutableLiveData<Wishlist> = MutableLiveData()
    val singleWishlist: LiveData<Wishlist> get() = _singleWishlist

    private val wishlistRepository = WishlistRepository()

    fun loadWishlists() {
        viewModelScope.launch {
            try{
                val tmp = wishlistRepository.getWishlists()
                val out = mutableListOf<Wishlist>()
                for (wishlist in tmp) {
                    out.add(Wishlist(wishlist))
                }
                _wishlists.value = out
                Log.d("WishlistViewModel", "Updated static wishlists: ${wishlists.value?.size}")
                ToastString.setMessage("Wishlists caricate")
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Error loading wishlists", e)
                ToastString.setMessage("Errore nel caricamento delle wishlists")
            }
        }

    }

    fun addGameToWishlist(wishlistName: String, gameId: String) {
        viewModelScope.launch {
            try {
                val response = wishlistRepository.addGameToWishlist(wishlistName, gameId)
                if (response) {
                    Log.d("WishlistViewModel", "Game aggiunto alla wishlist: Response = $response")
                    ToastString.setMessage("Gioco aggiunto alla wishlist")
                } else {
                    Log.e("WishlistViewModel", "Errore nell'aggiunta del gioco alla wishlist: Response = $response")
                    ToastString.setMessage("Errore nell'aggiunta del gioco alla wishlist")
                }
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Errore nell'aggiunta del gioco alla wishlist", e)
                ToastString.setMessage("Errore nell'aggiunta del gioco alla wishlist")
            }
        }

    }


    fun getSingleWishlist(wishlistName: String) {
        viewModelScope.launch {
            try {
                val response = wishlistRepository.getSingleWishlist(wishlistName)
                if (response != null) {
                    _singleWishlist.value = Wishlist(response)
                    Log.d("WishlistViewModel", "Caricata la wishlist: ${singleWishlist.value?.wishlistName}")
                    ToastString.setMessage("Wishlist caricata")
                } else {
                    Log.e("WishlistViewModel", "Errore nel caricamento della wishlist: Response = $response")
                    ToastString.setMessage("Errore nel caricamento della wishlist")
                }
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Errore nel caricamento della wishlist", e)
                ToastString.setMessage("Errore nel caricamento della wishlist")
            }
        }
    }

    fun removeGameFromWishlist(wishlistName: String, gameId: String) {
        viewModelScope.launch {
            try {
                val response = wishlistRepository.removeGameFromWishlist(wishlistName, gameId)
                if (response) {
                    Log.d("WishlistViewModel", "Game rimosso dalla wishlist: Response = $response")
                    ToastString.setMessage("Gioco rimosso dalla wishlist")
                } else {
                    Log.e("WishlistViewModel", "Errore nella rimozione del gioco dalla wishlist: Response = $response")
                    ToastString.setMessage("Errore nella rimozione del gioco dalla wishlist")
                }
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Errore nella rimozione del gioco dalla wishlist", e)
                ToastString.setMessage("Errore nella rimozione del gioco dalla wishlist")
            }
        }
    }


}
