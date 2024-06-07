package com.example.jokiandroid.model

data class Game(
    var id: Int = 0,
    var title: String,
    var price: Double,
    var description: String,
)

fun getFakeGames(): List<Game>{
    return listOf<Game>(
        Game(1,"Valorant", 35.0, "Fatti una doccia"),
        Game(2,"Rainbow six", 35.0, "Best gioco ever"),
        Game(3,"XDefiant", 35.0, "Gioco di merda"),
        Game(4,"XDefiant", 35.0, "Gioco di merda"),
        Game(5,"XDefiant", 35.0, "Gioco di merda"),
        Game(6,"XDefiant", 35.0, "Gioco di merda"),
        Game(7,"XDefiant", 35.0, "Gioco di merda"),
        Game(8,"XDefiant", 35.0,"Gioco di merda"),
        Game(9,"XDefiant", 35.0,"Gioco di merda"),
        Game(10,"XDefiant", 35.0,"Gioco di merda"),
        Game(11,"XDefiant", 35.0,"Gioco di merda"),
        Game(12,"XDefiant", 35.0,"Gioco di merda"),
        Game(13,"XDefiant", 35.0,"Gioco di merda")
    )
}
