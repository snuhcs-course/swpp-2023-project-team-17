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
    private val _studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()

    val studentAttendanceListLiveData: LiveData<List<AttendancesResponse>> get() = _studentAttendanceListLiveData
    val toastMessage: LiveData<String> get() = _toastMessage

    fun getStudentAttendanceList(
        classId: Int,
        userId: Int,
    ): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = classRepository.classGetAttendanceListByUserId(classId, userId)
                if (response.code == 200) {
                    _studentAttendanceListLiveData.postValue(response.attendanceList)
                }
            } catch (e: Exception) {
                Log.d("studentAttendanceListError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return _studentAttendanceListLiveData
    }

    fun addAttendance(
        classId: Int,
        userId: Int,
    ) {
        viewModelScope.launch {
            try {
                val attendanceDetail = "0111111111111111"
                val attendance =
                    Attendances(
                        2,
                        30,
                        classId,
                        attendanceDetail,
                    )
                val response = attendanceRepository.attendanceAdd(userId, attendance)
                if (response.code == 200) {
                    getStudentAttendanceList(classId, userId)
                    _toastMessage.postValue("Successfully added")
                } else {
                    _toastMessage.postValue("Failed to add")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
    }
}
