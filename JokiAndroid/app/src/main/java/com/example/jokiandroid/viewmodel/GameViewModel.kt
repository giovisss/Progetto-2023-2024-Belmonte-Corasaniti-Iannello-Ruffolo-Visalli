import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class GameViewModel : ViewModel() {
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    private val _gameDetails = MutableLiveData<Game?>()
    val gameDetails: LiveData<Game?> get() = _gameDetails

    private val _libraryGames = MutableLiveData<Response<List<Game>>>()
    val libraryGames: LiveData<Response<List<Game>>> get() = _libraryGames

    // LiveData per gestire gli errori
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

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
                if (response.isSuccessful) {
                    val collectionModel = response.body()
                    val games = collectionModel?._embedded?.modelList?.map { it.model } ?: emptyList()
                    _games.value = games
                } else {
                    _error.value = "Errore durante il caricamento dei giochi: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Errore durante il caricamento dei giochi: ${e.message}"
            }
        }
    }

    fun fetchGameById(gameId: String) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken()
                val response = RetrofitInstance.createApi(token?: "").getGameById(gameId)
                if (response.isSuccessful) {
                    val entityModel = response.body()
                    _gameDetails.value = entityModel?.model
                } else {
                    _error.value = "Errore durante il caricamento del gioco: ${response.code()}"
                    _gameDetails.value = null
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error fetching game by id", e)
                _error.value = "Errore durante il caricamento dei dettagli del gioco: ${e.message}"
                _gameDetails.value = null
            }
        }
    }

    fun fetchGamesByUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = TokenManager.getToken()
                if (token != null) {
                    _libraryGames.value = RetrofitInstance.createApi(token).getGamesByUser()
                } else {
                    _error.value = "Effettua il login per visualizzare i giochi"
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
            _isLoading.value = false
        }
    }

}
