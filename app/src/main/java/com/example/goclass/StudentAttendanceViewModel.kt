package com.example.goclass

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.dataClass.Attendances
import kotlinx.coroutines.launch
import java.lang.Exception

class StudentAttendanceViewModel(
    private val repository: Repository,
) : ViewModel() {

    private var studentAttendanceList: List<Attendances> = listOf<Attendances>()

    suspend fun getStudentAttendanceList(classId: Int, userId: Int): List<Attendances> {
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
