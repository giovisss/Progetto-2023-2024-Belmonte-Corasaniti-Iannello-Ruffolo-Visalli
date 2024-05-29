import { Component } from '@angular/core';
import {CartService} from "../../services/cart.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  nProducts: number = 0;
  showCart: boolean = false;

  constructor(private cartService: CartService) {
    this.nProducts = this.cartService.getQuantity();
  }

  toggleCart() {
    this.showCart = !this.showCart;
  }

  updateCart() {
    this.nProducts = this.cartService.getQuantity();
  }
}
