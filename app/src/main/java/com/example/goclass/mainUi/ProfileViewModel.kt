package com.example.goclass.mainUi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.Repository
import com.example.goclass.dataClass.Users
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    fun userEdit(userId: Int, userType: Int, userName: String){
        val editProfile = Users(userType, userName)
        viewModelScope.launch{
            try{
                val response = repository.userEdit(userId, editProfile)
            } catch (e: Exception){
                Log.e("UserEditError", "Failed to edit user profile", e)
            }
        }
    }
}