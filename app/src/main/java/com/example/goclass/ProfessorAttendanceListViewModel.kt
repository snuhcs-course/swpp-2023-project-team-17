package com.example.goclass

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.goclass.dataClass.AttendancesResponse
import java.lang.Exception

class ProfessorAttendanceListViewModel(
    private val repository: Repository,
) : ViewModel() {

    private var studentAttendanceList: List<AttendancesResponse> = listOf<AttendancesResponse>()

    suspend fun getStudentAttendanceList(date: String, user: Map<String, String>): List<AttendancesResponse> {
        try {
            val response = repository.userGetAttendanceListByDate(date, user)
            if (response.code == 200) {
                studentAttendanceList = response.attendanceList
            }
        } catch (e: Exception) {
            Log.d("professorStudentAttendanceListError", e.message.toString())
        }
        return studentAttendanceList
    }
}
