import { Injectable } from '@angular/core';
import {map, Observable, of} from "rxjs";
import { HttpClient } from '@angular/common/http';
import { Game } from '../model/game';
import { BASE_API_URL } from '../global';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  private apiUrl = BASE_API_URL + '/games';

  // products: Product[] = [
  //   new Product(1, 'Product 1', 'https://tailwindui.com/img/ecommerce-images/product-page-01-related-product-01.jpg', 100, 'Description 1'),
  //   new Product(2, 'Product 2', 'https://tailwindui.com/img/ecommerce-images/product-page-01-related-product-02.jpg', 100, 'Description 2'),
  //   new Product(3, 'Product 3', 'https://tailwindui.com/img/ecommerce-images/product-page-01-related-product-03.jpg', 100, 'Description 3'),
  //   new Product(4, 'Product 4', 'https://tailwindui.com/img/ecommerce-images/product-page-01-related-product-04.jpg', 100, 'Description 4'),
  // ];

  constructor(private httpClient: HttpClient) { }

  // getProductList(): Observable<Product[]> {
  //   return of(this.products);
  // }

  // getProduct(id: number): Observable<Product|undefined> {
  //   return of(this.products.find(product => product.id == id));
  // }

  getGamesList(): Observable<Game[]> {
    return this.httpClient.get<string>(this.apiUrl)
      .pipe(
        map(response => response as unknown as Game[])
      );
  }

  getGame(id: string): Observable<Game|undefined> {
    return this.httpClient.get<string>(`${this.apiUrl}/${id}`)
      .pipe(
        map(response => response as unknown as Game)
      );
  }

}
