import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';
import {ProductsService} from "../../services/products.service";
import {DatePipe, Location} from "@angular/common";
import {HttpResponse} from "@angular/common/http";
import { BASE_IMAGE_URL } from "../../global";

@Component({
  selector: 'app-user-info',
  templateUrl: './edit-user-info-admin.component.html',
  //styleUrl: './edit-user-info-admin.component.css'
  styleUrls: ['./edit-user-info-admin.component.css'],
  providers: [DatePipe]

})
export class EditUserInfoAdminComponent implements OnInit, OnChanges {
  protected readonly User = User;
  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;

  protected selectedUser: User | null = null;
  protected users: User[] = [];
  protected searchedUsers: User[] = [];
  protected tempUser: Partial<User> = {}; // Temporary object

  formattedBirthDate: string | null = '';


  constructor(private userService: UserService, private datePipe: DatePipe, private location: Location) {
    this.userService.getUserList().subscribe((response: User[]) => {
      this.users = response;
    });
  }

  ngOnInit() {
    this.formatBirthDate();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tempUser'] && changes['tempUser'].currentValue['birthdate'] !== changes['tempUser'].previousValue['birthdate']) {
      this.formatBirthDate();
    }
  }

  formatBirthDate() {
    if (this.tempUser['birthdate']) {
      this.formattedBirthDate = this.datePipe.transform(this.tempUser['birthdate'], 'yyyy-MM-dd');
    }
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
    this.tempUser = { ...user }; // Copia utente selezionato in tempUser
    this.formatBirthDate();
    this.searchedUsers = [];
  }

  protected updateBirthDate(event: any) {
    if (this.tempUser) {
      this.tempUser.birthdate = event.target.value;
    }
  }



  protected saveChanges() {

      this.userService.updateUser(this.tempUser as User).subscribe((response: HttpResponse<string>) => {
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
    this.tempUser = {};
    this.formattedBirthDate = '';
    this.searchedUsers = [];
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
