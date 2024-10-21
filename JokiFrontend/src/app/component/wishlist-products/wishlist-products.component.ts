import {Component, OnInit} from '@angular/core';
import {Wishlist} from "../../model/wishlist";
import {WishlistService} from "../../services/wishlist.service";
import {ActivatedRoute, Router} from "@angular/router";
import {BASE_IMAGE_URL} from "../../global";
import {CartService} from "../../services/cart.service";

@Component({
  selector: 'app-wishlist-products',
  templateUrl: './wishlist-products.component.html',
  styleUrl: './wishlist-products.component.css'
})
export class WishlistProductsComponent implements OnInit {
  protected other: boolean = false;
  private username: string = "";
  protected wishlistName: string = "";

  wishlist: Wishlist | null = null; // Memorizza i dati della wishlist

  showProductDetails: { [productId: string]: boolean } = {}; // Oggetto per tenere traccia della visibilità dei dettagli

  constructor(private wishlistService: WishlistService, private route: ActivatedRoute, protected router: Router, protected cartService: CartService) {}

  ngOnInit() {
    this.route.url.subscribe(url => {
        this.other = false;
        this.username = "";
        this.wishlistName = "";

        if (url[1].path == "other"){
          this.other = true;
          this.username = url[2].path;
          this.wishlistName = url[3].path;
        }
        else {
          this.other = false;
            this.wishlistName = url[1].path;
        }
    })

    // const wishlistName = this.route.snapshot.params['wishlistName'];

    if (this.other) {
        this.wishlistService.getOtherWishlist(this.username, this.wishlistName).subscribe(response => {
            console.log(response);
            this.wishlist = response;
        });
    } else {
      this.wishlistService.getWishlist(this.wishlistName).subscribe(response => {
        console.log(response);
        this.wishlist = response;
      });
    }
  }

  toggleProductDetails(product: any) {
    this.showProductDetails[product.id] = !this.showProductDetails[product.id];
  }

  removeProduct(product: any) {
    if (this.wishlist != null) {
      this.wishlistService.removeProductFromWishlist(this.wishlist.wishlistName, product.id)
        .subscribe(response => {
          if (response) {
            // @ts-ignore
            this.wishlistService.getWishlist(this.wishlist.wishlistName).subscribe(response => {
                console.log(response);
                this.wishlist = response;
            })
            console.log('Prodotto rimosso con successo!');
          } else {
            console.error('Wishlist non trovata.');
          }
        });
    }
  }

    protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
}
