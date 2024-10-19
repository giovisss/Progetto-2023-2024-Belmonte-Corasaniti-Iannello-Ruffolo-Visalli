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
    this.userMessagesSubscription = this.messageService.getUserMessages().subscribe((newMessage) => {
      const formattedMessage: ChatMessage = {
        content: newMessage.content,
        timestamp: new Date(newMessage.timestamp),
        isUser: false
      };
      this.allMessages.push(formattedMessage);
      this.allMessages.sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime());
    });
  }
}