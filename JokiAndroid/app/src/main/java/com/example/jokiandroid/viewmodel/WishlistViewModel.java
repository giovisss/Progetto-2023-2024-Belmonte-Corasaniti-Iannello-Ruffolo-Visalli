package com.example.jokiandroid.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jokiandroid.model.Wishlist;

import java.util.List;

public class WishlistViewModel extends ViewModel {
    private final MutableLiveData<List<Wishlist>> listOfWishlist = new MutableLiveData<>();
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

    public void addWishlist(String name, int visibility) {
        Wishlist newWishlist = new Wishlist(name, visibility);
        List<Wishlist> currentWishlist = listOfWishlist.getValue();
        assert currentWishlist != null;
        currentWishlist.add(newWishlist);
        listOfWishlist.setValue(currentWishlist);

    }

    public void removeWishlist(String name) {
        List<Wishlist> currentWishlist = listOfWishlist.getValue();
        assert currentWishlist != null;
        currentWishlist.removeIf(wishlist -> wishlist.getName().equals(name));
        listOfWishlist.setValue(currentWishlist);
    }

}
