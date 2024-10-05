import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductsService} from "../../services/products.service";
import {CartService} from "../../services/cart.service";
import {WishlistService} from "../../services/wishlist.service";
import {BASE_IMAGE_URL} from "../../global";
import {ReviewService} from "../../services/review.service";
import {Review} from "../../model/review";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})

export class ProductComponent {
  reviewExists: boolean = false;
  userReview: Review | null = null;

  product: any;
  reviews: Review[] = [];
  showWishlistModal: boolean = false;
  showSuggestedModal: boolean = false;
  showNotSuggestedModal: boolean = false;
  newWishlistName: string = '';
  newWishlistVisibility: number = 1; // 0: privata, 1: pubblica, 2: solo amici  pubblica di default
  newSuggestedReviewText: string = '';
  newNotSuggestedReviewText: string = '';
  wishlists = this.wishlistService.getWishlists();

  constructor(private route : ActivatedRoute, private productsService: ProductsService, private cartService: CartService, private wishlistService: WishlistService, private reviewService: ReviewService, private userService: UserService) {
    const id = this.route.snapshot.params['id'];
    console.log("ID", id);

  productsService.getGame(id).subscribe((product: any) => {
      this.product = product.model;
      console.log(this.product);
    });

  reviewService.getReviewsByGameId(id).subscribe((reviews: any) => {
      this.reviews = reviews;
      // console.log(this.reviews);
    });
  
    reviewService.getUserReview(id).subscribe((response) => {
        if (response) {
          this.reviewExists = true;
          this.userReview = response;
        } else {
          console.log('Recensione non trovata.');
        }
      },
      (error) => {
        console.error('Errore durante l\'invio della recensione:', error);
      }
    );
  }

  addToCart() {
    this.cartService.addToCart(this.product);

  }

  addSuggestedReview() {
    const suggested = true;
    const newReview = new Review(this.product.id, this.newSuggestedReviewText, suggested, this.userService.getUser()?.username);
    this.reviewService.insertReview(newReview).subscribe(
      (response) => {
        this.closeSuggestedModal();
      },
      (error) => {
        console.error('Errore durante l\'invio della recensione:', error);
      }
    );
  }

  addNotSuggestedReview() {
    const suggested = false;
    const newReview = new Review(this.product.id, this.newNotSuggestedReviewText, suggested, this.userService.getUser()?.username);
    this.reviewService.insertReview(newReview).subscribe(
      (response) => {
        this.closeNotSuggestedModal();
      },
      (error) => {
        console.error('Errore durante l\'invio della recensione:', error);
      }
    );
  }

  OpenWishlistModal() {
    this.showWishlistModal = true;
  }

  OpenSuggestedModal() {
    this.showSuggestedModal = true;
  }

  OpenNotSuggestedModal() {
    this.showNotSuggestedModal = true;
  }

  closeSuggestedModal() {
    this.showSuggestedModal = false;
  }

  closeNotSuggestedModal() {
    this.showNotSuggestedModal = false;
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

    protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
}
