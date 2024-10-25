import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
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
export class UserChatComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('chatContainer') private chatContainer!: ElementRef;

  message: string = '';
  allMessages: ChatMessage[] = [];
  private userMessagesSubscription!: Subscription;
  private shouldScroll = false;

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.messageService.addUser().subscribe(() => {
      console.log('User added');
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
        isUser: true
      };
      this.allMessages.push(newMessage);
      this.messageService.UserSendsToAdmin(this.message);
      this.message = '';
      this.shouldScroll = true;
    }
  }

  ngOnDestroy(): void {
    if (this.userMessagesSubscription) {
      this.userMessagesSubscription.unsubscribe();
    }
    this.messageService.destroy(); // Toglie il polling
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
      this.shouldScroll = true;
    });
  }

  private scrollToBottom(): void {
    try {
      this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    } catch(err) { }
  }
}