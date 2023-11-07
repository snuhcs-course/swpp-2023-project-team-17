package com.example.goclass.ui.classui.attendances.student

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.ClassRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class StudentAttendanceViewModel(
    private val repository: ClassRepository,
) : ViewModel() {
    private var studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()

    fun getStudentAttendanceList(
        classId: Int,
        userId: Int,
    ): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.classGetAttendanceListByUserId(classId, userId)
                if (response.code == 200) {
                    studentAttendanceListLiveData.postValue(response.attendanceList)
                }
            } catch (e: Exception) {
                Log.d("studentAttendanceListError", e.message.toString())
            }
        }
        return studentAttendanceListLiveData
    }
}
