import {Injectable} from '@angular/core';
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
    } catch (e) {
      this.isAdmin = false;
    }
  }
}
