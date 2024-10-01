export class User {
  private _username: string;
  private _firstName: string;
  private _lastName: string;
  private _email: string;
  private _birthdate: Date;
  private _formattedBirthdate: string;

  constructor(
    username: string = '',
    firstName: string = '',
    lastName: string = '',
    email: string = '',
    birthdate: Date = new Date()
  ) {
    this._username = username;
    this._firstName = firstName;
    this._lastName = lastName;
    this._email = email;
    this._birthdate = birthdate;
    this._formattedBirthdate = this._formatBirthdate();
  }

  get username(): string {
    return this._username;
  }

  get firstName(): string {
    return this._firstName;
  }

  get lastName(): string {
    return this._lastName;
  }

  get email(): string {
    return this._email;
  }

  get birthdate(): Date {
    return this._birthdate;
  }

  get formattedBirthdate(): string {
    return this._formattedBirthdate;
  }

  private _formatBirthdate(): string {
    if (!this._birthdate || !(this._birthdate instanceof Date)) {
      return 'Data non valida';
    }
    const day = ('0' + this._birthdate.getDate()).slice(-2);
    const month = ('0' + (this._birthdate.getMonth() + 1)).slice(-2);
    const year = this._birthdate.getFullYear();
    return `${day} - ${month} - ${year}`;
  }
}
