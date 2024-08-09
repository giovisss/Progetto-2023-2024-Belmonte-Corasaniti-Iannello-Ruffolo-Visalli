import { Component } from '@angular/core';
import {Wishlist} from "../../model/wishlist";
import {WishlistService} from "../../services/wishlist.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-wishlist-products',
  templateUrl: './wishlist-products.component.html',
  styleUrl: './wishlist-products.component.css'
})
export class WishlistProductsComponent {
  wishlist: Wishlist | null = null; // Memorizza i dati della wishlist

  showProductDetails: { [productId: number]: boolean } = {}; // Oggetto per tenere traccia della visibilità dei dettagli

  constructor(private wishlistService: WishlistService, private route: ActivatedRoute) {}

  ngOnInit() {
    const name = this.route.snapshot.params['name'];
    this.wishlistService.getWishlist(name).subscribe(wishlist => {
      this.wishlist = wishlist;
    });
  }

  toggleProductDetails(product: any) {
    this.showProductDetails[product.id] = !this.showProductDetails[product.id];
  }

  removeProduct(product: any) {
    if (this.wishlist) {
      this.wishlistService.removeProductFromWishlist(this.wishlist.name, product.id)
        .subscribe(updatedWishlist => {
          if (updatedWishlist) {
            this.wishlist = updatedWishlist;
            console.log('Prodotto rimosso con successo!');
          } else {
            console.error('Wishlist non trovata.');
          }
        });
    }
  }
}
