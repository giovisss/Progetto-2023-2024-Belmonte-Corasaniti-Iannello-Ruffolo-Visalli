import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private userClient!: Client;
  private adminClient!: Client;
  private userMessages = new BehaviorSubject<{ content: string, timestamp: string }[]>([]);
  private adminMessages = new BehaviorSubject<{ content: string, timestamp: string }[]>([]);

  private userId = 'ab049c2a-d9f1-44c7-a64c-579d8a72f434';
  private adminId = '4bba16da-998d-4582-b1bb-3ee3b62602db';

  constructor() {
    this.initializeUserClient();
    this.initializeAdminClient();
  }

  private initializeUserClient() {
    this.userClient = new Client({
      brokerURL: 'ws://localhost:8081/ws',
      onConnect: () => {
        console.log('User connected');
        this.userClient.subscribe(`/topic/chat/${this.userId}_${this.adminId}`, message => {
          const messageContent = JSON.parse(message.body);
          this.userMessages.next([...this.userMessages.getValue(), messageContent]);
        });
      },
    });
    this.userClient.activate();
  }

  private initializeAdminClient() {
    this.adminClient = new Client({
      brokerURL: 'ws://localhost:8081/ws',
      onConnect: () => {
        console.log('Admin connected');
        this.adminClient.subscribe(`/topic/chat/${this.adminId}_${this.userId}`, message => {
          const messageContent = JSON.parse(message.body);
          this.adminMessages.next([...this.adminMessages.getValue(), messageContent]);
        });
      },
    });
    this.adminClient.activate();
  }

  UserSendsToAdmin(message: string) {
    if (this.userClient.connected) {
      this.userClient.publish({
        destination: `/app/chat/user-to-admin/${this.userId}/${this.adminId}`,
        body: JSON.stringify({ content: message })
      });
    } else {
      console.error('User client not connected');
    }
  }

  AdminSendsToUser(message: string) {
    if (this.adminClient.connected) {
      this.adminClient.publish({
        destination: `/app/chat/admin-to-user/${this.userId}/${this.adminId}`,
        body: JSON.stringify({ content: message })
      });
    } else {
      console.error('Admin client not connected');
    }
  }

  getUserMessages() {
    return this.userMessages.asObservable();
  }

  getAdminMessages() {
    return this.adminMessages.asObservable();
  }
}
