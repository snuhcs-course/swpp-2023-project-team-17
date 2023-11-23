package com.example.goclass.ui.mainui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _userId = MutableLiveData<Int?>()
    private val userId: LiveData<Int?> get() = _userId

    fun userLogin(userEmail: String) {
        viewModelScope.launch {
            try {
                val response = repository.userLogin(userEmail)
                if (response.code == 200) {
                    _userId.postValue(response.userId)
                } else {
                    _userId.postValue(null)
                }
            } catch (e: Exception) {
                _userId.postValue(null)
            }
        }
    }

    fun accessUserId(): LiveData<Int?> {
        return userId
    }
}
