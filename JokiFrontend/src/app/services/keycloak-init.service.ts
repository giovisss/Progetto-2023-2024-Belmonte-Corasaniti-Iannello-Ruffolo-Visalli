import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { BASE_KEYCLOAK_URL } from '../global';

@Injectable({
  providedIn: 'root'
})
export class KeycloakInitService {
  constructor(private keycloak: KeycloakService) {}

  init(): Promise<boolean> {
    return this.keycloak.init({
      config: {
        url: BASE_KEYCLOAK_URL,
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
