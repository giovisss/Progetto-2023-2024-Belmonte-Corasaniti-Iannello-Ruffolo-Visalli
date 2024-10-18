import {Game} from "./game";

export class Wishlist {
  wishlistName: string;
  games: Game[] = [];
  visibility: number = 0;

  constructor(name: string, wishListProducts: Game[], visibility: number) {
    this.wishlistName = name;
    wishListProducts.forEach(game => {
        this.games.push(new Game(
            game.id,
            game.title,
            game.description,
            game.price,
            game.imagePath,
            game.genre,
            game.developer,
            game.publisher,
            game.releaseDate,
            game.stock,
            game.admin
        ))
    })
    this.visibility = visibility;


  }

  toString(): string {
    return "Wishlist{" + "name='" + this.wishlistName + '\'' + ", wishListProducts=" + this.games + ", visibility=" + this.visibility + '}';
  }

  equals(o: any): boolean {
    if (this === o) return true;
    if (o == null || this.constructor !== o.constructor) return false;
    const wishlist: Wishlist = o;
    return this.visibility === wishlist.visibility && this.wishlistName === wishlist.wishlistName && this.games === wishlist.games;
  }
}
