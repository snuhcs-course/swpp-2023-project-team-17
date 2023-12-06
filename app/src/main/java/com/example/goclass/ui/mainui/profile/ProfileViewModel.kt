package com.example.goclass.ui.mainui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Users
import com.example.goclass.repository.UserRepository
import com.example.goclass.utility.SnackbarBuilder
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    private val _editSuccess = MutableLiveData<Boolean>()
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val _userName: MutableLiveData<String> = MutableLiveData()

    val toastMessage: LiveData<String> get() = _toastMessage
    val editSuccess: LiveData<Boolean> get() = _editSuccess
    val isLoading: LiveData<Boolean> get() = _isLoading
    val userName: LiveData<String> get() = _userName

    fun userEdit(
        userId: Int,
        userType: Int,
        userName: String,
    ) {
        val editProfile = Users(userType, userName)
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.userEdit(userId, editProfile)
                if (response.code == 200) {
                    _editSuccess.postValue(true)
                } else {
                    _editSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _editSuccess.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun userGet(userId: Int) {
        viewModelScope.launch {
            val result = repository.userGet(userId)
            _userName.postValue(result.userName ?: "")
        }
    }
}
