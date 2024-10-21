import { Component } from '@angular/core';
import {CartService} from "../../services/cart.service";
import {Observable} from "rxjs";
import {AdminService} from "../../services/admin.service";
import {UserService} from "../../services/user.service";
import {LogoutComponent} from "../logout/logout.component";
import {KeycloakService} from "keycloak-angular";
import {ViewChild} from "@angular/core";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  nProducts: Observable<number> = new Observable<number>();
  showCart: boolean = false;

  constructor(private cartService: CartService, protected adminService: AdminService, protected userService: UserService, private keycloakService: KeycloakService) {
    this.nProducts = this.cartService.getQuantity();
  }

  toggleCart() {
    this.showCart = !this.showCart;
  }

  updateCart() {
    this.nProducts = this.cartService.getQuantity();
  }
}
