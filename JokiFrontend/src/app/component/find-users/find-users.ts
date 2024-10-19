import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {BASE_IMAGE_URL} from "../../global";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-find-users',
  templateUrl: './find-users.html',
  styleUrls: ['./find-users.css']
})
export class FindUsers {
  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;

  protected selectedUser: User | null = null;
  protected users: User[] = [];
  protected searchedUsers: User[] = [];

  btnText: string = 'Richiedi amicizia';




  constructor(private userService: UserService) {
    this.userService.getUserList(false).subscribe((response: User[]) => {
      this.users = response;
    });
  }

  protected OnInput(event: any) {
    if (event.target.value === '') {
      this.searchedUsers = [];
      return;
    }

    this.searchedUsers = [];
    for (let user of this.users) {
      if (user.username.toLocaleLowerCase().includes(event.target.value.toLocaleLowerCase())) {
        this.searchedUsers.push(user);
      }
    }
  }

  protected onUserClick(user: User) {
    this.selectedUser = user;
    this.searchedUsers = [];

    this.checkFriendship()
  }

  protected checkFriendship() {
    this.userService.checkFriendship(this.selectedUser!!.username).subscribe((response: HttpResponse<string>) => {
        if (response.ok) {
          console.log(response.body);
          switch (response.body) {
            case 'NOT_FRIENDS':
              this.btnText = 'Richiedi amicizia';
              break;
            case 'FRIENDS':
              this.btnText = 'Rimuovi amicizia';
              break;
            case 'PENDING':
              this.btnText = 'Richiesta inviata';
              break;
            case 'REQUESTED':
              this.btnText = 'Accetta amicizia';
              break;
          }
        }
    });
  }

  protected btnClick() {
    let positive: boolean = false;

    switch (this.btnText) {
      case 'Richiedi amicizia':
        positive = true;
        break;
      case 'Accetta amicizia':
        positive = true;
        break;
    }

    this.userService.editFriendship(positive, this.selectedUser!!.username).subscribe((response: HttpResponse<string>) => {
        if (response.ok) {
            this.checkFriendship()
        }
    })
  }

}
