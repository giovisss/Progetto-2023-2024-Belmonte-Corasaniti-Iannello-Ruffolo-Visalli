import { Component, OnChanges, OnInit, SimpleChanges, Input } from '@angular/core';
import { Game } from "../../model/game";
import { ProductsService } from "../../services/products.service";
import { BASE_IMAGE_URL } from "../../global";
import { DatePipe } from "@angular/common";

@Component({
  selector: 'app-edit-game',
  templateUrl: './edit-game.component.html',
  styleUrls: ['./edit-game.component.css'],
  providers: [DatePipe]
})
export class EditGameComponent implements OnInit, OnChanges {
  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;
  protected selectedGame: Game | null = null;
  protected games: Game[] = [];
  protected searchedGames: Game[] = [];
  protected tempGame: Partial<Game> = {}; // Temporary object
  formattedReleaseDate: string | null = '';

  constructor(private productsService: ProductsService, private datePipe: DatePipe) {
    this.productsService.getGamesList().subscribe((response: any) => {
      this.games = response._embedded.modelList.map((model: any) => model.model);
    });
  }

  ngOnInit() {
    this.formatReleaseDate();
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log("changes");
    if (changes['tempGame'] && changes['tempGame'].currentValue['releaseDate'] !== changes['tempGame'].previousValue['releaseDate']) {
      this.formatReleaseDate();
    }
  }

  formatReleaseDate() {
    if (this.tempGame['releaseDate']) {
      this.formattedReleaseDate = this.datePipe.transform(this.tempGame['releaseDate'], 'yyyy-MM-dd');
    }
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
    this.formatReleaseDate(); // Format the release date when a game is selected
    this.searchedGames = [];
  }

  protected updateReleaseDate(event: any) {
    if (this.tempGame) {
      this.tempGame.releaseDate = event.target.value;
    }
  }

  protected saveChanges() {
    console.log(this.tempGame);
    console.log(BASE_IMAGE_URL + this.tempGame.imagePath);
    // if (this.selectedGame) {
    //   Object.assign(this.selectedGame, this.tempGame); // Apply changes to selectedGame
    // }
  }
}
