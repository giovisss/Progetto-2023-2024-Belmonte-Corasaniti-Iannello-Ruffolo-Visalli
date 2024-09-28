import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, map, Observable} from 'rxjs';
import { game } from '../model/game';
import { BASE_API_URL } from '../global';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = BASE_API_URL + '/users';
  private cartSubject: BehaviorSubject<game[]> = new BehaviorSubject<game[]>([]);
  private totalSubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  private quantitySubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  constructor(private httpClient: HttpClient) { }

  getCart(): Observable<game[]> {
    this.httpClient.get<game[]>(this.apiUrl + '/user/cart').subscribe(cart => {
      this.cartSubject.next(cart);
      this.updateTotal(cart);
      this.updateQuantity(cart);
    });
    return this.cartSubject.asObservable();
  }

  addToCart(game: game): Observable<any> {
    console.log(this.apiUrl + '/user/cart/' + game.id);
    this.httpClient.post(this.apiUrl + '/user/cart/' + game.id, game).subscribe(() => {

    });
    return this.httpClient.post(this.apiUrl + '/user/cart/' + game.id, game).pipe(
      map(() => {
        console.log(1);
        const currentCart = this.cartSubject.value;
        console.log(2);
        currentCart.push(game);
        console.log(3);
        this.cartSubject.next(currentCart);
        console.log(4);
        this.updateTotal(currentCart);
        console.log(5);
        this.updateQuantity(currentCart);
        console.log(6);
      })
    );
  }

  removeFromCart(game: game): Observable<any> {
    return this.httpClient.delete(this.apiUrl + '/user/cart/' + game.id).pipe(
      map(() => {
        const currentCart = this.cartSubject.value.filter(item => item.id !== game.id);
        this.cartSubject.next(currentCart);
        this.updateTotal(currentCart);
        this.updateQuantity(currentCart);
      })
    );
  }

  clearCart(): Observable<any> {
    return this.httpClient.delete(this.apiUrl + '/user/cart').pipe(
      map(() => {
        this.cartSubject.next([]);
        this.totalSubject.next(0);
        this.quantitySubject.next(0);
      })
    );
  }

  getTotal(): Observable<number> {
    return this.totalSubject.asObservable();
  }

  getQuantity(): Observable<number> {
    return this.quantitySubject.asObservable();
  }

  private updateTotal(cart: game[]): void {
    const total = cart.reduce((sum, item) => sum + item.price, 0);
    this.totalSubject.next(total);
  }

  private updateQuantity(cart: game[]): void {
    const quantity = cart.length;
    this.quantitySubject.next(quantity);
  }
}
