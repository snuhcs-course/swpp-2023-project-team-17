/*
 * AttendanceDetailViewModel is a ViewModel class associated with the AttendanceDetailFragment.
 * It handles the business logic related to retrieving attendance details for a specific attendance record.
 *
 * @repository: The repository responsible for handling attendance-related data operations.
 * @toastMessage: LiveData to observe toast messages for UI feedback.
 * @attendanceLiveData: LiveData to observe detailed attendance information for visualization.
 *
 * getAttendance: Coroutine function to fetch attendance details for a given attendance ID.
 */

package com.example.goclass.ui.classui.attendances

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceDetailViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    // LiveData for displaying toast messages in the UI
    private val _toastMessage = MutableLiveData<String>()

    // LiveData for holding detailed attendance information
    private val _attendanceLiveData: MutableLiveData<AttendancesResponse> = MutableLiveData()

    // Public access to toast messages
    val toastMessage: LiveData<String> get() = _toastMessage

    // Public access to detailed attendance information
    val attendanceLiveData: LiveData<AttendancesResponse> get() = _attendanceLiveData

    /*
     * getAttendance is a coroutine function that retrieves attendance details for a given attendance ID.
     * It updates the _attendanceLiveData and _toastMessage LiveData accordingly based on the success or failure of the operation.
     */
    fun getAttendance(attendanceId: Int): MutableLiveData<AttendancesResponse> {
        viewModelScope.launch {
            try {
                val response = repository.attendanceGet(attendanceId)
                if (response.code == 200) {
                    _attendanceLiveData.postValue(response)
                    _toastMessage.postValue("Successfully retrieved attendance details")
                } else {
                    _toastMessage.postValue("Error: Database error")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return _attendanceLiveData
    }
}
