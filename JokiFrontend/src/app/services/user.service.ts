import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { map, Observable, of } from 'rxjs';
import { AuthGuard } from '../guard/auth.guard';
import { Game } from '../model/game';
import { BASE_API_URL } from '../global';
import { User } from '../model/user';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  token: any;
  isUser: boolean = false;
  private apiUrl = BASE_API_URL + '/users';
  private adminApiUrl = BASE_API_URL + '/admin/users';
  //private userApiUrl = '/api/v1/users'
  private _user: User | null = null;

  constructor(private httpClient: HttpClient, private auth: AuthGuard, private keycloakService: KeycloakService) {
    try {
      this.token = this.keycloakService.getKeycloakInstance().tokenParsed;
      this.isUser = this.token.resource_access.JokiBackend.roles.includes('client_user');
    } catch (e) {
      this.isUser = false;
    }
  }

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
    //     const jsonResponse = JSON.parse(response);Quando viene premuto sul bottone "resetta password" il programma chiama lo script che setta il "Required user actions Update Password" di keycloak su TRUE e l'utente viene reindirizzato su una nuova pagina per la modifica della password
    //     console.log(jsonResponse);
    //   } catch (e) {
    //     console.log(response);
    //   }
    // });

    // this.auth.keycloak.getToken().then(token => {
    //   console.log(token);
    // })
  }

  resetPassword(): Observable<any> {
    return this.httpClient.post(this.apiUrl + `/reset-password`, {});
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
    return this.httpClient.get<Game[]>(this.apiUrl + '/user/library').pipe(
        map(response => {
          console.log(response);
          let out = [];
          for (let game of response) {
            out.push(new Game(game.id, game.title, game.description, game.price, game.imagePath, game.genre, game.developer, game.publisher, game.releaseDate, game.stock, game.admin));
          }
          return out;
        })
    );
  }

  updateUser(userData: any): Observable<any> {
    return this.httpClient.put(`${this.apiUrl}/user`, userData);
  }

  getUserInfo(): Observable<any> {
    return this.httpClient.get<User>(this.apiUrl + '/user');
  }

  getUserList(isAdmin: boolean = true): Observable<User[]> {
    if (isAdmin) {
      return this.httpClient.get<User[]>(this.adminApiUrl);
    } else {
      return this.httpClient.get<User[]>(this.apiUrl);
    }
  }

  updateUserByAdmin(userData: User): Observable<HttpResponse<string>> {
    return this.httpClient.put<string>(this.adminApiUrl + "/" + userData.username, userData, { observe: 'response' });
  }

  deleteUser(username: string): Observable<HttpResponse<string>> {
    return this.httpClient.delete<string>(this.adminApiUrl + "/" + username, { observe: 'response' });
  }

  checkFriendship(username: string): Observable<HttpResponse<string>> {
    return this.httpClient.get<string>(this.apiUrl + "/user/friends/" + username, { observe: 'response' });
  }

  editFriendship(positive: boolean, username: string): Observable<HttpResponse<string>> {
    return this.httpClient.put<string>(this.apiUrl + "/user/friends/" + username + "/" + positive, {}, { observe: 'response' });
  }
}
