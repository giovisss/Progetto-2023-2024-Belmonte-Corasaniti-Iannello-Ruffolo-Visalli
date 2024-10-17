import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageService } from '../../services/message.service';

interface ChatMessage {
  content: string;
  timestamp: Date;
  isAdmin: boolean;
}

@Component({
  selector: 'app-admin-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.css']
})
export class AdminChatComponent implements OnInit, OnDestroy {
  message: string = '';
  allMessages: ChatMessage[] = [];
  private adminMessagesSubscription!: Subscription;

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.adminMessagesSubscription = this.messageService.getAdminMessages().subscribe((newMessages) => {
      this.allMessages = [
        ...this.allMessages,
        ...newMessages.map(msg => ({
          content: msg.content,
          timestamp: new Date(msg.timestamp),
          isAdmin: false
        }))
      ].sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime());
    });
  }

  sendMessage() {
    if (this.message.trim()) {
      const newMessage: ChatMessage = {
        content: this.message,
        timestamp: new Date(),
        isAdmin: true
      };
      this.allMessages.push(newMessage);
      this.allMessages.sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime());
      this.messageService.AdminSendsToUser(this.message);
      this.message = '';
    }
  }

  ngOnDestroy(): void {
    if (this.adminMessagesSubscription) {
      this.adminMessagesSubscription.unsubscribe();
    }
  }
}