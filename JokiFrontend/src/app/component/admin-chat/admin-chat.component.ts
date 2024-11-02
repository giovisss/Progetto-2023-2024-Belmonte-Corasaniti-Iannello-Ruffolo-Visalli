import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
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
export class AdminChatComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('chatContainer') private chatContainer!: ElementRef;

  message: string = '';
  allMessages: ChatMessage[] = [];
  private adminMessagesSubscription!: Subscription;
  private shouldScroll = false;

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.messageService.addAdmin().subscribe(() => {
      // console.log('Admin added');
    });
    this.connect();
  }

  ngAfterViewChecked() {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  sendMessage() {
    if (this.message.trim()) {
      const newMessage: ChatMessage = {
        content: this.message,
        timestamp: new Date(),
        isAdmin: true
      };
      this.allMessages.push(newMessage);
      this.messageService.AdminSendsToUser(this.message);
      this.message = '';
      this.shouldScroll = true;
    }
  }

  ngOnDestroy(): void {
    if (this.adminMessagesSubscription) {
      this.adminMessagesSubscription.unsubscribe();
    }
  }

  connect() {
    this.messageService.getUser();
    this.adminMessagesSubscription = this.messageService.getAdminMessages().subscribe((newMessage) => {
      const formattedMessage: ChatMessage = {
        content: newMessage.content,
        timestamp: new Date(newMessage.timestamp),
        isAdmin: false
      };
      this.allMessages.push(formattedMessage);
      this.allMessages.sort((a, b) => a.timestamp.getTime() - b.timestamp.getTime());
      this.shouldScroll = true;
    });
  }

  private scrollToBottom(): void {
    try {
      this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    } catch(err) { }
  }
}