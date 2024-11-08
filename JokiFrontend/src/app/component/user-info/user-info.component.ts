import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import {Date} from "../../utility/Date";

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  user: User | null = null;
  formattedBirthdate: string | null = null;
  userForm!: FormGroup;
  resetPasswordForm!: FormGroup;
  showEditForm = false;
  showPassword: boolean = false;
  showRepeatPassword: boolean = false;
  isSaving = false;
  isResettingPassword = false;
  message = '';

  constructor(private keycloakService: KeycloakService, private userService: UserService, private fb: FormBuilder, private router: Router) { }

    ngOnInit(): void {
      // Inizializza il form senza contrassegnare i campi come obbligatori
      this.userForm = this.fb.group({
        username: [null], // Obbligatorio
        firstName: [null, Validators.maxLength(50)], // Non è obbligatorio
        lastName: [null, Validators.maxLength(50)], // Non è obbligatorio
        email: [null, [Validators.email]] // Non è obbligatorio, ma deve essere un'email valida
      });

      // Recupera le informazioni dell'utente dal backend
      this.userService.getUserInfo().subscribe(
        data => {
          console.log('Fetched user data:', data);
          this.userService.setUser(data);
          this.user = this.userService.getUser();

          if (this.user) {
            this.formattedBirthdate = this._formatBirthdate(this.user);
          }

          // Aggiorna i valori del form con i dati dell'utente
          this.userForm.patchValue({
            username: this.user?.username,
            formattedBirthdate: this.formattedBirthdate,
          });
        },
        error => {
          console.error('Error fetching user data:', error);
        }
      );
    }

  // Funzione per resettare la password
  onResetPassword() {
    this.isResettingPassword = true;
    this.message = '';

    this.userService.resetPassword().subscribe(
      (response) => {
        console.log('User updated successfully', response);
        window.location.reload();
        this.router.navigate(['/user-info']);
      },
      (error) => {
        this.message = 'Si è verificato un errore. Riprova più tardi.';
        console.error('Errore durante la richiesta di reset password:', error);
      }
    );
  }

  onSave() {
    this.isSaving = true;
    const formData = this.userForm.value as { [key: string]: any };

    const updatedData = Object.keys(formData).reduce((acc: { [key: string]: any }, key: string) => {
      if (formData[key] !== null && formData[key] !== '') {
        acc[key] = formData[key];
      }
      return acc;
    }, {});

    if (Object.keys(updatedData).length > 0) {
      console.log('Updated Form Data:', updatedData);
      this.userService.updateUser(updatedData).subscribe(
        (response) => {
          console.log('User updated successfully', response);
          window.location.reload(); // Ricarica la pagina per visualizzare i nuovi dati
        },
        (error) => {
          console.error('Error updating user', error);
        }
      );
    } else {
      console.log('No fields to update');
    }
  }

  private _formatBirthdate(user: User): string {
    return new Date(user.birthdate).toString();
  }
}
