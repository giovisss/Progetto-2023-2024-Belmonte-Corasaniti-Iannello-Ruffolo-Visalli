import {User} from "./user";
import {Game} from "./game";

export class Review {
  private gameId: string;
  private review: string;
  private suggested: boolean;
  private username: string | undefined;

  constructor(gameId: string, review: string, suggested: boolean, username: string | undefined) {
    this.gameId = gameId;
    this.review = review;
    this.suggested = suggested;
    this.username = username;
  }

  public getUsername(): string {
    return <string>this.username;
  }

  public setUsername(username: string): void {
    this.username = username;
  }

  public getGameId(): string {
    return this.gameId;
  }

  public setGameId(gameId: string): void {
    this.gameId = gameId;
  }

  public getReview(): string {
    return this.review;
  }

  public setReview(review: string): void {
    this.review = review;
  }

  public getSuggested(): boolean {
    return this.suggested;
  }

  public setSuggested(suggested: boolean): void {
    this.suggested = suggested;
  }
}
