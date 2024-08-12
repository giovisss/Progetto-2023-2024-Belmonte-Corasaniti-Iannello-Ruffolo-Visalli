import { game } from './game';

export class admin {
  id: string;
  username: string;
  email: string;
  games: game[];

  constructor(id: string, username: string, email: string, games: game[]) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.games = games;
  }
}