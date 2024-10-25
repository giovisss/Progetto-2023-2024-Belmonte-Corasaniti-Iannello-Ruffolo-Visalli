import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { Subject, Observable, timer, Subscription } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BASE_API_URL } from '../global';
import { KeycloakService } from 'keycloak-angular';
import { retryWhen, delayWhen } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private userClient!: Client;
  private adminClient!: Client;
  private userMessages = new Subject<{ content: string, timestamp: string }>();
  private adminMessages = new Subject<{ content: string, timestamp: string }>();
  private userId: string | null = null;
  private adminId: string | null = null;
  private userCount: number = 0;
  private apiChatUrl = BASE_API_URL + '/chat';
  private pollingSubscription?: Subscription;
  private readonly POLLING_INTERVAL = 5000; // 5 seconds

  constructor(
    private http: HttpClient,
    private keycloakService: KeycloakService
  ) {
    console.log('API Chat URL:', this.apiChatUrl);
  }

  private initializeClients() {
    if (!this.userId || !this.adminId) {
      console.error('User ID or Admin ID not set');
      return;
    }

    const brokerURL = 'ws://localhost:8081/ws';
    console.log('Initializing clients with broker URL:', brokerURL);

    this.userClient = new Client({
      brokerURL: brokerURL,
      onConnect: () => {
        console.log('User connected');
        this.subscribeToTopics();
      },
    });

    this.adminClient = new Client({
      brokerURL: brokerURL,
      onConnect: () => {
        console.log('Admin connected');
        this.subscribeToTopics();
      },
    });

    this.userClient.activate();
    this.adminClient.activate();
  }

  private subscribeToTopics() {
    if (!this.userClient.connected || !this.adminClient.connected) {
      return;
    }

    const subscriptionTopicUser = `/topic/chat/${this.userId}_${this.adminId}`;
    const subscriptionTopicAdmin = `/topic/chat/${this.adminId}_${this.userId}`;

    this.userClient.subscribe(subscriptionTopicUser, message => {
      const messageContent = JSON.parse(message.body);
      console.log('User received message:', messageContent);
      this.userMessages.next(messageContent);
    });

    this.adminClient.subscribe(subscriptionTopicAdmin, message => {
      const messageContent = JSON.parse(message.body);
      console.log('Admin received message:', messageContent);
      this.adminMessages.next(messageContent);
    });

    console.log('Both clients are connected and subscribed.');
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

  getUserMessages(): Observable<{ content: string, timestamp: string }> {
    return this.userMessages.asObservable();
  }

  getAdminMessages(): Observable<{ content: string, timestamp: string }> {
    return this.adminMessages.asObservable();
  }

  addAdmin(): Observable<string> {
    const url = `${this.apiChatUrl}/admin`;
    console.log('Adding admin, POST request to:', url);
    return this.http.post<string>(url, {});
  }

  getAvailableAdmin() {
    this.stopPolling();
    
    const url = `${this.apiChatUrl}/admin`;
    console.log('Getting available admin, GET request to:', url);

    this.pollingSubscription = timer(0, this.POLLING_INTERVAL)
      .subscribe(() => {
        this.http.get<{ adminId: string }>(url).subscribe({
          next: (response) => {
            console.log('Admin found:', response);
            this.adminId = response.adminId;
            this.initializeUserIdAndClients();
            this.stopPolling();
          },
          error: (error: HttpErrorResponse) => {
            if (error.status === 404) {
              console.log('No admin available, will retry in 5 seconds...');
            } else {
              console.error('Error getting available admin:', error);
              this.stopPolling();
            }
          }
        });
      });
  }

  private stopPolling() {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
      this.pollingSubscription = undefined;
    }
  }

  getUser() {
    const url = `${this.apiChatUrl}/user`;
    console.log('Getting user, GET request to:', url);
    this.http.get<{ userId: string }>(url).subscribe(
      response => {
        this.userId = response.userId;
        console.log('Received user ID:', this.userId);
        this.initializeAdminIdAndClients();
      },
      error => {
        console.error('Error getting user:', error);
      }
    );
  }

  addUser(): Observable<string> {
    const url = `${this.apiChatUrl}/user`;
    console.log('Adding user, POST request to:', url);
    return this.http.post<string>(url, {});
  }

  private initializeUserIdAndClients() {
    try {
      const token = this.keycloakService.getKeycloakInstance().tokenParsed;
      if (token && token.sub) {
        this.userId = token.sub;
        console.log('User ID initialized:', this.userId);
        if (this.adminId) {
          this.initializeClients();
        }
      } else {
        console.error('User ID not found in token');
      }
    } catch (e) {
      console.error('Error initializing user ID', e);
    }
  }

  private initializeAdminIdAndClients() {
    try {
      const token = this.keycloakService.getKeycloakInstance().tokenParsed;
      if (token && token.sub) {
        this.adminId = token.sub;
        console.log('Admin ID initialized:', this.adminId);
        if (this.userId) {
          this.initializeClients();
        }
      } else {
        console.error('Admin ID not found in token');
      }
    } catch (e) {
      console.error('Error initializing admin ID', e);
    }
  }

  getUserCount(): void {
    const url = `${this.apiChatUrl}/user/count`;
    console.log('Getting user count, GET request to:', url);
    this.http.get<{ userCount: number }>(url).subscribe(
      response => {
        this.userCount = response.userCount;
        console.log('Received user count:', this.userCount);
      },
      error => {
        console.error('Error getting user count:', error);
      }
    );
  }

  public getUserCountValue(): number {
    return this.userCount;
  }

  destroy() {
    this.stopPolling();
  }
}