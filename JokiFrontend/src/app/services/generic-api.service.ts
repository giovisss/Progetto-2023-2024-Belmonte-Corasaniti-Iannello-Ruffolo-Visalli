import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GenericApiService {

  constructor(private token: string) { }

  makeRequest(url: string, method: string, body: any) {
    return fetch(url, {
      method: method,
      headers: {
        'Authorization': `Bearer ${this.token}`
      },
      body: JSON.stringify(body)
    });
  }
}
