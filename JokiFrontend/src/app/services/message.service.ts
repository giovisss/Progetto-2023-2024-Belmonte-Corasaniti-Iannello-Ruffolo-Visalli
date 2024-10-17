import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { BASE_API_URL } from '../global';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private userClient!: Client;
  private adminClient!: Client;
  private userMessages = new BehaviorSubject<{ content: string, timestamp: string }[]>([]);
  private adminMessages = new BehaviorSubject<{ content: string, timestamp: string }[]>([]);
  private userId: string | null = null;
  private adminId: string | null = null;
  private apiChatUrl = BASE_API_URL + '/chat';
  private isUserConnected = false;
  private isAdminConnected = false;

  constructor(
    private http: HttpClient,
    private keycloakService: KeycloakService
  ) {
    console.log('API Chat URL:', this.apiChatUrl);
  }

  private initializeUserId() {
    try {
      const token = this.keycloakService.getKeycloakInstance().tokenParsed;
      if (token && token.sub) {
        this.userId = token.sub;
        console.log('User ID initialized:', this.userId);
        this.initializeUserClient();
      } else {
        console.error('User ID not found in token');
      }
    } catch (e) {
      console.error('Error initializing user ID', e);
    }
  }

  private initializeUserClient() {
    if (!this.userId) {
      console.error('User ID not set');
      return;
    }

    const brokerURL = 'ws://localhost:8081/ws';
    console.log('Initializing user client with broker URL:', brokerURL);

    this.userClient = new Client({
      brokerURL: brokerURL,
      onConnect: () => {
        console.log('User connected');
        this.isUserConnected = true;
        this.connectClients(); // Chiama il metodo per connettere i client
      },
    });
    this.userClient.activate();
  }

  private initializeAdminClient() {
    if (!this.userId || !this.adminId) {
      console.error('User ID or Admin ID not set');
      return;
    }

    const brokerURL = 'ws://localhost:8081/ws';
    console.log('Initializing admin client with broker URL:', brokerURL);

    this.adminClient = new Client({
      brokerURL: brokerURL,
      onConnect: () => {
        console.log('Admin connected');
        this.isAdminConnected = true;
        this.connectClients(); // Chiama il metodo per connettere i client
      },
    });
    this.adminClient.activate();
  }

  private connectClients() {
    // Controlla se entrambi i client sono connessi
    if (this.isUserConnected && this.isAdminConnected) {
      const subscriptionTopicUser = `/topic/chat/${this.userId}_${this.adminId}`;
      const subscriptionTopicAdmin = `/topic/chat/${this.adminId}_${this.userId}`;

      // Sottoscrivi il client utente
      this.userClient.subscribe(subscriptionTopicUser, message => {
        const messageContent = JSON.parse(message.body);
        console.log('User received message:', messageContent);
        this.userMessages.next([...this.userMessages.getValue(), messageContent]);
      });

      // Sottoscrivi il client admin
      this.adminClient.subscribe(subscriptionTopicAdmin, message => {
        const messageContent = JSON.parse(message.body);
        console.log('Admin received message:', messageContent);
        this.adminMessages.next([...this.adminMessages.getValue(), messageContent]);
      });

      console.log('Both clients are connected and subscribed.');
    }
  }

  UserSendsToAdmin(message: string) {
    if (this.userClient.connected && this.userId && this.adminId) {
      const destination = `/app/chat/user-to-admin/${this.userId}/${this.adminId}`;
      console.log('User sending message to:', destination);
      this.userClient.publish({
        destination: destination,
        body: JSON.stringify({ content: message })
      });
    } else {
      console.error('User client not connected or IDs not set');
    }
  }

  AdminSendsToUser(message: string) {
    if (this.adminClient.connected && this.userId && this.adminId) {
      const destination = `/app/chat/admin-to-user/${this.userId}/${this.adminId}`;
      console.log('Admin sending message to:', destination);
      this.adminClient.publish({
        destination: destination,
        body: JSON.stringify({ content: message })
      });
    } else {
      console.error('Admin client not connected or IDs not set');
    }
  }

  getUserMessages() {
    return this.userMessages.asObservable();
  }

  getAdminMessages() {
    return this.adminMessages.asObservable();
  }

  addAdmin(): Observable<string> {
    const url = `${this.apiChatUrl}/admin`;
    console.log('Adding admin, POST request to:', url);
    return this.http.post<string>(url, {});
  }

  getAvailableAdmin() {
    const url = `${this.apiChatUrl}/admin`;
    console.log('Getting available admin, GET request to:', url);
    this.http.get<{ adminId: string }>(url).subscribe(
      response => {
        this.adminId = response.adminId;
        console.log('Received admin ID:', this.adminId);
        this.initializeUserId();
        this.initializeAdminClient(); // Inizializza anche il client admin
      },
      error => {
        console.error('Error getting available admin:', error);
      }
    );
  }
}