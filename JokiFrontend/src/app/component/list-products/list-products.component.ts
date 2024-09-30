import {Component, OnInit} from '@angular/core';
import {ProductsService} from "../../services/products.service";
import {BASE_IMAGE_URL} from "../../global";
import { game } from '../../model/game';

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

  loadGames() {
    this.productsService.getGamesList().subscribe(
      (games: game[]) => {
        this.games = games;
        console.log('Loaded games:', this.games);
      },
      error => {
        console.error('Error loading games:', error);
      }
    );
  }
}