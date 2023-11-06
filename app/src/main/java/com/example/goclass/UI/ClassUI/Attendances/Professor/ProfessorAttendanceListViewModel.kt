package com.example.goclass.UI.ClassUI.Attendances.Professor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.Network.DataClass.AttendancesResponse
import com.example.goclass.Repository.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfessorAttendanceListViewModel(
    private val repository: Repository,
) : ViewModel() {
    private var studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()

    fun getStudentAttendanceList(
        date: String,
        user: Map<String, String>,
    ): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.userGetAttendanceListByDate(date, user)
                if (response.code == 200) {
                    studentAttendanceListLiveData.postValue(response.attendanceList)
                }
            } catch (e: Exception) {
                Log.d("professorStudentAttendanceListError", e.message.toString())
            }
        }
        return studentAttendanceListLiveData
    }
}
