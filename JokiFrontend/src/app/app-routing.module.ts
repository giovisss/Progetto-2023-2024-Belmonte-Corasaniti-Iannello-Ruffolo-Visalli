import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./component/home/home.component";
import {UserInfoComponent} from "./component/user-info/user-info.component";
import {LoginComponent} from "./component/login/login.component";
import {ProductComponent} from "./component/product/product.component";
import {LibraryComponent} from "./component/library/library.component";
import {AboutComponent} from "./component/about/about.component";
import {WishlistsComponent} from "./component/wishlists/wishlists.component";
import {WishlistProductsComponent} from "./component/wishlist-products/wishlist-products.component";
import {AuthGuard} from "./guard/auth.guard";
import {EditGameComponent} from "./component/edit-game/edit-game.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'product/:id', component: ProductComponent },
  { path: 'wishlists/:name', component: WishlistProductsComponent },
  // { path: 'api/admin/personal', component: LoginComponent },
  // { path: 'api/user/personal', component: LoginComponent },
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard] },
  { path: 'user-info', component: UserInfoComponent},
  { path: 'library', component: LibraryComponent},
  { path: 'wishlists', component: WishlistsComponent},
  { path: 'about', component: AboutComponent},
  { path: 'editGames', component: EditGameComponent },
  { path: '**', redirectTo: 'home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
