import { Component, OnInit, OnDestroy } from '@angular/core';
import { MessageService } from './services/message.service';
import { KeycloakService } from 'keycloak-angular';
import { AdminService } from './services/admin.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'JokiFrontend';
  public message: string = '';
  public messages: { type: string, content: string }[] = [];
  public userId: string = 'user123';
  public adminId: string = 'admin123';

  constructor(private messageService: MessageService) {}

  ngOnInit() {
    // Iscriviti ai messaggi dal servizio
    this.messageService.messages$.subscribe(messages => {
      this.messages = messages; // Aggiorna l'array dei messaggi
    });

    // Connetti il servizio
    this.messageService.connect(this.userId, this.adminId); // Passa gli ID utente e admin
  }

  ngOnDestroy() {
    // Disconnetti il servizio quando il componente viene distrutto
    this.messageService.disconnect();
  }

  sendMessage() {
    if (this.message.trim()) {
      this.messageService.sendMessage(this.userId, this.adminId, this.message); // Invia il messaggio tramite il servizio
      this.message = ''; // Resetta il campo input dopo l'invio
    }
  }
}
