import {Component} from '@angular/core';
import {ProductsService} from "../../services/products.service";
import {BASE_IMAGE_URL} from "../../global";

@Component({
  selector: 'app-list-products',
  templateUrl: './list-products.component.html',
  styleUrl: './list-products.component.css'
})
export class ListProductsComponent {
  protected products: any[] = [];
  constructor(private productsService: ProductsService) {
    this.productsService.getGamesList().subscribe((response: any) => {
      this.products = response._embedded.modelList.map((model: any) => model.model);
    });
  }

  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
}
