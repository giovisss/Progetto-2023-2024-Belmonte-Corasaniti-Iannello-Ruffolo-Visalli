import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  constructor(private http: HttpClient, private keycloakService: KeycloakService) {}

  getData() {
    const token = this.keycloakService.getKeycloakInstance().token;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get('http://localhost:8081/api/data', { headers }); //Chiamata al backend
  }
}
