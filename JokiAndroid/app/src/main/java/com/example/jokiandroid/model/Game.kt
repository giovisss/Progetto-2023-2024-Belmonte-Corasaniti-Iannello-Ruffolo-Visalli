package com.example.jokiandroid.model

data class Game(
    var id: String,
    var title: String,
    var price: Double,
    var description: String,
    var image: String = ""
)
