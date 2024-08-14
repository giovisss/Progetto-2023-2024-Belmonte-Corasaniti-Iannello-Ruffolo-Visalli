import {Component} from '@angular/core';
import {ProductsService} from "../../services/products.service";
import {BASE_IMAGE_URL} from "../../global";

@Component({
  selector: 'app-list-products',
  templateUrl: './list-products.component.html',
  styleUrl: './list-products.component.css'
})
export class ListProductsComponent {
  protected products: any;
  constructor(private productsService: ProductsService) {
    this.productsService.getGamesList().subscribe((products: any) => {
      this.products = products;
    });
  }

  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
}
