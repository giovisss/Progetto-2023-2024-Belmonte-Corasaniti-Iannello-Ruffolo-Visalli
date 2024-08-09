import { Injectable } from '@angular/core';
import {Wishlist} from "../model/wishlist";
import {of} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class WishlistService {

  constructor() {}

  getWishlists() {
    return JSON.parse(localStorage.getItem('wishlists') || '[]');
  }

  getWishlist(name: string) {
    const wishlists = this.getWishlists();
    return of(wishlists.find((w: { name: string; }) => w.name === name));
  }

  addWishlist(wishlist: { wishListProducts: any[]; name: string }) {
    const wishlists = this.getWishlists();
    wishlists.push(wishlist);
    localStorage.setItem('wishlists', JSON.stringify(wishlists));
  }

  removeWishlist(wishlist: Wishlist) {
    let wishlists = this.getWishlists();
    wishlists = wishlists.filter((w: { name: string; }) => w.name !== wishlist.name); //Filtra l'array per escludere la wishlist che ha lo stesso nome della wishlist passata come parametro.
    localStorage.setItem('wishlists', JSON.stringify(wishlists));
  }

  removeProductFromWishlist(wishlistName: string, productId: number) {
    const wishlists = this.getWishlists();
    const wishlistIndex = wishlists.findIndex((w: { name: string; }) => w.name === wishlistName);

    if (wishlistIndex !== -1) {
      const wishlist = wishlists[wishlistIndex];
      wishlist.wishListProducts = wishlist.wishListProducts.filter((p: { id: number; }) => p.id !== productId);
      localStorage.setItem('wishlists', JSON.stringify(wishlists));
      return of(wishlist); // Restituisce la wishlist aggiornata come Observable
    } else {
      return of(null); // Oppure gestisci l'errore in altro modo
    }
  }
}
