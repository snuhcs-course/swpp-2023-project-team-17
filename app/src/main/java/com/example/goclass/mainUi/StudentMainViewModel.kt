package com.example.goclass.mainUi

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.Repository
import com.example.goclass.dataClass.Classes
import kotlinx.coroutines.launch
import java.lang.Exception

class StudentMainViewModel(private val repository: Repository) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    val classListLiveData: MutableLiveData<List<Classes>> = MutableLiveData()

    fun classJoin(userId: Int, className: String, classCode: String){
        viewModelScope.launch {
            val joinClass = Classes(className, classCode)
            try {
                val response = repository.classJoin(userId, joinClass)
                if(response.code == 200){
                    _toastMessage.postValue("Successfully joined!")
                } else {
                    _toastMessage.postValue("join failed")
                }
            } catch (e: Exception){
                _toastMessage.postValue("Error: $(e.message}")
            }
        }
    }

    fun getClassList(user: Map<String, String>): MutableLiveData<List<Classes>> {
        viewModelScope.launch {
            try {
                val response = repository.userGetClassList(user)
                if(response.code == 200){
                    classListLiveData.postValue(response.classList)
                }
            } catch (e: Exception) {
                Log.d("classListError", e.message.toString())
            }
        }
        return classListLiveData
    }
}