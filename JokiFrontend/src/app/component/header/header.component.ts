import { Component, OnInit } from '@angular/core';
import {CartService} from "../../services/cart.service";
import {Observable} from "rxjs";
import {AdminService} from "../../services/admin.service";
import {UserService} from "../../services/user.service";
import {LogoutComponent} from "../logout/logout.component";
import {KeycloakService} from "keycloak-angular";
import {ViewChild} from "@angular/core";
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  nProducts: Observable<number> = new Observable<number>();
  showCart: boolean = false;
  hasNewMessages: boolean = false;
  pollingInterval: any; 

  constructor(
    private cartService: CartService,
    protected adminService: AdminService,
    protected userService: UserService,
    private keycloakService: KeycloakService,
    private messageService: MessageService) {
    this.nProducts = this.cartService.getQuantity();
  }

  ngOnInit() {
    this.pollingInterval = setInterval(() => {
      this.messageService.getUserCount();
      if (this.messageService.getUserCountValue() > 0) {
        this.hasNewMessages = true;
      } else {
        this.hasNewMessages = false;
      }
    }, 10000); // Controlla ogni 10 secondi
  }

  ngOnDestroy() {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
    }
  }

  toggleCart() {
    this.showCart = !this.showCart;
  }

  updateCart() {
    this.nProducts = this.cartService.getQuantity();
  }
}
