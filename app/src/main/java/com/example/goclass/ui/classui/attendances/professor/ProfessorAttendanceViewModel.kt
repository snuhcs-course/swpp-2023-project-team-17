package com.example.goclass.ui.classui.attendances.professor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfessorAttendanceViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    val professorAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()

    fun getProfessorAttendanceList(classMap: Map<String, String>): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.attendanceGetDateList(classMap)
                if (response.code == 200) {
                    professorAttendanceListLiveData.postValue(response.attendanceDateList)
                }
            } catch (e: Exception) {
                Log.d("professorAttendanceListError", e.message.toString())
            }
        }
        return professorAttendanceListLiveData
    }
}
