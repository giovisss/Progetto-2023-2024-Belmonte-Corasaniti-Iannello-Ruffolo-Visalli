import { Component } from '@angular/core';
import { Game } from "../../model/game";
import { ProductsService } from "../../services/products.service";
import {BASE_IMAGE_URL} from "../../global";

@Component({
  selector: 'app-edit-game',
  templateUrl: './edit-game.component.html',
  styleUrl: './edit-game.component.css'
})
export class EditGameComponent {
  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
  protected selectedGame: Game | null = null;
  protected games: Game[] = [];
  protected searchedGames: Game[] = [];
  protected tempGame: Partial<Game> = {}; // Temporary object

  constructor(private productsService: ProductsService) {
    this.productsService.getGamesList().subscribe((response: any) => {
      this.games = response._embedded.modelList.map((model: any) => model.model);
    });
  }

  protected OnInput(event: any) {
    console.log(event.target.value);

    if (event.target.value === '') {
        this.searchedGames = [];
        return;
    }

    this.searchedGames = [];
    for (let game of this.games) {
      if (game.title.toLocaleLowerCase().includes(event.target.value.toLocaleLowerCase())) {
        this.searchedGames.push(game);
      }
    }
  }

  protected onGameClick(game: Game) {
    this.selectedGame = game;
    this.tempGame = { ...game }; // Copy selected game to tempGame
    this.searchedGames = [];
  }

  protected saveChanges() {
    if (this.selectedGame) {
      Object.assign(this.selectedGame, this.tempGame); // Apply changes to selectedGame
    }
  }
}