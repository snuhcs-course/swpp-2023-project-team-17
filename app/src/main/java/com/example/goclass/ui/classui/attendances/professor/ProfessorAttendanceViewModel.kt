package com.example.goclass.ui.classui.attendances.professor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfessorAttendanceViewModel(
    private val repository: Repository,
) : ViewModel() {
    private var professorAttendanceListLiveDate: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()

    fun getProfessorAttendanceList(user: Map<String, String>): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.attendanceGetDateList(user)
                if (response.code == 200) {
                    professorAttendanceListLiveDate.postValue(response.attendanceDateList)
                }
            } catch (e: Exception) {
                Log.d("professorAttendanceListError", e.message.toString())
            }
        }
        return professorAttendanceListLiveDate
    }
}
