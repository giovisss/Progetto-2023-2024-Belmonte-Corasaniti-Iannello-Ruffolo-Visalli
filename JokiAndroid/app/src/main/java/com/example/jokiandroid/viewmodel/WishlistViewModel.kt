package com.example.jokiandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.model.Wishlist
import com.example.jokiandroid.repository.WishlistRepository
import kotlinx.coroutines.launch

class WishlistViewModel : ViewModel() {
    private val _wishlists: MutableLiveData<List<Wishlist>> = MutableLiveData()
    val wishlists: LiveData<List<Wishlist>> get() = _wishlists

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
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Error loading wishlists", e)
            }
        }

    }

    //crea una nuova wishlist
//    fun createWishlist(name: String, visibility: Int) {
//        viewModelScope.launch {
//            try {
//                val response = wishlistRepository.createWishlist(name, visibility)
//                if (response) {
//                    Log.d("WishlistViewModel", "Wishlist creata: Response = $response")
//                    loadWishlists()
//                } else {
//                    Log.e("WishlistViewModel", "Errore nella creazione della wishlist: Response = $response")
//                }
//            } catch (e: Exception) {
//                Log.e("WishlistViewModel", "Errore nella creazione della wishlist", e)
//            }
//        }
//    }
//    fun getListOfWishlist(): LiveData<MutableList<Wishlist>>? {
//        return wishlists
//    }

//    fun createWishlist(name: String?, visibility: Int) {
//        require(!(name == null || name.isEmpty())) { "Il nome della wishlist non può essere vuoto" }
//        var currentWishlists: MutableList<Wishlist>? = wishlists!!.getValue()
//        if (currentWishlists == null) {
//            currentWishlists = ArrayList()
//        }
//        val newWishlist = Wishlist(name, visibility)
//        currentWishlists.add(newWishlist)
//        wishlists.setValue(currentWishlists)
//    }
//
//
//    fun removeWishlist(name: String) {
//        val currentWishlist: MutableList<Wishlist> = checkNotNull(
//            wishlists!!.value
//        )
//        currentWishlist.removeIf { wishlist: Wishlist -> wishlist.name == name }
//        wishlists.setValue(currentWishlist)
//    }
}