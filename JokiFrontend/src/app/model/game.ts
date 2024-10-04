import { admin } from "./admin";

export class Game {
  id: string | null;
  title: string;
  description: string;
  price: number;
  imagePath: string;
  genre: string;
  developer: string;
  publisher: string;
  releaseDate: Date;
  stock: number;
  admin: admin | null;

  url1: string;
  url2: string;
  url3: string;

  constructor(
    id: string | null,
    title: string,
    description: string,
    price: number,
    imagePath: string,
    genre: string,
    developer: string,
    publisher: string,
    releaseDate: Date,
    stock: number,
    admin: admin | null) {

    this.id = id;
    this.title = title;
    this.description = description;
    this.price = price;
    this.imagePath = imagePath;
    this.genre = genre;
    this.developer = developer;
    this.publisher = publisher;
    this.releaseDate = releaseDate;
    this.stock = stock;
    this.admin = admin;

    this.url1 = this.imagePath.replace(".jpg", "_1.jpg");
    this.url2 = this.imagePath.replace(".jpg", "_2.jpg");
    this.url3 = this.imagePath.replace(".jpg", "_3.jpg");
  }
}