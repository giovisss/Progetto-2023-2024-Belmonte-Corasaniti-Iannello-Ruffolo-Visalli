import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-admin-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.css']
})
export class AdminChatComponent implements OnInit, OnDestroy {
  message: string = '';
  messages: { content: string, timestamp: string }[] = [];
  private adminMessagesSubscription!: Subscription;

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.adminMessagesSubscription = this.messageService.getAdminMessages().subscribe((newMessages) => {
      this.messages = newMessages;
    });
  }

  sendMessage() {
    if (this.message.trim()) {
      this.messageService.AdminSendsToUser(this.message);
      this.message = ''; // Resetta l'input dopo l'invio
    }
  }

  ngOnDestroy(): void {
    if (this.adminMessagesSubscription) {
      this.adminMessagesSubscription.unsubscribe();
    }
  }
}
