import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { of } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard extends KeycloakAuthGuard {

  constructor(
    protected override readonly router: Router,
    protected readonly keycloak: KeycloakService,
    private http: HttpClient
  ) {
    super(router, keycloak);
  }

  async isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean | UrlTree> {

    if (!this.authenticated) {
      await this.keycloak.login({
        redirectUri: window.location.origin + state.url,
      });
    }

    return this.checkAuthorizationWithBackend(route, state);
  }

  // Send a request to the backend to check if the user is authorized
  private async checkAuthorizationWithBackend(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    const url = 'http://localhost:8080/realms/JokiRealm';
    const token = await this.keycloak.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post<{ authorized: boolean }>(url, {
      user: this.keycloak.getUsername(),
      route: state.url
    }, { headers }).pipe(
      map(response => response.authorized), // Garantisce che sia sempre boolean
      catchError(() => of(false)) // Gestisce eventuali errori restituendo false
    ).toPromise() as Promise<boolean>; // Forziamo il tipo di ritorno come booleano
  }
}


// import { Injectable } from '@angular/core';
// import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
// import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
//
// @Injectable({
//   providedIn: 'root'
// })
// export class AuthGuard extends KeycloakAuthGuard {
//
//   constructor(
//     protected override readonly router: Router,
//     protected readonly keycloak: KeycloakService
//   ) {
//     super(router, keycloak);
//   }
//
//   async isAccessAllowed(
//     route: ActivatedRouteSnapshot,
//     state: RouterStateSnapshot): Promise<boolean | UrlTree> {
//
//     if (!this.authenticated) {
//       await this.keycloak.login({
//         redirectUri: window.location.origin + state.url,
//       });
//     }
//
//     return this.authenticated;
//   }
// }
