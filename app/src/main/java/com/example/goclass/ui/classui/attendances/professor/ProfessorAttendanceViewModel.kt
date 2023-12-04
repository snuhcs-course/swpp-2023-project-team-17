package com.example.goclass.ui.classui.attendances.professor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch

class ProfessorAttendanceViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _professorAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()

    val professorAttendanceListLiveData: LiveData<List<AttendancesResponse>> get() = _professorAttendanceListLiveData
    val toastMessage: LiveData<String> get() = _toastMessage

    fun getProfessorAttendanceList(classMap: Map<String, String>): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.attendanceGetDateList(classMap)
                if (response.code == 200) {
                    _professorAttendanceListLiveData.postValue(response.attendanceDateList)
                }
            } catch (e: Exception) {
                Log.d("professorAttendanceListError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return _professorAttendanceListLiveData
    }
}
