package com.example.jokiandroid.model

import java.util.UUID

data class Wishlist(
    var visibility : Int,
    var id: UUID?,
    var userid: Int,
    var wishlistName: String,
    var games: List<Game>
) {
    constructor(name: String, visibility: Int) : this(visibility, null, 0, name, emptyList()) {
        this.wishlistName = name
        this.visibility = visibility
    }

    constructor(wishlist: Wishlist) : this(wishlist.visibility, wishlist.id, wishlist.userid, wishlist.wishlistName, wishlist.games) {
        this.visibility = wishlist.visibility
        this.id = wishlist.id
        this.userid = wishlist.userid
        this.wishlistName = wishlist.wishlistName
        this.games = wishlist.games
    }
}