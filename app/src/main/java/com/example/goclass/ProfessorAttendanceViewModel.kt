package com.example.goclass

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.goclass.dataClass.AttendancesResponse
import java.lang.Exception

class ProfessorAttendanceViewModel(
    private val repository: Repository,
) : ViewModel() {

    private var professorAttendanceList: List<AttendancesResponse> = listOf<AttendancesResponse>()

    suspend fun getProfessorAttendanceList(user: Map<String, String>): List<AttendancesResponse> {
        try {
            val response = repository.attendanceGetDateList(user)
            if (response.code == 200) {
                professorAttendanceList = response.attendanceDateList
            }
        } catch (e: Exception) {
            Log.d("professorAttendanceListError", e.message.toString())
        }
        return professorAttendanceList
    }
}
