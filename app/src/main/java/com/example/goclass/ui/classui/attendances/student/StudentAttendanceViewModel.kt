package com.example.goclass.ui.classui.attendances.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.repository.ClassRepository
import kotlinx.coroutines.launch

class StudentAttendanceViewModel(
    private val classRepository: ClassRepository,
    private val attendanceRepository: AttendanceRepository,
) : ViewModel() {
    private val studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()
    private val toastMessage: LiveData<String> get() = _toastMessage

    fun getStudentAttendanceList(
        classId: Int,
        userId: Int,
    ): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = classRepository.classGetAttendanceListByUserId(classId, userId)
                if (response.code == 200) {
                    studentAttendanceListLiveData.postValue(response.attendanceList)
                }
            } catch (e: Exception) {
                Log.d("studentAttendanceListError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return studentAttendanceListLiveData
    }

    fun addAttendance(
        classId: Int,
        userId: Int,
    ) {
        viewModelScope.launch {
            try {
                val attendance =
                    Attendances(
                        2,
                        60,
                        classId,
                    )
                val response = attendanceRepository.attendanceAdd(userId, attendance)
                if (response.code == 200) {
                    getStudentAttendanceList(classId, userId)
                    _toastMessage.postValue("Successfully added")
                } else {
                    _toastMessage.postValue("Failed to add")
                }
            } catch (e: Exception) {
                Log.d("dummyAttendanceError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun accessStudentAttendanceListLiveData(): MutableLiveData<List<AttendancesResponse>> {
        return studentAttendanceListLiveData
    }

    fun accessToastMessage(): LiveData<String> {
        return toastMessage
    }
}
