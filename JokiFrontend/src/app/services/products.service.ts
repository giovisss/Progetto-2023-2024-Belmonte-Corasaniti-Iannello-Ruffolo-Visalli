import { Injectable } from '@angular/core';
import {map, Observable, of} from "rxjs";
import { HttpClient } from '@angular/common/http';
import { game } from '../model/game';
import { BASE_API_URL } from '../global';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  private apiUrl = BASE_API_URL + '/games';

  constructor(private httpClient: HttpClient) { }

  getGamesList(): Observable<game[]> {
    return this.httpClient.get<any>(this.apiUrl).pipe(
      map(response => response._embedded.modelList.map((item: any) => item.model))
    );
  }

  getGame(id: string): Observable<game | undefined> {
    return this.httpClient.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(response => response.model)
    );
  }
}