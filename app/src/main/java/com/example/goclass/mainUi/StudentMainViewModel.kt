package com.example.goclass.mainUi

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.Repository
import com.example.goclass.dataClass.Classes
import kotlinx.coroutines.launch

class StudentMainViewModel(private val repository: Repository) : ViewModel() {
    fun classJoin(userId: Int, className: String, classCode: String){
        viewModelScope.launch {
            val joinClass = Classes(className, classCode)
            repository.classJoin(userId, joinClass)
        }
    }
}