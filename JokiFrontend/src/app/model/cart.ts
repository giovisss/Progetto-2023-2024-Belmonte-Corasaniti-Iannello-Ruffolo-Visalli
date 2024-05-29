import {Product} from "./product";

class CartItem {
  product: Product;
  quantity: number;

  constructor(product: Product, quantity: number) {
    this.product = product;
    this.quantity = quantity;
  }
}

export class Cart {
  cartItems: CartItem[] = [];

  constructor(cartItems: CartItem[]) {
    this.cartItems = cartItems;
  }
}
