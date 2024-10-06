import {Component} from '@angular/core';
import {ProductsService} from "../../services/products.service";
import {BASE_IMAGE_URL} from "../../global";
import {Game} from "../../model/game";

@Component({
  selector: 'app-list-products',
  templateUrl: './list-products.component.html',
  styleUrl: './list-products.component.css'
})
export class ListProductsComponent {
  protected products: Game[] = [];
  constructor(private productsService: ProductsService) {
    this.productsService.getGamesList().subscribe((response: Game[]) => {
      this.products = response;
    });
  }

  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
}
