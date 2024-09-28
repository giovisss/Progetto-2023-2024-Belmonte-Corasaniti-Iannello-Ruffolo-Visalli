
import {game} from "./game";

class CartItem {
  game: game;
  quantity: number;

  constructor(product: game, quantity: number) {
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
