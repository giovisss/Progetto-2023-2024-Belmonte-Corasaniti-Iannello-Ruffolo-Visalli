import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {of} from "rxjs";
import {KeycloakService} from "keycloak-angular";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  token: any;
  isAdmin: boolean = false;

  constructor(private keycloakService: KeycloakService) {
    try {
      this.token = this.keycloakService.getKeycloakInstance().tokenParsed;
      this.isAdmin = this.token.resource_access.JokiBackend.roles.includes('client_admin');
      console.log(this.isAdmin);
    } catch (e) {
      this.isAdmin = false;
    }
  }
}
