import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object TokenManager {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    fun setToken(newToken: String) {
        _token.value = newToken
    }

    fun clearToken() {
        _token.value = null
    }

    fun getToken(): String? = _token.value

    fun isLoggedIn(): Boolean = _token.value != null
}