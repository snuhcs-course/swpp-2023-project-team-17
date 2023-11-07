package com.example.goclass.ui.mainui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.repository.Repository
import com.example.goclass.network.dataclass.Users
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(
    private val repository: Repository,
) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    private val _editSuccess = MutableLiveData<Boolean>()

    val toastMessage: LiveData<String> get() = _toastMessage
    val editSuccess: LiveData<Boolean> get() = _editSuccess
    val isLoading = MutableLiveData<Boolean>()
    val userName: MutableLiveData<String> = MutableLiveData()

    fun userEdit(
        userId: Int,
        userType: Int,
        userName: String,
    ) {
        val editProfile = Users(userType, userName)
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                val response = repository.userEdit(userId, editProfile)
                if (response.code == 200) {
                    _toastMessage.postValue("Success")
                    _editSuccess.postValue(true)
                } else {
                    _toastMessage.postValue("Failure")
                    _editSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
                _editSuccess.postValue(false)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun userGet(userId: Int) {
        viewModelScope.launch {
            val result = repository.userGet(userId)
            userName.postValue(result?.userName ?: "")
        }
    }
}
