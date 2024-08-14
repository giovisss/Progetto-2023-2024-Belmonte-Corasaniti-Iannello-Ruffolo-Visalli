package com.example.jokiandroid.model

data class Game(
    var id: String,
    var title: String,
    var price: Double,
    var description: String,
    var imagePath: String = "",
    var stock: Int,
    var genre: String,
    var developer: String
)
