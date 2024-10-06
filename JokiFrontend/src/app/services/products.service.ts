import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {map, Observable} from 'rxjs';
import { BASE_API_URL } from '../global';
import { Game } from '../model/game';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  private adminUrl = `${BASE_API_URL}/admin/games`;
  private apiUrl = `${BASE_API_URL}/games`;

  constructor(private httpClient: HttpClient) {}

  uploadPhoto(id: string, index: number, file: File): Observable<HttpResponse<string>> {
    const formData: FormData = new FormData();
    formData.append('file', file);

    return this.httpClient.put<string>(`${this.adminUrl}/${id}/${index}`, formData, {
      headers: new HttpHeaders({
        'Accept': 'application/json'
      }),
        observe: 'response'
    });
  }

  deletePhoto(id: string, index: number): Observable<HttpResponse<string>> {
    return this.httpClient.delete<string>(`${this.adminUrl}/${id}/${index}`, {
      observe: 'response'
    });
  }

  getGamesList(): Observable<Game[]> {
    return this.httpClient.get<any>(this.apiUrl).pipe(
      map(response => {
        let out = [];
        for (let game of response._embedded.modelList.map((model: any) => model.model)) {
          out.push(new Game(game.id, game.title, game.description, game.price, game.imagePath, game.genre, game.developer, game.publisher, game.releaseDate, game.stock, game.admin));
        }
        return out;
      })
    );
  }

  getGame(id: string): Observable<Game> {
    return this.httpClient.get<any>(`${this.apiUrl}/${id}`)
    .pipe(
        map(response => {
            let game = response.model;
            return new Game(game.id, game.title, game.description, game.price, game.imagePath, game.genre, game.developer, game.publisher, game.releaseDate, game.stock, game.admin);
        })
    )
  }

  addGame(game: Game): Observable<Game> {
    return this.httpClient.post<Game>(this.adminUrl, game).pipe(
        map(game => {
            return new Game(game.id, game.title, game.description, game.price, game.imagePath, game.genre, game.developer, game.publisher, game.releaseDate, game.stock, game.admin);
        })
    );
  }

  updateGame(game: Game): Observable<HttpResponse<string>> {
    return this.httpClient.put<string>(`${this.adminUrl}/${game.id}`, game, {
        observe: 'response'
    });
  }

    deleteGame(id: string): Observable<HttpResponse<string>> {
        return this.httpClient.delete<string>(`${this.adminUrl}/${id}`, {
            observe: 'response'
        });
    }
}