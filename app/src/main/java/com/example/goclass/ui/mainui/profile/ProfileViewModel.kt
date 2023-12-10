/*
 * ProfileViewModel is a ViewModel class responsible for handling the logic related to user profile operations.
 *
 * @property repository: UserRepository, the repository for accessing user-related data.
 * @property _editSuccess: MutableLiveData<Boolean>, indicates whether the user profile edit operation was successful.
 * @property _isLoading: MutableLiveData<Boolean>, indicates whether there is an ongoing data loading operation.
 * @property _userName: MutableLiveData<String>, holds the username of the user.
 *
 * userEdit: Initiates a coroutine to edit the user's profile with the provided details.
 * userGet: Initiates a coroutine to retrieve the user's data, specifically the username.
 */

package com.example.goclass.ui.mainui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Users
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _editSuccess = MutableLiveData<Boolean>()
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val _userName: MutableLiveData<String> = MutableLiveData()

    val editSuccess: LiveData<Boolean> get() = _editSuccess
    val isLoading: LiveData<Boolean> get() = _isLoading
    val userName: LiveData<String> get() = _userName

    /*
     * userEdit initiates a coroutine to edit the user's profile with the provided details.
     *
     * @param userId: Int, the unique identifier of the user.
     * @param userType: Int, the type of the user (e.g., student or professor).
     * @param userName: String, the desired username for the user.
     */
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

    /*
     * userGet initiates a coroutine to retrieve the user's data, specifically the username.
     *
     * @param userId: Int, the unique identifier of the user.
     */
    fun userGet(userId: Int) {
        viewModelScope.launch {
            val result = repository.userGet(userId)
            _userName.postValue(result.userName ?: "")
        }
    }
}
