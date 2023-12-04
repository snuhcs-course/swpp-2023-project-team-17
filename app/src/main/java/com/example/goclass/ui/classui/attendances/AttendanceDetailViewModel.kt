package com.example.goclass.ui.classui.attendances

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch

class AttendanceDetailViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    private val _attendanceDetailListLiveData: MutableLiveData<List<String>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()

    val attendanceDetailListLiveDate: LiveData<List<String>> get() = _attendanceDetailListLiveData
    val toastMessage: LiveData<String> get() = _toastMessage

    fun getAttendanceDetail(
        attendanceId: Int,
    ): MutableLiveData<List<String>> {
        viewModelScope.launch {
            try {
                val response = repository.attendanceDetailListGet(attendanceId)
                if (response.code == 200) {
                    _attendanceDetailListLiveData.postValue(response.attendanceDetailList)
                    _toastMessage.postValue("Successfully get")
                } else if (response.code == 500) {
                    _toastMessage.postValue("Error: Database error")
                } else {
                    _toastMessage.postValue("Failed to get")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return _attendanceDetailListLiveData
    }
}
