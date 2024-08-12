import {Product} from "./product";
import {game} from "./game";

export class Wishlist {
  name: string;
  wishListProducts: game[] = [];
  visibility: number = 0;

  constructor(name: string, wishListProducts: game[], visibility: number) {
    this.name = name;
    this.wishListProducts = wishListProducts;
    this.visibility = visibility;
  }
}
