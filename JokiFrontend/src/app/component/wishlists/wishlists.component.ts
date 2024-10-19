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
      this.loadWishlists()
  }

  loadWishlists() {
    this.wishlistService.getWishlists().subscribe( response => {
        if (response != null) {
            this.wishlists = response;
            console.log(this.wishlists);
        } else {
            alert('Errore nel caricamento delle wishlist!');
        }
    })
  }

    openModal() {
        this.showModal = true;
    }

    closeModal() {
        this.showModal = false;
    }

    createWishlist() {
        this.wishlistService.createWishlist(new Wishlist(this.newWishlistName, [], this.newWishlistVisibility)).subscribe( response => {
            if (!response) alert('Errore nella creazione della wishlist!');
            else {
                this.loadWishlists();
                console.log('Wishlist creata con successo!');
                this.closeModal();
            }
        })
    }

  removeWishlist(wishlistName: string) {
    this.wishlistService.deleteWishlist(wishlistName).subscribe( response => {
        if (!response) alert('Errore nella rimozione della wishlist!');
        else {
            this.loadWishlists();
            console.log('Wishlist rimossa con successo!');
        }
    })
  }

  OpenWishlistDetails(wishlist: Wishlist) {
    this.router.navigate(['/wishlists', wishlist.wishlistName]);
  }

}
