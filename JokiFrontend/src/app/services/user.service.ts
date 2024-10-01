import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, of } from 'rxjs';
import { AuthGuard } from '../guard/auth.guard';
import { Game } from '../model/game';
import { BASE_API_URL } from '../global';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = BASE_API_URL + '/users';
  private _user: User | null = null;

  constructor(private httpClient: HttpClient, private auth: AuthGuard) { }

  keycloakAuth() {
    // Make HTTP request with authorization token
    this.auth.keycloak.getToken().then(token => {
      console.log(token);
      this.httpClient.get('http://localhost:8081/tmp/v1/admin/personal', {
        headers: {
          Authorization: `Bearer ${token}`
        },
        responseType: 'text'
      }).subscribe(response => {
        try {
          const jsonResponse = JSON.parse(response);
          console.log(jsonResponse);
        } catch (e) {
          console.log(response);
        }
      });
    });

    // this.httpClient.get('http://localhost:8081/tmp/admin/personal', {
    //   responseType: 'text'
    // }).subscribe(response => {
    //   try {
    //     const jsonResponse = JSON.parse(response);
    //     console.log(jsonResponse);
    //   } catch (e) {
    //     console.log(response);
    //   }
    // });

    // this.auth.keycloak.getToken().then(token => {
    //   console.log(token);
    // })
  }

  setUser(userData: any): void {
      this._user = new User(
        userData.username,
        userData.firstName,
        userData.lastName,
        userData.email,
        new Date(userData.birthdate)
      );
    }

  // Metodo per ottenere l'utente
  getUser(): User | null {
    return this._user;
  }

  // Metodo per controllare se l'utente è settato
  isUserLoggedIn(): boolean {
    return this._user !== null;
  }

  // Metodo per ottenere la data di nascita formattata
  getFormattedBirthdate(): string | null {
    return this._user ? this._user.formattedBirthdate : null;
  }

  getUserLibrary(): Observable<Game[]> {
    return this.httpClient.get<string>(this.apiUrl + '/user/library')
      .pipe(
        map(response => response as unknown as Game[])
      );
  }

  getUserInfo(): Observable<any> {
    return this.httpClient.get<User>(this.apiUrl + '/user');
  }
}
