package com.example.goclass.ui.classui.attendances

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceDetailViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()

    private val _attendanceLiveData: MutableLiveData<AttendancesResponse> = MutableLiveData()

    val toastMessage: LiveData<String> get() = _toastMessage
    val attendanceLiveData: LiveData<AttendancesResponse> get() = _attendanceLiveData

    fun getAttendance(
        attendanceId: Int,
    ): MutableLiveData<AttendancesResponse> {
        viewModelScope.launch {
            try {
                val response = repository.attendanceGet(attendanceId)
                if (response.code == 200) {
                    _attendanceLiveData.postValue(response)
                    _toastMessage.postValue("Successfully get")
                } else {
                    _toastMessage.postValue("Error: Database error")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return _attendanceLiveData
    }
}
