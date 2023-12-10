/*
 * StudentAttendanceViewModel is a ViewModel responsible for handling business logic related to student attendance.
 * It communicates with the ClassRepository and AttendanceRepository to retrieve and add student attendance information.
 *
 * @classRepository: Repository for class-related data and operations.
 * @attendanceRepository: Repository for attendance-related data and operations.
 * @_studentAttendanceListLiveData: LiveData containing a list of student attendance items for a specific class.
 * @_toastMessage: LiveData containing a message to be displayed as a toast.
 *
 * getStudentAttendanceList: Retrieves the list of student attendance items for a specific class and user.
 * addAttendance: Adds a new attendance record for the current user in the specified class.
 */

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
    private val _studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> =
        MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()

    val studentAttendanceListLiveData: LiveData<List<AttendancesResponse>> get() = _studentAttendanceListLiveData
    val toastMessage: LiveData<String> get() = _toastMessage

    /*
     * Retrieves the list of student attendance items for a specific class and user.
     *
     * @classId: ID of the class for which attendance information is requested.
     * @userId: ID of the user for whom the attendance information is being retrieved.
     *
     * Returns a MutableLiveData containing the list of student attendance items.
     */
    fun getStudentAttendanceList(
        classId: Int,
        userId: Int,
    ): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response =
                    classRepository.classGetAttendanceListByUserId(classId, userId)
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
}
