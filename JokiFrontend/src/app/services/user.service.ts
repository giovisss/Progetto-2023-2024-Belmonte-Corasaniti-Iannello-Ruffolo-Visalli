import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, of } from 'rxjs';
import { AuthGuard } from '../guard/auth.guard';
import { game } from '../model/game';
import { BASE_API_URL } from '../global';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = BASE_API_URL + '/users';

  constructor(private httpClient: HttpClient, private auth: AuthGuard) { }

  test() {
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

  getUserLibrary(): Observable<game[]> {
    return this.httpClient.get<string>(this.apiUrl + '/games')
      .pipe(
        map(response => response as unknown as game[])
      );
  }
}
