import { Component } from '@angular/core';
import {Wishlist} from "../../model/wishlist";
import {WishlistService} from "../../services/wishlist.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-wishlists',
  templateUrl: './wishlists.component.html',
  styleUrl: './wishlists.component.css'
})
export class WishlistsComponent {
  wishlists: Wishlist[] = [];

  constructor(private wishlistService: WishlistService, private router: Router) {}

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

  OpenWishlistDetails(wishlist: Wishlist) {
    this.router.navigate(['/wishlists', wishlist.name]);
  }

}
