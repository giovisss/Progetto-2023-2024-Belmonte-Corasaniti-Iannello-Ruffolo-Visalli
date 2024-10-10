import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';

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
  showPassword: boolean = false;
  showRepeatPassword: boolean = false;

  constructor(private keycloakService: KeycloakService, private userService: UserService, private fb: FormBuilder) { }

    ngOnInit(): void {
      // Inizializza il form con i campi e i validatori
      this.userForm = this.fb.group({
        username: [null],
        firstName: [null, Validators.maxLength(50)],
        lastName: [null, Validators.maxLength(50)],
        email: [null, [Validators.email]],
        password: [null, [Validators.required, this.passwordStrengthValidator]],
        repeatpassword: [null, [Validators.required]]
      }, { validator: this.passwordsMatchValidator });

      // Recupera le informazioni dell'utente dal backend
      this.userService.getUserInfo().subscribe(
        data => {
          console.log('Fetched user data:', data);
          this.userService.setUser(data);
          this.user = this.userService.getUser();

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

    // Funzione di validazione per la forza della password
    passwordStrengthValidator(control: AbstractControl) {
      const password = control.value;

      if (!password) {
        return null; // Non fare nulla se il campo è vuoto
      }

      // La regex richiede:
      // - Almeno 16 caratteri
      // - Almeno una lettera maiuscola
      // - Almeno una lettera minuscola
      // - Almeno un simbolo
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{16,}$/;

      const valid = passwordRegex.test(password);

      return valid ? null : { weakPassword: true };
    }

  passwordsMatchValidator(control: AbstractControl) {
    const password = control.get('password')?.value;
    const repeatPassword = control.get('repeatpassword')?.value;

    if (password && repeatPassword && password !== repeatPassword) {
      control.get('repeatpassword')?.setErrors({ mismatch: true });
    } else {
      control.get('repeatpassword')?.setErrors({ mismatch: false });
    }
    return null;
  }

  // Funzione per alternare la visibilità della password
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  // Funzione per alternare la visibilità della password ripetuta
  toggleRepeatPasswordVisibility() {
    this.showRepeatPassword = !this.showRepeatPassword;
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
