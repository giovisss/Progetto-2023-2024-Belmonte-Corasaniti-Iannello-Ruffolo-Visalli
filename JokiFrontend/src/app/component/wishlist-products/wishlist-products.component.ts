import { Component } from '@angular/core';
import {Wishlist} from "../../model/wishlist";
import {WishlistService} from "../../services/wishlist.service";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";

@Component({
  selector: 'app-wishlist-products',
  templateUrl: './wishlist-products.component.html',
  styleUrl: './wishlist-products.component.css'
})
export class WishlistProductsComponent {
  wishlist: Wishlist | null = null; // Memorizza i dati della wishlist
  constructor(private wishlistService: WishlistService, private route: ActivatedRoute) {}

  ngOnInit() {
    const name = this.route.snapshot.params['name'];
    this.wishlistService.getWishlist(name).subscribe(wishlist => {
      this.wishlist = wishlist;
    });
  }

}
