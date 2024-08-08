import { Injectable } from '@angular/core';
import {Wishlist} from "../model/wishlist";
import {of} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class WishlistService {

  wishlists: Wishlist[] = [];

  constructor() {}

  getWishlists() {
    return JSON.parse(localStorage.getItem('wishlists') || '[]');
  }

  getWishlist(name: string) {
    const wishlists = this.getWishlists();
    return of(wishlists.find((w: { name: string; }) => w.name === name));
  }

  addWishlist(wishlist: Wishlist) {
    const wishlists = this.getWishlists();
    wishlists.push(wishlist);
    localStorage.setItem('wishlists', JSON.stringify(wishlists));
  }

  removeWishlist(wishlist: Wishlist) {
    let wishlists = this.getWishlists();
    wishlists = wishlists.filter((w: { name: string; }) => w.name !== wishlist.name); //Filtra l'array per escludere la wishlist che ha lo stesso nome della wishlist passata come parametro.
    localStorage.setItem('wishlists', JSON.stringify(wishlists));
  }
}
