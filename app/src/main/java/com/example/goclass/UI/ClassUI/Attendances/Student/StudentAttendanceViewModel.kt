package com.example.goclass.UI.ClassUI.Attendances.Student

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.Network.DataClass.AttendancesResponse
import com.example.goclass.Repository.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

class StudentAttendanceViewModel(
    private val repository: Repository,
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
