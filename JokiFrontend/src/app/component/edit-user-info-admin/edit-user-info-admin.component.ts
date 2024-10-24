import {Component} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {DatePipe, Location} from "@angular/common";
import {HttpResponse} from "@angular/common/http";
import {BASE_IMAGE_URL} from "../../global";
import {Date} from "../../utility/Date";

@Component({
  selector: 'app-user-info',
  templateUrl: './edit-user-info-admin.component.html',
  styleUrls: ['./edit-user-info-admin.component.css'],
  providers: [DatePipe]

})
export class EditUserInfoAdminComponent {
  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;

  protected selectedUser: User | null = null;
  protected users: User[] = [];
  protected searchedUsers: User[] = [];
  protected tempUsername: string = '';
  protected tempName: string = '';
  protected tempLastName: string = '';
  protected tempEmail: string = '';
  protected tempDate: string = '';


  constructor(private userService: UserService, private location: Location) {
    this.userService.getUserList().subscribe((response: User[]) => {
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

    this.tempUsername = user.username;
    this.tempName = user.firstName;
    this.tempLastName = user.lastName;
    this.tempEmail = user.email;
    this.tempDate = new Date(user.birthdate).toString();
  }

  protected updateBirthDate(event: any) {
    if (this.tempDate !== null) {
      this.tempDate = event.target.value;
    }
  }

  protected saveChanges() {
      let tmp=new User(
          this.tempUsername,
          this.tempName,
          this.tempLastName,
          this.tempEmail,
          this.tempDate
      )
    console.log(tmp);
      this.userService.updateUserByAdmin(tmp).subscribe((response: HttpResponse<string>) => {
        if(!response.ok) {
          alert('Failed to update user'); }
        else{this.reloadPage()}
      });
  }


  protected reloadPage() {
    this.location.go(this.location.path());
    window.location.reload();
  }


  resetForm() {
    this.selectedUser = null;
    this.searchedUsers = [];

    this.tempUsername = '';
    this.tempName = '';
    this.tempLastName = '';
    this.tempEmail = '';
    this.tempDate = '';
  }


  deleteUser() {
    if (this.selectedUser) {
      this.userService.deleteUser(this.selectedUser.username as string).subscribe((response: HttpResponse<string>) => {
        if(response.ok) {
          this.resetForm();
          this.reloadPage();
        } else { alert('Failed to delete user'); }
      });
    }
  }
}
