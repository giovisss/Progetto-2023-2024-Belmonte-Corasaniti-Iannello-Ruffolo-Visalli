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
      // Inizializza il form senza contrassegnare i campi come obbligatori
      this.userForm = this.fb.group({
        username: [this.user?.username, Validators.required], // Obbligatorio
        firstName: [null, Validators.maxLength(50)], // Non è obbligatorio
        lastName: [null, Validators.maxLength(50)], // Non è obbligatorio
        email: [null, [Validators.email]], // Non è obbligatorio, ma deve essere un'email valida
        password: [null, this.passwordStrengthValidator], // Non è obbligatorio, ma deve rispettare la validazione della forza
        repeatpassword: [null] // Non è obbligatorio
      }, { validators: this.passwordsMatchValidator });

      // Recupera le informazioni dell'utente dal backend
      this.userService.getUserInfo().subscribe(
        data => {
          console.log('Fetched user data:', data);
          this.userService.setUser(data);
          this.user = this.userService.getUser();

          // Aggiorna i valori del form con i dati dell'utente
//           this.userForm.patchValue({
//             username: this.user?.username,
//              firstName: this.user?.firstName,
//             lastName: this.user?.lastName,
//             email: this.user?.email
//           });
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
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{5,}$/;

      //Per reimpostare la password semplice a 5 caratteri
      //const passwordRegex = /^.{5,}$/;

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
        },
        (error) => {
          console.error('Error updating user', error);
        }
      );
    } else {
      console.log('No fields to update');
    }
  }
}
