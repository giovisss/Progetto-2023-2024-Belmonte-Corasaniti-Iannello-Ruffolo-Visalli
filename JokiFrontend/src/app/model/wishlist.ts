import {Product} from "./product";

export class Wishlist {
  wishListProducts: Product[] = [];

  constructor(wishListProducts: Product[]) {
    this.wishListProducts = wishListProducts;
  }
}
