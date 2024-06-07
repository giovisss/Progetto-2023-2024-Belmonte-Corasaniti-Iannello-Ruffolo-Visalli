package com.example.jokiandroid.model

class Cart(list: List<Game> = listOf()) {
    var games: MutableList<Game> = list.toMutableList()
        private set

    val totalPrice: Double
        get() = games.sumOf { it.price }

    val count: Int
        get() = games.size

    fun add(game: Game) {
        games.add(game)
    }

    fun remove(game: Game) {
        games.remove(game)
    }
}
