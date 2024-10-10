import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  //styleUrl: './edit-user-info-admin.component.css'
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  //user: any;
  user: User | null = null;
  formattedBirthdate: string | null = null;
  userForm!: FormGroup;
  showEditForm = false;

  constructor(private keycloakService: KeycloakService, private userService: UserService, private fb: FormBuilder) { }

  ngOnInit(): void {
    // Inizializza il form con campi vuoti o placeholder
    this.userForm = this.fb.group({
      username: [null],
      firstName: [null, Validators.maxLength(50)],
      lastName: [null, Validators.maxLength(50)],
      email: [null, [Validators.email]],
      password: [null, Validators.minLength(6)],
      repeatpassword: [null, Validators.minLength(6)]
    });

    // Recupera le informazioni dell'utente dal backend
    this.userService.getUserInfo().subscribe(
      data => {
        console.log('Fetched user data:', data);
        this.userService.setUser(data); // Imposta l'utente nel servizio
        this.user = this.userService.getUser(); // Recupera l'utente

        // Aggiorna i valori del form con i dati dell'utente
        this.userForm.patchValue({
          username: this.user?.username,
        });
      },
      error => {
        console.error('Error fetching user data:', error);
      }
    );
  }

  onSave() {
    if (this.userForm.valid) {
      const formData = this.userForm.value;
      // Invia i dati al backend
      this.userService.updateUser(formData).subscribe(
        (response) => {
          console.log('Utente aggiornato con successo', response);
        },
        (error) => {
          console.error('Errore durante l\'aggiornamento dell\'utente', error);
        }
      );
    } else {
      console.error('Il form non è valido');
    }
  }
}
