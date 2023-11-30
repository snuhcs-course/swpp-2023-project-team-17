package com.example.goclass.ui.classui.attendances.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class StudentAttendanceAdapterViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    private val _editSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val editSuccess: LiveData<Boolean> get() = _editSuccess

    fun editAttendance(attendanceId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.attendanceEdit(attendanceId)
                if (response.code == 200) {
                    _editSuccess.postValue(true)
                } else {
                    _editSuccess.postValue(false)
                }
            } catch (e: Exception) {
                Log.d("attendanceSendError", e.message.toString())
                _editSuccess.postValue(false)
            }
        }
    }
}
