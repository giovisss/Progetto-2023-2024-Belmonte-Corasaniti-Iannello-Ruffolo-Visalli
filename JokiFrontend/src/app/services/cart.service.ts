import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, catchError, map, Observable, of, switchMap, take, tap} from 'rxjs';
import { Game } from '../model/game';
import { BASE_API_URL } from '../global';
import {data} from "autoprefixer";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = BASE_API_URL + '/users';
  private cartSubject: BehaviorSubject<Game[]> = new BehaviorSubject<Game[]>([]);
  private totalSubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  private quantitySubject: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  constructor(private httpClient: HttpClient) { }

  getCart(): Observable<Game[]> {
    this.httpClient.get<Game[]>(this.apiUrl + '/user/cart').subscribe(cart => {
        let out = [];
        for (let game of cart) {
            out.push(new Game(game.id, game.title, game.description, game.price, game.imagePath, game.genre, game.developer, game.publisher, game.releaseDate, game.stock, game.admin));
        }
      this.cartSubject.next(out);
      this.updateTotal(cart);
      this.updateQuantity(cart);
    });
    return this.cartSubject.asObservable();
  }

  addToCart(game: Game) {
    this.httpClient.post(this.apiUrl + '/user/cart/' + game.id, game).subscribe(() => {
        const currentCart = this.cartSubject.value;
        this.cartSubject.next([...currentCart, game]);
        this.updateTotal(this.cartSubject.value);
        this.updateQuantity(this.cartSubject.value);
    });
  }

  removeFromCart(game: Game): Observable<any> {
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

  private updateTotal(cart: Game[]): void {
    const total = cart.reduce((sum, item) => sum + item.price, 0);
    this.totalSubject.next(total);
  }

  private updateQuantity(cart: Game[]): void {
    const quantity = cart.length;
    this.quantitySubject.next(quantity);
  }

  checkout(): Observable<any> {
    return this.cartSubject.pipe(
        take(1),
        map(cart => {
            const gameIds = cart.map(game => game.id);
            console.log('Game IDs:', gameIds);
            return gameIds;
        }),
        switchMap(gameIds => {
            return this.httpClient.post(this.apiUrl + '/user/library', gameIds).pipe(
                tap(() => {
                    this.clearCart().subscribe();
                }),
                catchError(err => {
                    console.error('Errore durante il checkout:', err);
                    return of(null);
                })
            );
        })
    );
  }

}
