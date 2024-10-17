import { Component, OnDestroy } from '@angular/core';
import { MessageService } from '../../services/message.service';
import { AdminService } from '../../services/admin.service';
import { admin } from '../../model/admin';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnDestroy {
  
  constructor(private adminService: AdminService, private messageService: MessageService) {}

  ngOnDestroy() {
    // this.messageService.addAdmin().subscribe(
    //   response => {
    //     console.log('Admin aggiunto con successo:', response);
    //   },
    //   error => {
    //     console.error('Errore durante l\'aggiunta dell\'admin:', error);
    //   }
    // );
  }
}