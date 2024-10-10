package com.example.jokiandroid.viewmodel

import RetrofitInstance
import TokenManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokiandroid.model.User
import com.example.jokiandroid.service.UserApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CurrentUserViewModel : ViewModel() {
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
                Thread.sleep(5000)
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

                return
            }

            val userResponse = RetrofitInstance.createApi(UserApiService::class.java, token).getUser()
            if (userResponse.isSuccessful) {
                _currentUser.value = userResponse.body()?.let { User(it) }
                _isAdmin.value = false
            }
        }

    }
}