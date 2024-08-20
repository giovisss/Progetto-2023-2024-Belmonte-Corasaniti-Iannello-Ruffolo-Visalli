import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.auth.TokenManager
import com.example.jokiandroid.model.Game
import kotlinx.coroutines.launch
import retrofit2.Response

class GameViewModel : ViewModel() {
    private val _games = MutableLiveData<Response<List<Game>>>()
    val games: LiveData<Response<List<Game>>> get() = _games

    private val _gameDetails = MutableLiveData<Response<Game>>()
    val gameDetails: LiveData<Response<Game>> get() = _gameDetails

    private val _libraryGames = MutableLiveData<Response<List<Game>>>()
    val libraryGames: LiveData<Response<List<Game>>> get() = _libraryGames

    // LiveData per gestire gli errori
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
//        if (TokenManager.getToken() == null) {
//            _error.value = "Token non disponibile. Effettua il login."
//        } else {
//            fetchGames()
//        }
        fetchGames()
    }

    fun refreshData() {
        fetchGames()
        fetchGamesByUser()
        // Aggiungi qui altre chiamate di fetch se necessario
    }


    private fun fetchGames() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.createApi().getGames()
                _games.value = response
            } catch (e: Exception) {
                _error.value = "Errore durante il caricamento dei giochi: ${e.message}"
            }
        }
    }

    fun fetchGameById(gameId: String) {
        viewModelScope.launch {
            try {
                _gameDetails.value = RetrofitInstance.createApi().getGameById(gameId)
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error fetching games", e)
                _error.value = "Errore durante il caricamento del gioco"
            }
//            try {
//                val token = TokenManager.getToken()
//                _gameDetails.value = RetrofitInstance.createApi(token).getGameById(gameId)
//            } catch (e: Exception) {
//                Log.e("GameViewModel", "Error fetching game by id", e)
//                _error.value = "Errore durante il caricamento dei dettagli del gioco"
//            }
        }
    }

    fun fetchGamesByUser() {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken()
                if (token != null) {
                    _libraryGames.value = RetrofitInstance.createApi(token).getGamesByUser()
                } else {
                    _error.value = "Token non disponibile. Effettua il login."
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error fetching games", e)
                _error.value = "Errore durante il caricamento dei giochi"
            }
//            try {
//                _libraryGames.value = RetrofitInstance.api.getGamesByUser()
//            } catch (e: Exception) {
//                Log.e("GameViewModel", "Error fetching games by user", e)
//                _error.value = "Errore durante il caricamento dei giochi dell'utente"
//            }
        }
    }

}
