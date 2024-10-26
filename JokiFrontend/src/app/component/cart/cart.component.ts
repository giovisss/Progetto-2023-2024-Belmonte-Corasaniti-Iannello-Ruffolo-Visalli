import { CartService } from "../../services/cart.service";
import { Game } from "../../model/game";
import { Router } from '@angular/router';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Observable } from "rxjs";
import { BASE_IMAGE_URL } from "../../global";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  @Output() closeCart = new EventEmitter();
  @Output() cartUpdated = new EventEmitter();
  cart: Observable<Game[]> = new Observable<Game[]>;
  total: Observable<number> = new Observable<number>();
  isCartEmpty: boolean = true;

  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit(): void {
    this.cart = this.cartService.getCart();
    this.total = this.cartService.getTotal();
    this.cart.subscribe(items => {
      this.isCartEmpty = items.length === 0;
    });
  }

  removeItem(game: Game): void {
    this.cartService.removeFromCart(game).subscribe(() => {
      this.cart = this.cartService.getCart();
      this.total = this.cartService.getTotal();
      this.cart.subscribe(items => {
        this.isCartEmpty = items.length === 0;
      });
    });
  }

  close() {
    this.closeCart.emit();
  }

  cartUpdate() {
    this.cartUpdated.emit();
  }

  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;

  checkout() {
    this.cartService.checkout().subscribe({
        next: () => {
            this.close();
            this.router.navigate(['/checkout']);
        },
        error: (err) => {
            console.error('Errore durante il checkout', err);
        }
    });
  }
}
