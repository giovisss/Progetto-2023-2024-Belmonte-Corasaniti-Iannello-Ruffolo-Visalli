package com.example.jokiandroid.model

data class Wishlist(
    var visibility : Int,
    var id: Int,
    var userid: Int,
    var name: String,
    var games: List<Game>
) {
    constructor(name: String, visibility: Int) : this(visibility, 0, 0, name, emptyList()) {
        this.name = name
        this.visibility = visibility
    }
}