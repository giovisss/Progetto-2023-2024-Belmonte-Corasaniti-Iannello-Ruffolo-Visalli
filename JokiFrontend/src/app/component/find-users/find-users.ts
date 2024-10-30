import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user';
import {BASE_IMAGE_URL} from "../../global";
import {HttpResponse} from "@angular/common/http";
import {WishlistService} from "../../services/wishlist.service";
import {Wishlist} from "../../model/wishlist";
import {Router} from "@angular/router";

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
  protected loggedInUsername: string = '';
  protected loggedUser: User | null = null;

  protected wishlists: Wishlist[] = [];

  btnText: string = 'Richiedi amicizia';


  constructor(private userService: UserService, private wishlistService: WishlistService, private router: Router) {}

  ngOnInit() {
    this.userService.getUserList(false).subscribe((response: User[]) => {
      this.users = response;
    });

    this.userService.getUserInfo().subscribe((userData: User) => {
      this.loggedUser = userData;
      this.loggedInUsername = userData.username;
    });
  }

  protected OnInput(event: any) {

    if (event.target.value === '') {
      this.searchedUsers = [];
      return;
    }

    const searchTerm = event.target.value.toLocaleLowerCase();

    this.searchedUsers = this.users.filter(
      (user) => user.username.toLocaleLowerCase().includes(searchTerm) && user.username !== this.loggedInUsername
    );
  }

  protected onUserClick(user: User) {
    this.selectedUser = user;
    this.searchedUsers = [];
    this.wishlists = [];

    this.wishlistService.getOtherWishlists(user.username).subscribe( response => {
      if (response != null) {
        this.wishlists = response;
      } else {
        alert('Errore nel caricamento delle wishlist!');
      }
    })

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

    protected onWishlistClick(wishlist: Wishlist) {
        this.router.navigate(['/wishlists/other', this.selectedUser?.username, wishlist.wishlistName]);
    }
}
