export class User {
  public username: string;
  public firstName: string;
  public lastName: string;
  public email: string;
  public birthdate: string;

  constructor(
    username: string = '',
    firstName: string = '',
    lastName: string = '',
    email: string = '',
    birthdate: string = '',
  ) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.birthdate = birthdate;
  }

  copy(user: User): void {
    this.username = user.username;
    this.firstName = user.firstName;
    this.lastName = user.lastName;
    this.email = user.email;
    this.birthdate = user.birthdate;
  }

  toString(): string {
      return '{"birthdate":"2003-01-01","email":"aaa@gmail.com","firstName":"giovis","lastName":"pisello","username":"aaa"}'
  }

  // get username(): string {
  //   return this._username;
  // }
  //
  // get firstName(): string {
  //   return this._firstName;
  // }
  //
  // get lastName(): string {
  //   return this._lastName;
  // }
  //
  // get email(): string {
  //   return this._email;
  // }
  //
  // get birthdate(): Date {
  //   return this._birthdate;
  // }
  //
  // set username(username: string) {
  //   this._username = username;
  // }
  //
  // set firstName(firstName: string) {
  //   this._firstName = firstName;
  // }
  //
  // set lastName(lastName: string) {
  //   this._lastName = lastName;
  // }
  //
  // set email(email: string) {
  //   this._email = email;
  // }
  //
  // set birthdate(birthdate: Date) {
  //   this._birthdate = birthdate;
  // }

}
