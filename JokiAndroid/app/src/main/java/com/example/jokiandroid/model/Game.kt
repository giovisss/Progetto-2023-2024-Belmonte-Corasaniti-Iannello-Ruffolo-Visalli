package com.example.jokiandroid.model

class Game {
    var id: String
    var title: String
    var price: Double
    var description: String
    var imagePath: String
    var stock: Int
    var genre: String
    var developer: String

    var url1: String
    var url2: String
    var url3: String

    constructor(){
        this.id = "0"
        this.title = "title"
        this.price = 0.0
        this.description = "description"
        this.imagePath = "imagePath"
        this.stock = 0
        this.genre = "genre"
        this.developer = "developer"
        this.url1 = imagePath.replace(".jpg", "_1.jpg")
        this.url2 = imagePath.replace(".jpg", "_2.jpg")
        this.url3 = imagePath.replace(".jpg", "_3.jpg")
    }

    constructor(game: Game) {
        this.id = game.id
        this.title = game.title
        this.price = game.price
        this.description = game.description
        this.imagePath = game.imagePath
        this.stock = game.stock
        this.genre = game.genre
        this.developer = game.developer
        this.url1 = imagePath.replace(".jpg", "_1.jpg")
        this.url2 = imagePath.replace(".jpg", "_2.jpg")
        this.url3 = imagePath.replace(".jpg", "_3.jpg")
    }

    constructor(id: String, title: String, price: Double, description: String, imagePath: String, stock: Int, genre: String, developer: String) {
        this.id = id
        this.title = title
        this.price = price
        this.description = description
        this.imagePath = imagePath
        this.stock = stock
        this.genre = genre
        this.developer = developer
        this.url1 = imagePath.replace(".jpg", "_1.jpg")
        this.url2 = imagePath.replace(".jpg", "_2.jpg")
        this.url3 = imagePath.replace(".jpg", "_3.jpg")
    }

    override fun toString(): String {
        return "Game(id=$id, title='$title', price=$price, description='$description', imagePath='$imagePath', stock=$stock, genre='$genre', developer='$developer', url1='$url1', url2='$url2', url3='$url3')"
    }
}
