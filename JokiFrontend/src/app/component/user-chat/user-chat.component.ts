import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-user-chat',
  templateUrl: './user-chat.component.html',
  styleUrls: ['./user-chat.component.css']
})
export class UserChatComponent implements OnInit, OnDestroy {
  message: string = '';
  messages: { content: string, timestamp: string }[] = [];
  private userMessagesSubscription!: Subscription;

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.userMessagesSubscription = this.messageService.getUserMessages().subscribe((newMessages) => {
      this.messages = newMessages;
    });
  }

  sendMessage() {
    if (this.message.trim()) {
      this.messageService.UserSendsToAdmin(this.message);
      this.message = ''; // Resetta l'input dopo l'invio
    }
  }

  ngOnDestroy(): void {
    if (this.userMessagesSubscription) {
      this.userMessagesSubscription.unsubscribe();
    }
  }
}
