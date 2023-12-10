/*
 * AttendanceServiceViewModel is a ViewModel class responsible for handling attendance-related data and interactions in the GoClass app.
 * It communicates with the AttendanceRepository to save attendance information.
 *
 * @param repository: AttendanceRepository for interacting with the data source.
 * @param _addSuccess: MutableLiveData to observe the success state of attendance addition.
 * @param addSuccess: LiveData to expose the success state of attendance addition to external observers.
 */

package com.example.goclass.ui.classui.attendances.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceServiceViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    // MutableLiveData to observe the success state of attendance addition.
    private val _addSuccess = MutableLiveData<Boolean>()

    // LiveData to expose the success state of attendance addition to external observers.
    val addSuccess: LiveData<Boolean> get() = _addSuccess

    // Save attendance information using the provided data.
    fun saveAttendance(
        attendanceStatus: Int,
        attendanceDuration: Int,
        userId: Int,
        classId: Int,
        scanResults: List<String>
    ) {
        // Convert scan results to a concatenated string.
        val attendanceDetail = scanResults.joinToString("")
        val attendances = Attendances(attendanceStatus, attendanceDuration, classId, attendanceDetail)

        viewModelScope.launch {
            try {
                // Attempt to save attendance information through the repository.
                val response = repository.attendanceAdd(userId, attendances)
                Log.d("AttendanceSaveSuccess", "$attendanceStatus, $attendanceDuration")

                // Update the MutableLiveData based on the response.
                _addSuccess.postValue(response.code == 200)
            } catch (e: Exception) {
                // Log and update the MutableLiveData in case of an error.
                Log.e("AttendanceSaveError", e.message.toString())
                _addSuccess.postValue(false)
            }
        }
    }
}
