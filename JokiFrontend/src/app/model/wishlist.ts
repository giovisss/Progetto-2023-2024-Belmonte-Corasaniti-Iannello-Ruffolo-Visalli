import {Game} from "./game";

enum wishlistVisibility {
    PRIVATA = 0,
    AMICI = 1,
    PUBBLICA = 2
}

export class Wishlist {
  wishlistName: string;
  games: Game[] = [];
  visibility: wishlistVisibility = wishlistVisibility.PRIVATA;

  constructor(name: string, wishListProducts: Game[], visibility: wishlistVisibility) {
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

  getVisibilityName(value: number): string {
    return Object.keys(wishlistVisibility).find(key => wishlistVisibility[key as keyof typeof wishlistVisibility] === value) || '';
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
