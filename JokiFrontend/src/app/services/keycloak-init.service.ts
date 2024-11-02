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
        onLoad: 'check-sso',      // login-required richiede il login all'avvio della'applicazione
                                  // check-sso controlla se l'utente è già autenticato
                                  // none non fa nulla

        checkLoginIframe: true,  //checkLoginIframe: true: Abilita il controllo periodico dello stato di autenticazione tramite l'iframe.
                                //checkLoginIframe: false: Disabilita il controllo dello stato di autenticazione. L'applicazione non verificherà automaticamente
                                // se l'utente è ancora autenticato dopo l'iniziale autenticazione.

        checkLoginIframeInterval: 5  //Intervallo di controllo dello stato di autenticazione in secondi. Default: 5
      }
    });
  }
}
