import { Injectable } from '@angular/core';
import {Wishlist} from "../model/wishlist";

@Injectable({
  providedIn: 'root'
})

export class WishlistService {

  wishlists: Wishlist[] = [];

  constructor() {}
}
