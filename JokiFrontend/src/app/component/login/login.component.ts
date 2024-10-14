import { Component, OnDestroy } from '@angular/core';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy {

  constructor(private adminService: AdminService) {}

  ngOnDestroy() {
    if (this.adminService.isAdmin) {
      this.adminService.addAdminToList().subscribe(
        response => {
          console.log('Admin aggiunto con successo:', response);
        },
        error => {
          console.error('Errore durante l\'aggiunta dell\'admin:', error);
        }
      );
    }
  }

}
