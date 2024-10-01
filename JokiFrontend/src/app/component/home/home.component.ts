import { Component } from '@angular/core';
import {UserService} from "../../services/user.service";
import {AdminService} from "../../services/admin.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  constructor(protected userService:UserService, protected adminService:AdminService) {}
}
