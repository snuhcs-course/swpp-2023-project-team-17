package com.example.goclass.mainUi

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.goclass.Repository
import com.example.goclass.dataClass.ClassListsResponse
import com.example.goclass.dataClass.Classes
import com.example.goclass.dataClass.Users
import retrofit2.Call

class ProfessorMainViewModel(private val repository: Repository) : ViewModel() {
    fun createClass(className: String, classCode: String, professorId: Int, classTime: String, buildingNumber: String, roomNumber: String){
        val newClass = Classes(className, classCode, professorId, classTime, buildingNumber, roomNumber)
        repository.classCreate(newClass)
    }

    fun getClassList(user: Users) {
        repository.userGetClassList(user)
    }
}