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



}
