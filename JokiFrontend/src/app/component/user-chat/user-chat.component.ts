import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageService } from '../../services/message.service';

interface ChatMessage {
  content: string;
  timestamp: Date;
  isUser: boolean;
}

@Component({
  selector: 'app-user-chat',
  templateUrl: './user-chat.component.html',
  styleUrls: ['./user-chat.component.css']
})
export class UserChatComponent implements OnInit, OnDestroy {
  message: string = '';
  allMessages: ChatMessage[] = [];
  private userMessagesSubscription!: Subscription;

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.messageService.addUser().subscribe(() => {
      console.log('User added');
    });
  }

  sendMessage() {
    if (this.message.trim()) {
      const newMessage: ChatMessage = {
        content: this.message,
        timestamp: new Date(),
        isUser: true
      };
      this.allMessages.push(newMessage);
      this.allMessages.sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime());
      this.messageService.UserSendsToAdmin(this.message);
      this.message = '';
    }
  }

  ngOnDestroy(): void {
    if (this.userMessagesSubscription) {
      this.userMessagesSubscription.unsubscribe();
    }
  }
  
  connect() {
    this.messageService.getAvailableAdmin();
    this.userMessagesSubscription = this.messageService.getUserMessages().subscribe((newMessages) => {
      const formattedMessages = newMessages.map(msg => ({
        content: msg.content,
        timestamp: new Date(msg.timestamp),
        isUser: false
      }));
      this.allMessages = [...this.allMessages, ...formattedMessages].sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime());
    });
  }
}
