package com.example.goclass.UI.MainUI.UserMain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.Repository.Repository
import com.example.goclass.Network.DataClass.Classes
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfessorMainViewModel(private val repository: Repository) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    val classListLiveData: MutableLiveData<List<Classes>> = MutableLiveData()

    fun createClass(
        className: String,
        classCode: String,
        professorId: Int,
        classTime: String,
        buildingNumber: String,
        roomNumber: String,
    ) {
        viewModelScope.launch {
            val newClass = Classes(
                className,
                classCode,
                professorId,
                classTime,
                buildingNumber,
                roomNumber
            )
            try {
                val response = repository.classCreate(newClass)
                if (response.code == 200) {
                    _toastMessage.postValue("Successfully created!")
                    getClassList(mapOf("userId" to professorId.toString(), "userType" to "1"))
                } else {
                    _toastMessage.postValue("create failed")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun getClassList(user: Map<String, String>): MutableLiveData<List<Classes>> {
        viewModelScope.launch {
            try {
                val response = repository.userGetClassList(user)
                if (response.code == 200) {
                    classListLiveData.postValue(response.classList)
                }
            } catch (e: Exception) {
                Log.d("classListError", e.message.toString())
            }
        }
        return classListLiveData
    }
}
