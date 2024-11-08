
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.model.Game
import com.example.jokiandroid.service.GameApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    private val _gameDetails = MutableLiveData<Game?>()
    val gameDetails: LiveData<Game?> get() = _gameDetails

    private val _libraryGames = MutableLiveData<List<Game>>()
    val libraryGames: LiveData<List<Game>> get() = _libraryGames

    // LiveData per gestire gli errori
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {

        fetchGames()
    }

    fun refreshData() {
        fetchGames()
        fetchGamesByUser()
    }


    private fun fetchGames() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.createApi(GameApiService::class.java).getGames()
                if (response.isSuccessful) {
                    val collectionModel = response.body()
                    val games = collectionModel?._embedded?.modelList?.map { it.model } ?: emptyList()

                    val out = mutableListOf<Game>()
                    for (game in games) {
                        out.add(Game(game))
                    }

                    _games.value = out
                } else {
                    _error.value = "Errore durante il caricamento dei giochi: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("fetchGames", "Error: ${e.message}")
                _error.value = "Errore durante il caricamento dei giochi: ${e.message}"
            }
        }
    }

    fun fetchGameById(gameId: String) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken()
                val response = RetrofitInstance.createApi(GameApiService::class.java,token).getGameById(gameId)
                if (response.isSuccessful) {
                    val entityModel = response.body()
                    _gameDetails.value = entityModel?.model?.let { Game(it) }
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
                    val response = RetrofitInstance.createApi(GameApiService::class.java,token).getGamesByUser()
                    if (response.isSuccessful) {
                        val games = response.body()?.map { it } ?: emptyList()

                        val out = mutableListOf<Game>()
                        for (game in games) {
                            out.add(Game(game))
                        }

                        _libraryGames.value = out
                    }
                    else {
                        _error.value = "Errore durante il caricamento dei giochi: ${response.code()}"
                    }
                } else {
                    _error.value = "Effettua il login per visualizzare i giochi"
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error fetching games", e)
                _error.value = "Errore durante il caricamento dei giochi"
            }
            _isLoading.value = false
        }
    }

    fun updateGame(game: Game) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.createApi(GameApiService::class.java,TokenManager.getToken()).updateGame(game.id, game)
                if (response.isSuccessful) {
                    _error.value = "Gioco aggiornato con successo"
                    fetchGames()
                } else {
                    _error.value = "Errore durante l'aggiornamento del gioco: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error updating game", e)
                _error.value = "Errore durante l'aggiornamento del gioco: ${e.message}"
            }
        }
    }

    fun deleteGame(gameId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.createApi(GameApiService::class.java,TokenManager.getToken()).deleteGame(gameId)
                if (response.isSuccessful) {
                    _error.value = "Gioco eliminato con successo"
                    fetchGames()
                } else {
                    _error.value = "Errore durante l'eliminazione del gioco: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error deleting game", e)
                _error.value = "Errore durante l'eliminazione del gioco: ${e.message}"
            }
        }
    }



}
