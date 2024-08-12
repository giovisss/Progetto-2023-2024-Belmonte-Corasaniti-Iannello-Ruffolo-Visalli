import { Component } from '@angular/core';
import {ProductsService} from "../../services/products.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-list-products',
  templateUrl: './list-products.component.html',
  styleUrl: './list-products.component.css'
})
export class ListProductsComponent {
  protected products: any;
  constructor(private productsService: ProductsService, private userService:UserService) {
    this.userService.test()
    this.productsService.getProductList().subscribe((products: any) => {
      this.products = products;
    });
  }
}
