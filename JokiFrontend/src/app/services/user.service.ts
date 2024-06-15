import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient:HttpClient) { }

  getUser() {
    return this.httpClient.get('http://localhost:8081/api/users/4bba16da-998d-4582-b1bb-3ee3b62602db/roles');
  }
}
