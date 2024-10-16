package com.example.jokiandroid.viewmodel

import RetrofitInstance
import TokenManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.model.User
import com.example.jokiandroid.service.UserApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object UserViewModel : ViewModel() {
    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>> get() = _usersList

    private val _selectedUser = MutableLiveData<User>()
    val selectedUser: LiveData<User> get() = _selectedUser

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private val _isAdmin = MutableLiveData<Boolean>()
    val isAdmin: LiveData<Boolean> get() = _isAdmin

    init {
        viewModelScope.launch {
            fetchCurrentUser()
        }
    }

    private suspend fun fetchCurrentUser() {
        _currentUser.value = User()
        _isAdmin.value = false

        // Wait for the token to be available
        while(TokenManager.getToken() == null) {
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
            }
        }

        val token = TokenManager.getToken()
        if (token != null) {

            val response = RetrofitInstance.createApi(UserApiService::class.java, token).isAdmin()
            if (response.isSuccessful) {
                _isAdmin.value = true
                _currentUser.value!!.id = response.body()!!.id
                _currentUser.value!!.username = response.body()!!.username
                _currentUser.value!!.email = response.body()!!.email

                fetchAllUsers()

                return
            }

            val userResponse = RetrofitInstance.createApi(UserApiService::class.java, token).getCurrentUser()
            if (userResponse.isSuccessful) {
                _currentUser.value = userResponse.body()?.let { User(it) }
                _isAdmin.value = false
            }
        }

    }

    private suspend fun fetchAllUsers(){
        try {
            val token = TokenManager.getToken()
            if (token != null) {
                val response =
                    RetrofitInstance.createApi(UserApiService::class.java, token).getUsers()
                if (response.isSuccessful) {
                    val out = mutableListOf<User>()

                    for (user in response.body()!!) {
                        out.add(User(user))
                    }

                    _usersList.value = out
                } else {
                    _usersList.value = emptyList()
                    Log.e("UserViewModel", "Error fetching users: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error: $e")
            _usersList.value = emptyList()
        }
    }

    fun fetchUserByUsername(username: String) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken()
                val response = RetrofitInstance.createApi(UserApiService::class.java,token).getUserByUsername(username)
                if (response.isSuccessful) {
                    _selectedUser.value = response.body()?.let { User(it) }
                } else {
                    Log.e("UserViewModel", "Error fetching user by id: ${response.code()}")
                    _selectedUser.value = User()
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error fetching game by id: $e")
                _selectedUser.value = User()
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken()
                val response = RetrofitInstance.createApi(UserApiService::class.java,token).updateUser(user.username, user)
                if (response.isSuccessful) {
                    _selectedUser.value = response.body()?.let { User(it) }
                } else {
                    Log.e("UserViewModel", "Error updating user: ${response.code()}")
                    _selectedUser.value = User()
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error updating user: $e")
                _selectedUser.value = User()
            } finally {
                fetchAllUsers()
            }
        }
    }
}