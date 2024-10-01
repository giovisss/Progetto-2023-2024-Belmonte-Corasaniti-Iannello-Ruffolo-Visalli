import { Game } from './game';

export class admin {
  id: string;
  username: string;
  email: string;
  games: Game[];

  constructor(id: string, username: string, email: string, games: Game[]) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.games = games;
  }
}