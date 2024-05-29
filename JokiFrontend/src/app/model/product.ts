export class Product {
  id: number;
  name: string;
  url: string;
  price: number;
  description: string;


  constructor(id: number, name: string, url: string, price: number, description: string) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.price = price;
    this.description = description;
  }
}
