import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {BASE_IMAGE_URL} from "../../global";

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
  }

}
