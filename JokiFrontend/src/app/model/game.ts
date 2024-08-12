import { admin } from "./admin";

export class game {
  id: string;
  title: string;
  description: string;
  price: number;
  imagePath: string;
  genre: string;
  developer: string;
  publisher: string;
  releaseDate: Date;
  stock: number;
  admin: admin;

  constructor(
    id: string,
    title: string,
    description: string,
    price: number,
    imagePath: string,
    genre: string,
    developer: string,
    publisher: string,
    releaseDate: Date,
    stock: number,
    admin: admin) {

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
  }
}