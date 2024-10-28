import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductsService} from "../../services/products.service";
import {CartService} from "../../services/cart.service";
import {WishlistService} from "../../services/wishlist.service";
import {BASE_IMAGE_URL} from "../../global";
import {ReviewService} from "../../services/review.service";
import {Review} from "../../model/review";
import {UserService} from "../../services/user.service";
import {Game} from "../../model/game";
import {Wishlist} from "../../model/wishlist";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})

export class ProductComponent {
  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;

  reviewExists: boolean = false;
  userReview: Review | null = null;
  reviewsAvg: number = 0;

  product: Game | null = null;
  reviews: Review[] = [];
  showWishlistModal: boolean = false;
  showSuggestedModal: boolean = false;
  showNotSuggestedModal: boolean = false;
  isReviewsModalOpen: boolean = false;
  isInLibrary: boolean = false;
  newWishlistName: string = '';
  newWishlistVisibility: number = 1; // 0: privata, 1: pubblica, 2: solo amici  pubblica di default
  newSuggestedReviewText: string = '';
  newNotSuggestedReviewText: string = '';
  wishlists: Wishlist[] = [];

  photo1: boolean = true;
  photo2: boolean = true;
  photo3: boolean = true;

  constructor(private route : ActivatedRoute, private productsService: ProductsService, private cartService: CartService, private wishlistService: WishlistService, private reviewService: ReviewService, private userService: UserService) {
    const id = this.route.snapshot.params['id'];
    console.log("ID", id);

    this.wishlistService.getWishlists().subscribe((response) => {
      if (response != null) {
        this.wishlists = response;
        console.log(this.wishlists);
      } else {
        console.error('Errore nel caricamento delle wishlist!');
      }
    })

    productsService.getGame(id).subscribe((product: Game) => {
      this.product = product;
      console.log(this.product);
    });

    reviewService.getReviewsByGameId(id).subscribe((reviews: any) => {
      this.reviews = reviews;
      console.log(this.reviews);
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

    reviewService.getAvgReviews(id).subscribe((response) => {
        this.reviewsAvg = response * 100;
      },
      (error) => {
        console.error('Errore durante l\'invio della recensione:', error);
      }
    );
  }

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.productsService.getGame(id).subscribe((product: Game) => {
      this.product = product;
      this._checkIfInLibrary();
    });
  }

  addToCart() {
    this.cartService.addToCart(this.product!);

  }

  addSuggestedReview() {
    const suggested = true;
    const newReview = new Review(this.product!.id!, this.newSuggestedReviewText, suggested, this.userService.getUser()?.username);
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
    const newReview = new Review(this.product!.id!, this.newNotSuggestedReviewText, suggested, this.userService.getUser()?.username);
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
      let out = [];
      if (this.product != null) out.push(this.product);

      this.wishlistService.createWishlist(new Wishlist(this.newWishlistName, out, 0)).subscribe(response => {
        if (response) {
            console.log('Wishlist creata con successo!');
          this.newWishlistName = '';
          this.newWishlistVisibility = 1;
          this.closeWishlistModal();
        } else {
            alert('Errore nella creazione della wishlist!');
        }
      });
    }
  }

  addToWishlist(wishlistName: string, productId: string | null) {
    if (productId != null) {
      this.wishlistService.addProductToWishlist(wishlistName, productId).subscribe(response => {
        if (response) {
          console.log('Prodotto aggiunto alla wishlist con successo!');
          this.closeWishlistModal();
        } else {
          alert('Errore durante l\'aggiunta del prodotto alla wishlist!');
        }
      });
    }
  }


  openModal() {
    this.isReviewsModalOpen = true;
  }

  closeModal() {
    this.isReviewsModalOpen = false;
  }

  private _checkIfInLibrary(): void {
    this.userService.getUserLibrary().subscribe((library: Game[]) => {
      this.isInLibrary = library.some(game => game.id === this.product?.id);
    });
  }
}
