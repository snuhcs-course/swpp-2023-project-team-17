package com.example.goclass.ui.classui.attendances.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceServiceViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    private val _addSuccess = MutableLiveData<Boolean>()
    val addSuccess: LiveData<Boolean> get() = _addSuccess

    fun saveAttendance(
        attendanceStatus: Int,
        attendanceDuration: Int,
        userId: Int,
        classId: Int,
    ) {
        val attendances = Attendances(attendanceStatus, attendanceDuration, classId)

        viewModelScope.launch {
            try {
                val response = repository.attendanceAdd(userId, attendances)
                Log.d("AttendanceSaveSuccess", "$attendanceStatus, $attendanceDuration")
                if (response.code == 200) {
                    _addSuccess.postValue(true)
                } else {
                    _addSuccess.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("AttendanceSaveError", e.message.toString())
                _addSuccess.postValue(false)
            }
        }
    }
}
