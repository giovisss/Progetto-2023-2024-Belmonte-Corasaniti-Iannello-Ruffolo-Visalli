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
  newWishlistName = '';
  newWishlistVisibility: number = 1;
  showModal = false;

  constructor(private wishlistService: WishlistService, private router: Router) {}

  ngOnInit() {
    this.wishlists = this.wishlistService.getWishlists();
  }

  loadWishlists() {
    this.wishlists = this.wishlistService.getWishlists();
  }

    openModal() {
        this.showModal = true;
    }

    closeModal() {
        this.showModal = false;
    }

    createWishlist() {
        const newWishlist: Wishlist = {
            name: this.newWishlistName,
            wishListProducts: [],
            visibility: this.newWishlistVisibility
        };
        this.wishlistService.addWishlist(newWishlist)
        this.loadWishlists();
        console.log('Wishlist creata con successo!');
        this.closeModal();
    }

  removeWishlist(wishlist: Wishlist) {
    this.wishlistService.removeWishlist(wishlist);
    this.loadWishlists();
  }

  OpenWishlistDetails(wishlist: Wishlist) {
    this.router.navigate(['/wishlists', wishlist.name]);
  }

}
