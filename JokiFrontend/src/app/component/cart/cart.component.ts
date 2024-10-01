import {Component, EventEmitter, input, OnInit, Output} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {BASE_IMAGE_URL} from "../../global";
import {Observable, Subscription} from "rxjs";
import {Game} from "../../model/game";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  @Output() closeCart = new EventEmitter();
  @Output() cartUpdated = new EventEmitter();
  cart: Observable<Game[]> = new Observable<Game[]>;
  total: Observable<number> = new Observable<number>();

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cart = this.cartService.getCart();
    this.total = this.cartService.getTotal();
  }

  removeItem(game: Game): void {
    this.cartService.removeFromCart(game).subscribe(() => {
      this.cart = this.cartService.getCart();
      this.total = this.cartService.getTotal();
    });
  }

  close() {
    this.closeCart.emit();
  }

  cartUpdate() {
    this.cartUpdated.emit();
  }

  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;


  // constructor(private cartService: CartService) {
  //   this.cart = this.cartService.getCart();
  //   this.total = this.cartService.getTotal();
  // }





  // removeItem(product: Product) {
  //   this.userService.removeFromCart(product);
  //   this.cart = this.userService.getCart();
  //   this.total = this.userService.getTotal();
  //   this.cartUpdate();
  // }

}
