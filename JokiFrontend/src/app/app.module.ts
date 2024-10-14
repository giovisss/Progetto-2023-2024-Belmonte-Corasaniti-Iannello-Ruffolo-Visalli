import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';  // <-- Aggiungi BrowserAnimationsModule
import { HttpClientModule } from "@angular/common/http";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

// Moduli di Angular Material
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';  // <-- Aggiungi MatButtonModule

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { KeycloakAngularModule } from "keycloak-angular";
import { KeycloakInitService } from "./services/keycloak-init.service";

import { HomeComponent } from './component/home/home.component';
import { HeaderComponent } from './component/header/header.component';
import { FooterComponent } from './component/footer/footer.component';
import { ListProductsComponent } from './component/list-products/list-products.component';
import { LoginComponent } from './component/login/login.component';
import { ProductComponent } from './component/product/product.component';
import { CartComponent } from './component/cart/cart.component';
import { LibraryComponent } from './component/library/library.component';
import { AboutComponent } from './component/about/about.component';
import { WishlistsComponent } from './component/wishlists/wishlists.component';
import { WishlistProductsComponent } from './component/wishlist-products/wishlist-products.component';
import { UserInfoComponent } from './component/user-info/user-info.component';
import { EditGameComponent } from './component/edit-game/edit-game.component';
import { ReviewComponent } from './component/review/review.component';
import { EditUserInfoAdminComponent } from "./component/edit-user-info-admin/edit-user-info-admin.component";
import { CheckoutComponent } from './component/checkout/checkout.component';

// Funzione per l'inizializzazione di Keycloak
function initializeKeycloak(keycloak: KeycloakInitService) {
  return () => keycloak.init();
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    ListProductsComponent,
    LoginComponent,
    ProductComponent,
    CartComponent,
    LibraryComponent,
    AboutComponent,
    WishlistsComponent,
    WishlistProductsComponent,
    UserInfoComponent,
    EditGameComponent,
    ReviewComponent,
    EditUserInfoAdminComponent,
    CheckoutComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,  // <-- Aggiungi BrowserAnimationsModule qui
    AppRoutingModule,
    KeycloakAngularModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatSidenavModule,  // <-- Aggiungi MatSidenavModule qui
    MatButtonModule  // <-- Aggiungi MatButtonModule qui
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakInitService],
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
