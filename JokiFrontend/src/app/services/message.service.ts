import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import { KeycloakService } from 'keycloak-angular';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class MessageService {
  private stompClient!: Client;
  private messagesSubject = new BehaviorSubject<{ type: string, content: string }[]>([]);
  public messages$ = this.messagesSubject.asObservable();

  constructor(private keycloakService: KeycloakService) {}

  connect() {
    this.keycloakService.getToken().then(token => {
      this.stompClient = new Client({
        brokerURL: 'ws://localhost:8081/ws',
        connectHeaders: {
          Authorization: `Bearer ${token}`
        },
        debug: (str) => {
          console.log(str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

      this.stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        this.stompClient.subscribe('/topic/ping', (message: Message) => {
          console.log('Received: ' + message.body);
          this.messagesSubject.next([...this.messagesSubject.getValue(), { type: 'received', content: message.body }]);
        });
      };

      this.stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      };

      this.stompClient.activate();
    }).catch(err => {
      console.error('Error fetching token', err);
    });
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  sendMessage(message: string) {
    if (message.trim()) {
      this.keycloakService.getToken().then(token => {
        this.stompClient.publish({
          destination: '/app/ping',
          body: message,
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        console.log('Sent message: ' + message);
        this.messagesSubject.next([...this.messagesSubject.getValue(), { type: 'sent', content: message }]);
      }).catch(err => {
        console.error('Error fetching token for message', err);
      });
    }
  }
}
