import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import {BASE_IMAGE_URL} from "../../global";

@Component({
  selector: 'app-library',
  templateUrl: './library.component.html',
  styleUrl: './library.component.css'
})
export class LibraryComponent {
  protected games: any;
  constructor(private userService: UserService) {
    this.userService.getUserLibrary().subscribe((games: any) => {
      this.games = games;
    });
  }

    protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
}
