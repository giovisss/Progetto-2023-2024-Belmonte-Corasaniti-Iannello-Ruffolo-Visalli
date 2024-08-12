import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8081/api/user/personal';

  constructor(private httpClient:HttpClient) { }

}
