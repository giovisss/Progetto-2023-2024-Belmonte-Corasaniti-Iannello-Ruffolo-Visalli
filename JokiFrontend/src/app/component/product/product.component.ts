import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductsService} from "../../services/products.service";
import {CartService} from "../../services/cart.service";
import {WishlistService} from "../../services/wishlist.service";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent {
  product: any;
  showWishlistModal: boolean = false;
  newWishlistName: string = '';
  newWishlistVisibility: number = 1; // 0: privata, 1: pubblica, 2: solo amici  pubblica di default
  wishlists = this.wishlistService.getWishlists();

  constructor(private route : ActivatedRoute, private productsService: ProductsService, private cartService: CartService, private wishlistService: WishlistService) {
    const id = this.route.snapshot.params['id'];
    console.log("ID", id);
    productsService.getProduct(id).subscribe((product: any) => {
      this.product = product;
      console.log(this.product);
    });
  }

  addToCart() {
    this.cartService.addToCart(this.product);
  }

  OpenWishlistModal() {
    this.showWishlistModal = true;
  }

  closeWishlistModal() {
    this.showWishlistModal = false;
  }

  createWishlist() {
    if (this.newWishlistName.trim()) {
      this.wishlists.push({ name: this.newWishlistName, wishListProducts: [this.product], visibility: this.newWishlistVisibility});
      localStorage.setItem('wishlists', JSON.stringify(this.wishlists));
      console.log(this.wishlists);
      this.newWishlistName = '';
      this.newWishlistVisibility = 1;
      this.closeWishlistModal();
    }
  }

  addToWishlist(wishlist: any) {
    wishlist.wishListProducts.push(this.product);
    localStorage.setItem('wishlists', JSON.stringify(this.wishlists));
    console.log(this.wishlists);
    this.closeWishlistModal();
  }


}
