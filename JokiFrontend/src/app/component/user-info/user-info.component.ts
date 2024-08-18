import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';


@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.css'
})
export class UserInfoComponent implements OnInit {
  user: any;

  constructor(private keycloakService: KeycloakService) {}

  ngOnInit(): void {
    this.user = this.keycloakService.getKeycloakInstance().tokenParsed;
  }
}
