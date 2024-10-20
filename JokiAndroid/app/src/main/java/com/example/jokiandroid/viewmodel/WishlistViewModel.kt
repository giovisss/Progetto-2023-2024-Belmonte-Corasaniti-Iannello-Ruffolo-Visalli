package com.example.jokiandroid.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jokiandroid.model.Wishlist;

import java.util.ArrayList;
import java.util.List;

public class WishlistViewModel extends ViewModel {
    private MutableLiveData<List<Wishlist>> listOfWishlist = new MutableLiveData<>();
//    private WishlistRepository wishlistRepository;

//    public WishlistViewModel() {
////        wishlistRepository = new WishlistRepository();
//    }

//    public void setWishlist() {
//        wishlist.setValue(wishlistRepository.getWishlist());
//    }

    public LiveData<List<Wishlist>> getListOfWishlist() {
        return listOfWishlist;
    }

//    public void createWishlist(String name, int visibility) {
//        Wishlist newWishlist = new Wishlist(name, visibility);
//        List<Wishlist> currentWishlist = listOfWishlist.getValue();
////        assert currentWishlist != null;
//        currentWishlist.add(newWishlist);
//        listOfWishlist.setValue(currentWishlist);
//
//    }

    public void createWishlist(String name, int visibility) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Il nome della wishlist non può essere vuoto");
        }
        List<Wishlist> currentWishlists = listOfWishlist.getValue();
        if (currentWishlists == null) {
            currentWishlists = new ArrayList<>();
        }
        Wishlist newWishlist = new Wishlist(name, visibility);
        currentWishlists.add(newWishlist);
        listOfWishlist.setValue(currentWishlists);
    }


    public void removeWishlist(String name) {
        List<Wishlist> currentWishlist = listOfWishlist.getValue();
        assert currentWishlist != null;
        currentWishlist.removeIf(wishlist -> wishlist.getName().equals(name));
        listOfWishlist.setValue(currentWishlist);
    }

}
