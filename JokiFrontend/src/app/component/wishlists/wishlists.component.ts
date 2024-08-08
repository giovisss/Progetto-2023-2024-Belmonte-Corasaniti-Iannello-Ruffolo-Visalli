import { Component } from '@angular/core';
import {Wishlist} from "../../model/wishlist";
import {WishlistService} from "../../services/wishlist.service";

@Component({
  selector: 'app-wishlists',
  templateUrl: './wishlists.component.html',
  styleUrl: './wishlists.component.css'
})
export class WishlistsComponent {
  wishlists: Wishlist[] = [];

  constructor(private wishlistService: WishlistService) {}

  ngOnInit() {
    this.wishlists = this.wishlistService.getWishlists();
  }

  loadWishlists() {
    this.wishlists = this.wishlistService.getWishlists();
  }

  removeWishlist(wishlist: Wishlist) {
    this.wishlistService.removeWishlist(wishlist);
    this.loadWishlists();
  }
}
