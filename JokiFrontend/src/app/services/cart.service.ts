import { Injectable } from '@angular/core';
import {Product} from "../model/product";
import {UserService} from "./user.service";
@Injectable({
  providedIn: 'root'
})
export class CartService {
//forse la dobbiamo togliere e usARE SOLO USER SERVICE
  constructor(userService: UserService) { }

  getCart() {
    return JSON.parse(localStorage.getItem('cart') || '[]');
  }

  addToCart(product: Product) {
    const cart = this.getCart();
    const productInCart = cart.find((item: any) => item.product.id == product.id);
    if (productInCart) {
      productInCart.quantity++;
    } else {
      cart.push({product, quantity: 1});
    }
    localStorage.setItem('cart', JSON.stringify(cart));
  }

  removeFromCart(product: Product) {
    const cart = this.getCart();
    const productInCart = cart.find((item: any) => item.product.id == product.id);
    if (productInCart) {
      productInCart.quantity--;
      if (productInCart.quantity === 0) {
        cart.splice(cart.indexOf(productInCart), 1);
      }
    }
    localStorage.setItem('cart', JSON.stringify(cart));
  }

  clearCart() {
    localStorage.removeItem('cart');
  }

  getTotal() {
    const cart = this.getCart();
    return cart.reduce((total: number, item: any) => total + item.product.price * item.quantity, 0);
  }

  getQuantity() {
    const cart = this.getCart();
    return cart.reduce((total: number, item: any) => total + item.quantity, 0);
  }

  getCartItems() {
    return this.getCart();
  }

  isInCart(product: any) {
    const cart = this.getCart();
    return cart.find((item: any) => item.product.id === product.id);
  }

  isCartEmpty() {
    return this.getCart().length === 0;
  }

  checkout() {
    this.clearCart();
  }
}
