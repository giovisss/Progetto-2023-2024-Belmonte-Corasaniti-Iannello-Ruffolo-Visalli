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
        onLoad: 'login-required', // TODO: login-required | check-sso | none
                                  // login-required richiede il login all'avvio della'applicazione
                                  // check-sso controlla se l'utente è già autenticato (cercate su chatGPT)
                                  // none non fa nulla
        checkLoginIframe: false // anche questo cercate su chatGPT
      }
    });
  }
}
