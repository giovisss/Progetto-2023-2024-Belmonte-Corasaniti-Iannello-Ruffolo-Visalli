export class User {
  private _username: string;
  private _firstName: string;
  private _lastName: string;
  private _email: string;
  private _birthdate: Date;

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

  set username(username: string) {
    this._username = username;
  }

  set firstName(firstName: string) {
    this._firstName = firstName;
  }

  set lastName(lastName: string) {
    this._lastName = lastName;
  }

  set email(email: string) {
    this._email = email;
  }

  set birthdate(birthdate: Date) {
    this._birthdate = birthdate;
  }

}
