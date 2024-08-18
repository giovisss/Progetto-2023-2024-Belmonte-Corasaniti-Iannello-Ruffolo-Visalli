import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {KeycloakInitService} from "./services/keycloak-init.service";
//import {initializeKeycloak} from "./init/keycloak-init.factory";
import { HomeComponent } from './component/home/home.component';
import { HeaderComponent } from './component/header/header.component';
import { FooterComponent } from './component/footer/footer.component';
import { ListProductsComponent } from './component/list-products/list-products.component';
import { LoginComponent } from './component/login/login.component';
import { ProductComponent } from './component/product/product.component';
import { CartComponent } from './component/cart/cart.component';
import { HttpClientModule } from "@angular/common/http";
import { LibraryComponent } from './component/library/library.component';
import { AboutComponent } from './component/about/about.component';
import { WishlistsComponent } from './component/wishlists/wishlists.component';
import { WishlistProductsComponent } from './component/wishlist-products/wishlist-products.component';
import {FormsModule} from "@angular/forms";
import { UserInfoComponent } from './component/user-info/user-info.component';

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
    UserInfoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    KeycloakAngularModule,
    HttpClientModule,
    FormsModule
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
