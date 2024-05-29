import {Component, EventEmitter, input, Output} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {Product} from "../../model/product";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  @Output() closeCart = new EventEmitter();
  @Output() cartUpdated = new EventEmitter();

  close() {
    this.closeCart.emit();
  }

  cartUpdate() {
    this.cartUpdated.emit();
  }

  cart = null;
  total = 0;

  constructor(private cartService: CartService) {
    this.cart = this.cartService.getCart();
    this.total = this.cartService.getTotal();
  }

  removeItem(product: Product) {
    this.cartService.removeFromCart(product);
    this.cart = this.cartService.getCart();
    this.total = this.cartService.getTotal();
    this.cartUpdate();
  }
}
