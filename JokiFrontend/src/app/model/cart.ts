
import {Game} from "./game";

class CartItem {
  game: Game;
  quantity: number;

  constructor(product: Game, quantity: number) {
    this.game = product;
    this.quantity = quantity;
  }
}

export class Cart {
  cartItems: CartItem[] = [];

  constructor(cartItems: CartItem[]) {
    this.cartItems = cartItems;
  }
}
