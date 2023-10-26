package com.example.goclass.mainUi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.Repository
import com.example.goclass.dataClass.ClassListsResponse
import com.example.goclass.dataClass.Classes
import com.example.goclass.dataClass.Users
import kotlinx.coroutines.launch
import retrofit2.Call
import java.lang.Exception

class ProfessorMainViewModel(private val repository: Repository) : ViewModel() {
    val classListLiveData: MutableLiveData<List<Classes>> = MutableLiveData()
    fun createClass(
        className: String,
        classCode: String,
        professorId: Int,
        classTime: String,
        buildingNumber: String,
        roomNumber: String
    ) {
        viewModelScope.launch {
            val newClass = Classes(className, classCode, professorId, classTime, buildingNumber, roomNumber)
            repository.classCreate(newClass)
        }
    }


    fun getClassList(user: Users) {
//        viewModelScope.launch {
//            try {
//                val classList = repository.userGetClassList(user)
//                classListLiveData.postValue(classList)
//            } catch (e: Exception){
//
//            }
//        }
    }
}