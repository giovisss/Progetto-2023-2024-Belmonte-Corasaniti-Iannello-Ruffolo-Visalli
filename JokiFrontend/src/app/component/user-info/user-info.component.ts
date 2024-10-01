import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  //styleUrl: './user-info.component.css'
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  //user: any;
  user: User | null = null;
  formattedBirthdate: string | null = null;

  constructor(private keycloakService: KeycloakService, private userService: UserService) { }

 ngOnInit(): void {
   this.userService.getUserInfo().subscribe(data => {
         console.log('Fetched user data:', data); // Debugging line
         this.userService.setUser(data); // Imposta l'utente nel servizio
         this.user = this.userService.getUser(); // Recupera l'utente
         this.formattedBirthdate = this.userService.getFormattedBirthdate(); // Ottiene la data formattata
       }, error => {
         console.error('Error fetching user data:', error); // Error handling
       });
    //this.user = this.keycloakService.getKeycloakInstance().tokenParsed;
 }
}
