import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Game } from "../../model/game";
import { ProductsService } from "../../services/products.service";
import { BASE_IMAGE_URL } from "../../global";
import { DatePipe } from "@angular/common";
import {HttpResponse} from "@angular/common/http";
import { Location } from '@angular/common';

@Component({
  selector: 'app-edit-game',
  templateUrl: './edit-game.component.html',
  styleUrls: ['./edit-game.component.css'],
  providers: [DatePipe]
})
export class EditGameComponent implements OnInit, OnChanges {
  protected readonly Game = Game;
  protected readonly BASE_IMAGE_URL = BASE_IMAGE_URL;

  protected selectedGame: Game | null = null;
  protected games: Game[] = [];
  protected searchedGames: Game[] = [];
  protected tempGame: Partial<Game> = {}; // Temporary object

  isEditing: boolean = true;
  formattedReleaseDate: string | null = '';

  url: string = 'assets/upload.jpg';
  url1: string = this.url;
  url2: string = this.url;
  url3: string = this.url;
  photo1: File | null = null;
  photo2: File | null = null;
  photo3: File | null = null;

  constructor(private productsService: ProductsService, private datePipe: DatePipe, private location: Location) {
    this.productsService.getGamesList().subscribe((response: Game[]) => {
      this.games = response;
    });
  }

  ngOnInit() {
    this.formatReleaseDate();
  }

  ngOnChanges(changes: SimpleChanges) {
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
    this.isEditing = true;
    this.selectedGame = game;
    this.url1 = BASE_IMAGE_URL + game.url1;
    this.url2 = BASE_IMAGE_URL + game.url2;
    this.url3 = BASE_IMAGE_URL + game.url3;
    this.tempGame = { ...game }; // Copy selected game to tempGame
    this.formatReleaseDate(); // Format the release date when a game is selected
    this.searchedGames = [];
  }

  protected updateReleaseDate(event: any) {
    if (this.tempGame) {
      this.tempGame.releaseDate = event.target.value;
    }
  }

  newGame() {
    this.isEditing = false;
    this.selectedGame = new Game(null, 'x', 'x', 0, 'x', 'x', 'x', 'x', new Date(), 0, null);
    this.url1 = this.url;
    this.url2 = this.url;
    this.url3 = this.url;
    this.tempGame = { ...this.selectedGame }; // Copy selected game to tempGame
    this.formatReleaseDate(); // Format the release date when a game is selected
    this.searchedGames = [];
  }

  protected saveChanges() {
    if (this.isEditing) {
      this.productsService.updateGame(this.tempGame as Game).subscribe((response: HttpResponse<string>) => {
        if(response.ok) {
          this.updatePhoto();
        } else { alert('Failed to update game'); }
      });
    } else {
      if (this.selectedGame) {
        Object.assign(this.selectedGame, this.tempGame); // Apply changes to selectedGame
        this.productsService.addGame(this.selectedGame).subscribe((response: Game) => {
          if (response == null) {
            alert('Failed to add game');
            return
          }
          this.productsService.getGame(response.id!).subscribe((game: Game) => {
            if(game==null) {
                alert('Failed to load back game, reload the page');
                return
            }

            this.games.push(game);
            this.selectedGame = game;

            this.isEditing = true;

            this.updatePhoto();
          });
        });
      }
    }
  }

  updatePhoto(){
    if (!this.selectedGame) return;

    let wait = 0;

    if (this.photo1) {
      wait++;
      this.productsService.uploadPhoto(this.selectedGame.id as string, 1, this.photo1).subscribe((response: HttpResponse<string>) => {
        if (!response.ok) alert('Failed to upload photo n.1');
        wait--;
      });
    }

    if (this.photo2) {
      wait++;
      this.productsService.uploadPhoto(this.selectedGame.id as string, 2, this.photo2).subscribe((response: HttpResponse<string>) => {
        if (!response.ok) alert('Failed to upload photo n.2');
        wait--;
      });
    }

    if (this.photo3) {
      wait++;
      this.productsService.uploadPhoto(this.selectedGame.id as string, 3, this.photo3).subscribe((response: HttpResponse<string>) => {
        if (!response.ok) alert('Failed to upload photo n.3');
        wait--;
      });
    }

    let interval = setInterval(() => {
      if (wait == 0) {
        clearInterval(interval);
        this.resetForm();
        this.reloadPage();
      }
    }, 100);
  }

  protected reloadPage() {
    this.location.go(this.location.path());
    window.location.reload();
  }

  deletePhoto(index: number) {
    if(!this.isEditing) {
      switch (index) {
        case 1:
          this.url1 = this.url;
          this.photo1 = null;
          break;
        case 2:
          this.url2 = this.url;
          this.photo2 = null;
          break;
        case 3:
          this.url3 = this.url;
          this.photo3 = null;
          break;
        default:
          break;
      }
    }
    else {
      this.productsService.deletePhoto(this.selectedGame?.id as string, index).subscribe((response: HttpResponse<string>) => {
        if (response.ok) {
          switch (index) {
            case 1:
              this.url1 = this.url;
              this.photo1 = null;
              break;
            case 2:
              this.url2 = this.url;
              this.photo2 = null;
              break;
            case 3:
              this.url3 = this.url;
              this.photo3 = null;
              break;
            default:
              break;
          }
        } else { alert('Failed to delete photo'); }
      });
    }
  }

  resetForm() {
    this.selectedGame = null;
    this.tempGame = {};
    this.formattedReleaseDate = '';
    this.url1 = this.url;
    this.url2 = this.url;
    this.url3 = this.url;
    this.searchedGames = [];
  }

  protected onFileSelected(event: any, index: number) {
    if (event.target.files) {
      var reader = new FileReader();
      const file = event.target.files[0];
      reader.readAsDataURL(file);

      reader.onload = (e: any) => {
        switch (index) {
          case 1:
            this.url1 = e.target.result;
            this.photo1 = file;
            break;
          case 2:
            this.url2 = e.target.result;
            this.photo2 = file;
            break;
          case 3:
            this.url3 = e.target.result;
            this.photo3 = file;
            break;
          default:
            break;
        }
      }
    }
  }

  deleteGame() {
    if (this.selectedGame) {
      this.productsService.deleteGame(this.selectedGame.id as string).subscribe((response: HttpResponse<string>) => {
        if(response.ok) {
          this.resetForm();
          this.reloadPage();
        } else { alert('Failed to delete game'); }
      });
    }
  }

}