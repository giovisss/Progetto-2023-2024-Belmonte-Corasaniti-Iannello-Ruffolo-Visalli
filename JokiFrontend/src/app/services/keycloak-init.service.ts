import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class KeycloakInitService {
  constructor(private keycloak: KeycloakService) {}

  init(): Promise<boolean> {
    return this.keycloak.init({
      config: {
        url: 'http://localhost:8080',
        realm: 'JokiRealm',
        clientId: 'JokiClient'
      },
      initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false
      }
    });
  }
}
