package com.example.jokiandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jokiandroid.model.Game

/*// Recupera giochi dal server
    val call = RetrofitClient.apiService.getGames()
    call.enqueue(object : Callback<List<Game>> {
        override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            } else {
                callback(emptyList())
            }
        }

        override fun onFailure(call: Call<List<Game>>, t: Throwable) {
            callback(emptyList())
        }
})*/

class GameViewModel {
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    init {
        _games.value = getFakeGames()
    }

}

fun getFakeGames(): List<Game>{
    return listOf<Game>(
        Game(1,"Valorant", 35.0, "Gioco bellissimo"),
        Game(2,"Rainbow six", 35.0, "Best gioco ever"),
        Game(3,"XDefiant", 35.0, "Gioco"),
        Game(4,"XDefiant", 35.0, "Gioco"),
        Game(5,"XDefiant", 35.0, "Gioco"),
        Game(6,"XDefiant", 35.0, "Gioco"),
        Game(7,"XDefiant", 35.0, "Gioco"),
        Game(8,"XDefiant", 35.0,"Gioco"),
        Game(9,"XDefiant", 35.0,"Gioco"),
        Game(10,"XDefiant", 35.0,"Gioco"),
        Game(11,"XDefiant", 35.0,"Gioco"),
        Game(12,"XDefiant", 35.0,"Gioco"),
        Game(13,"XDefiant", 35.0,"Gioco")
    )
}