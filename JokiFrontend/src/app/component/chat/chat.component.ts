import { Component, OnInit, OnDestroy } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  private stompClient!: Client;
  public message: string = '';
  public messages: { type: string, content: string }[] = [];  // Array per messaggi inviati e ricevuti

  constructor(private keycloakService: KeycloakService) {}

  ngOnInit() {
    // Ottieni il token da Keycloak e crea il client STOMP
    this.keycloakService.getToken().then(token => {

      // Crea il client STOMP con l'header Authorization
      this.stompClient = new Client({
        brokerURL: 'ws://localhost:8081/ws',
        connectHeaders: {
          Authorization: `Bearer ${token}` // Inserisci il token come Bearer
        },
        debug: (str) => {
          console.log(str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

      // Gestisci la connessione quando il client è connesso
      this.stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);

        // Sottoscrivi al topic per ricevere i messaggi dal server
        this.stompClient.subscribe('/topic/ping', (message: Message) => {
          console.log('Received: ' + message.body);
          this.messages.push({ type: 'received', content: message.body }); // Aggiungi messaggio ricevuto all'array
        });
      };

      // Gestisci gli errori STOMP
      this.stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      };

      // Attiva il client STOMP
      this.stompClient.activate();
    }).catch(err => {
      console.error('Error fetching token', err);
    });
  }

  ngOnDestroy() {
    // Disattiva la connessione quando il componente viene distrutto
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  sendMessage() {
    // Invia il messaggio solo se non è vuoto e la connessione è attiva
    if (this.message.trim()) {
      this.keycloakService.getToken().then(token => {
        this.stompClient.publish({
          destination: '/app/ping',
          body: this.message,
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        console.log('Sent message: ' + this.message);
        this.messages.push({ type: 'sent', content: this.message }); // Aggiungi messaggio inviato all'array
        this.message = ''; // Resetta il messaggio dopo l'invio
      }).catch(err => {
        console.error('Error fetching token for message', err);
      });
    }
  }
}
