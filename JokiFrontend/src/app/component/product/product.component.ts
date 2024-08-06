import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductsService} from "../../services/products.service";
import {CartService} from "../../services/cart.service";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent {
  product: any;
  constructor(private route : ActivatedRoute, private productsService: ProductsService, private cartService: CartService) {
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


}
