import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';
import {Component, Input, Output, EventEmitter} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(private router: Router, private userService: UserService){
    this.userService.firstLogin().pipe(catchError(() => { return of(null) })).subscribe((response: HttpResponse<string> | null) => {
      if (response?.ok) {
        this.router.navigate(['']);
      } else {
        alert("Errore nella verifica dell'utente");
      }
    });
  }
}


