import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';

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
}
