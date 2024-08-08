import {Product} from "./product";

export class Wishlist {
  name: string;
  wishListProducts: Product[] = [];
  visibility: number = 0;

  constructor(name: string, wishListProducts: Product[], visibility: number) {
    this.name = name;
    this.wishListProducts = wishListProducts;
    this.visibility = visibility;
  }
}
