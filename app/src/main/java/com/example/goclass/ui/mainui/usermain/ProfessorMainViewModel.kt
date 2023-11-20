package com.example.goclass.ui.mainui.usermain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.repository.ClassRepository
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfessorMainViewModel(
    private val userRepository: UserRepository,
    private val classRepository: ClassRepository,
) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    val classListLiveData: MutableLiveData<List<ClassesResponse>> = MutableLiveData()

    fun createClass(
        className: String,
        classCode: String,
        professorId: Int,
        classTime: String,
        buildingNumber: String,
        roomNumber: String,
    ) {
        viewModelScope.launch {
            val newClass =
                Classes(
                    className,
                    classCode,
                    professorId,
                    classTime,
                    buildingNumber,
                    roomNumber,
                )
            try {
                val response = classRepository.classCreate(newClass)
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

    fun getClassList(user: Map<String, String>): MutableLiveData<List<ClassesResponse>> {
        viewModelScope.launch {
            try {
                val response = userRepository.userGetClassList(user)
                if (response.code == 200) {
                    classListLiveData.postValue(response.classList)
                }
            } catch (e: Exception) {
                Log.d("classListError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return classListLiveData
    }

    fun deleteClass(classId: Int, professorId: Int) {
        viewModelScope.launch {
            try {
                val response = classRepository.classDelete(classId)
                if(response.code == 200) {
                    _toastMessage.postValue("Successfully deleted")
                    getClassList(mapOf("userId" to professorId.toString(), "userType" to "1"))
                } else {
                    _toastMessage.postValue("delete Failed")
                }
            } catch (e: Exception) {
                Log.d("classDeleteError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
    }
}
