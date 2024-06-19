package com.example.jokiandroid.model

data class Game(
    var id: Int,
    var title: String,
    var price: Double,
    var description: String,
    var image: String = ""
)
