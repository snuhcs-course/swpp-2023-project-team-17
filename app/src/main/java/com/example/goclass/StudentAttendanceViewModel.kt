package com.example.goclass

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.goclass.dataClass.AttendancesResponse
import java.lang.Exception

class StudentAttendanceViewModel(
    private val repository: Repository,
) : ViewModel() {

    private var studentAttendanceList: List<AttendancesResponse> = listOf<AttendancesResponse>()

    suspend fun getStudentAttendanceList(classId: Int, userId: Int): List<AttendancesResponse> {
        try {
            val response = repository.classGetAttendanceListByUserId(classId, userId)
            if (response.code == 200) {
                studentAttendanceList = response.attendanceList
            }
        } catch (e: Exception) {
            Log.d("studentAttendanceListError", e.message.toString())
        }
        return studentAttendanceList
    }
}
