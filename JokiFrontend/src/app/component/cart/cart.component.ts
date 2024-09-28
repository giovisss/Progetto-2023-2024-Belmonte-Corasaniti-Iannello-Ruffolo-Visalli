import {Component, EventEmitter, input, Output} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {Product} from "../../model/product";
import {BASE_IMAGE_URL} from "../../global";
import {UserService} from "../../services/user.service";
import {Observable, Subscription} from "rxjs";
import {game} from "../../model/game";

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

  cart: Observable<game[]> = new Observable<game[]>;
  total = 0;

  // constructor(private cartService: CartService) {
  //   this.cart = this.cartService.getCart();
  //   this.total = this.cartService.getTotal();
  // }

  constructor(private userService: UserService) {
    this.cart = this.userService.getUserCart();
    // this.total = this.userService.getTotal();
  }



  // removeItem(product: Product) {
  //   this.userService.removeFromCart(product);
  //   this.cart = this.userService.getCart();
  //   this.total = this.userService.getTotal();
  //   this.cartUpdate();
  // }

    protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
}
