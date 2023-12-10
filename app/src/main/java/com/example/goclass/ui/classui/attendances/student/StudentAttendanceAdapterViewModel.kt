/*
 * StudentAttendanceAdapterViewModel is a ViewModel responsible for handling business logic
 * related to student attendance data in the StudentAttendanceAdapter.
 *
 * @param repository: AttendanceRepository for handling attendance-related data operations.
 * @_editSuccess: MutableLiveData indicating the success state of attendance editing.
 * @editSuccess: LiveData to observe the success state of attendance editing.
 *
 * The ViewModel exposes a function editAttendance to perform attendance editing asynchronously.
 * It communicates with the repository to edit the attendance and updates the editSuccess LiveData accordingly.
 * In case of an exception, it logs an error message.
 */

package com.example.goclass.ui.classui.attendances.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.repository.AttendanceRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class StudentAttendanceAdapterViewModel(
    private val repository: AttendanceRepository,
) : ViewModel() {
    private val _editSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val editSuccess: LiveData<Boolean> get() = _editSuccess

    /*
     * Performs attendance editing asynchronously and updates the _editSuccess LiveData based on the response code.
     * Logs an error message in case of an exception.
     *
     * @param attendanceId: The ID of the attendance record to be edited.
     */
    fun editAttendance(attendanceId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.attendanceEdit(attendanceId)
                if (response.code == 200) {
                    _editSuccess.postValue(true)
                } else {
                    _editSuccess.postValue(false)
                }
            } catch (e: Exception) {
                Log.d("attendanceSendError", e.message.toString())
                _editSuccess.postValue(false)
            }
        }
    }
}
