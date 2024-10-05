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

  uploadPhoto(id: string, index: number, file: File): Observable<string> {
    const formData: FormData = new FormData();
    formData.append('file', file);

    return this.httpClient.put<string>(`${this.adminUrl}/${id}/${index}`, formData, {
      headers: new HttpHeaders({
        'Accept': 'application/json'
      })
    });
  }

  deletePhoto(id: string, index: number): Observable<HttpResponse<string>> {
    return this.httpClient.delete<string>(`${this.adminUrl}/${id}/${index}`, {
      observe: 'response'
    });
  }

  getGamesList(): Observable<Game[]> {
    return this.httpClient.get<string>(this.apiUrl)
      .pipe(
        map(response => response as unknown as Game[])
      );
  }

  getGame(id: string): Observable<Game> {
    return this.httpClient.get<any>(`${this.apiUrl}/${id}`)
    .pipe(
        map(response => response as Game)
    )
  }

  addGame(game: Game): Observable<Game> {
    return this.httpClient.post<any>(this.adminUrl, game, {
      observe: 'response'
    }).pipe(
        map(response => response.body as Game)
    );
  }

  updateGame(game: Game): Observable<string> {
    return this.httpClient.put<string>(`${this.adminUrl}/${game.id}`, game);
  }
}