import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { map, Observable, of } from 'rxjs';
import { AuthGuard } from '../guard/auth.guard';
import { Game } from '../model/game';
import { BASE_API_URL } from '../global';
import { User } from '../model/user';
import { KeycloakService } from 'keycloak-angular';
import { Date } from '../utility/Date';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  token: any;
  isUser: boolean = false;
  private apiUrl = BASE_API_URL + '/users';
  private adminApiUrl = BASE_API_URL + '/admin/users';
  private _user: User | null = null;

  constructor(private httpClient: HttpClient, private auth: AuthGuard, private keycloakService: KeycloakService) {
    try {
      this.token = this.keycloakService.getKeycloakInstance().tokenParsed;
      this.isUser = this.token.resource_access.JokiBackend.roles.includes('client_user');
    } catch (e) {
      this.isUser = false;
    }
  }

  firstLogin(): Observable<HttpResponse<string>> {
    return this.httpClient.get<string>(this.apiUrl + '/first-login', { observe: 'response' });
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
        userData.birthdate
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

  // getUserList(isAdmin: boolean = true): Observable<User[]> {
  //   if (isAdmin) {
  //     return this.httpClient.get<User[]>(this.adminApiUrl).pipe(
  //         map(response => {
  //           let out = [];
  //           for (let user of response) {
  //             out.push(new User(user.username, user.firstName, user.lastName, user.email, new Date(user.birthdate.toString())));
  //           }
  //           return out;
  //         })
  //     );
  //   } else {
  //     return this.httpClient.get<User[]>(this.apiUrl).pipe(
  //         map(response => {
  //           let out = [];
  //           for (let user of response) {
  //             out.push(new User(user.username, user.firstName, user.lastName, user.email, new Date(user.birthdate.toString())));
  //           }
  //           return out;
  //         })
  //     );
  //   }
  // }

  getUserList(isAdmin: boolean = true): Observable<User[]> {
    if (isAdmin) {
      return this.httpClient.get<User[]>(this.adminApiUrl);
    } else {
      return this.httpClient.get<User[]>(this.apiUrl);
    }
  }

  updateUserByAdmin(user: User): Observable<HttpResponse<string>> {
    return this.httpClient.put<string>(this.adminApiUrl + "/" + user.username, user, { observe: 'response' });
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
