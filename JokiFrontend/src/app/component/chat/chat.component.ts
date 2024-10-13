import { Component, OnInit, OnDestroy } from '@angular/core';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  public message: string = '';
  public messages: { type: string, content: string }[] = [];  // Array per messaggi inviati e ricevuti

  constructor(private messageService: MessageService) {}

  ngOnInit() {
    this.messageService.connect();

    // Iscriviti ai messaggi dal servizio
    this.messageService.messages$.subscribe(messages => {
      this.messages = messages; // Aggiorna l'array dei messaggi
    });
  }

  ngOnDestroy() {
    this.messageService.disconnect();
  }

  sendMessage() {
    this.messageService.sendMessage(this.message); // Invia il messaggio tramite il servizio
    this.message = ''; // Resetta il messaggio dopo l'invio
  }
}
