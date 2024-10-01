import {Game} from "./game";

export class Wishlist {
  name: string;
  wishListProducts: Game[] = [];
  visibility: number = 0;

  constructor(name: string, wishListProducts: Game[], visibility: number) {
    this.name = name;
    this.wishListProducts = wishListProducts;
    this.visibility = visibility;
  }
}
