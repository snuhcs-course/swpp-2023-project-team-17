package com.example.goclass.ui.classui.attendances.professor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch

class ProfessorAttendanceListViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()

    val studentAttendanceListLiveData: LiveData<List<AttendancesResponse>> get() = _studentAttendanceListLiveData
    val toastMessage: LiveData<String> get() = _toastMessage

    fun getStudentAttendanceList(
        date: String,
        classMap: Map<String, String>,
    ): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.userGetAttendanceListByDate(date, classMap)
                if (response.code == 200) {
                    _studentAttendanceListLiveData.postValue(response.attendanceList)
                    _toastMessage.postValue("Refreshed!")
                } else {
                    _toastMessage.postValue("Failed to refresh: Check network connection.")
                }
            } catch (e: Exception) {
                Log.d("professorStudentAttendanceListError", e.message.toString())
                _toastMessage.postValue("Failed to refresh: Check network connection.")
            }
        }
        return _studentAttendanceListLiveData
    }
}
