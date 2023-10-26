package com.example.goclass.mainUi

import androidx.lifecycle.ViewModel
import com.example.goclass.Repository
import com.example.goclass.dataClass.Users

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    fun userEdit(userId: Int, users: Users){
        val result = repository.userEdit(userId, users)
    }
}