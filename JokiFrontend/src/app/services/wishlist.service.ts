import { Injectable } from '@angular/core';
import {Wishlist} from "../model/wishlist";
import {map, Observable, of} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {BASE_API_URL} from "../global";

@Injectable({
  providedIn: 'root'
})
export class WishlistService {
  private apiUrl = `${BASE_API_URL}/wishlists`;

  constructor(private httpClient: HttpClient) {}

  getWishlists(): Observable<Wishlist[]> {
    return this.httpClient.get<Wishlist[]>(this.apiUrl, {
      observe: 'response'
    }).pipe(
        map(response => {
            console.log(response);
            if (!response.ok || response.body == null) return [];
            return response.body.map(wishlist => new Wishlist(wishlist.wishlistName, wishlist.games, wishlist.visibility))
        }
      )
    )
  }

  getWishlist(name: string): Observable<Wishlist> {
    return this.httpClient.get<Wishlist>(this.apiUrl+"/"+name, {
      observe: 'response'
    }).pipe(
        map(response => {
            console.log(response);
            if (response.body == null) return new Wishlist("", [], 0);
            return new Wishlist(response.body.wishlistName, response.body.games, response.body.visibility)
        })
    )
  }

  createWishlist(wishlist: Wishlist): Observable<boolean> {
    return this.httpClient.post<Wishlist>(this.apiUrl, wishlist, {
        observe: 'response'
    }).pipe(
        map(response => {
            console.log(response);
            return response.ok
        })
    )
  }

  deleteWishlist(wishlistName: string): Observable<boolean> {
    return this.httpClient.delete(this.apiUrl+"/"+wishlistName, {
      observe: 'response'
    }).pipe(
        map(response => {
            console.log(response);
            return response.ok
        })
    )
  }

  addProductToWishlist(wishlistName: string, productId: string): Observable<boolean> {
    return this.httpClient.post(this.apiUrl+"/"+wishlistName+"/"+productId, null, {
      observe: 'response'
    }).pipe(
        map(response => {
            console.log(response);
            return response.ok
        })
    )
  }

  removeProductFromWishlist(wishlistName: string, productId: string): Observable<boolean> {
    return this.httpClient.delete(this.apiUrl+"/"+wishlistName+"/"+productId, {
      observe: 'response'
    }).pipe(
        map(response => {
            console.log(response);
            return response.ok
        })
    )
  }
}
