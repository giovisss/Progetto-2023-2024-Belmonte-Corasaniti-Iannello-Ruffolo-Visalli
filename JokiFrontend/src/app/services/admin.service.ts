import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeycloakService } from 'keycloak-angular';
import { Observable } from 'rxjs';
import { BASE_API_URL } from '../global';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  token: any;
  isAdmin: boolean = false;
  private apiUrl = BASE_API_URL + '/chat'; // URL per aggiungere l'admin alla lista

  constructor(private keycloakService: KeycloakService, private http: HttpClient) {
    try {
      this.token = this.keycloakService.getKeycloakInstance().tokenParsed;
      // Verifica se l'utente ha il ruolo client_admin
      this.isAdmin = this.token.resource_access.JokiBackend.roles.includes('client_admin');
    } catch (e) {
      this.isAdmin = false;
    }
  }

  // Metodo per inviare la richiesta POST per aggiungere l'admin alla lista
  addAdminToList(): Observable<any> {
    if (this.isAdmin) {
      return this.http.post(this.apiUrl + '/admin', {}); // La richiesta al backend
    } else {
      throw new Error('Non autorizzato. Solo gli admin possono aggiungersi alla lista.');
    }
  }
}
