/*
 * ProfessorAttendanceListViewModel is a ViewModel responsible for managing the data and business logic
 * related to the professor's attendance list in the UI.
 *
 * @property repository: UserRepository, the repository responsible for handling user-related data operations.
 * @property _studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>>, holds the LiveData for student attendance data.
 * @property _toastMessage: MutableLiveData<String>, holds the LiveData for displaying toast messages in the UI.
 *
 * getStudentAttendanceList: Fetches the student attendance list for a specific date and class, updating the LiveData accordingly.
 */

package com.example.goclass.ui.classui.attendances.professor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch

class ProfessorAttendanceListViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _studentAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()

    // studentAttendanceListLiveData: Exposes the LiveData for student attendance data to observers.
    val studentAttendanceListLiveData: LiveData<List<AttendancesResponse>> get() = _studentAttendanceListLiveData
    // toastMessage: Exposes the LiveData for toast messages to observers.
    val toastMessage: LiveData<String> get() = _toastMessage

    /*
     * getStudentAttendanceList: Fetches the student attendance list for a specific date and class,
     * updating the LiveData accordingly. Handles network and repository errors and notifies the UI
     * with appropriate toast messages.
     */
    fun getStudentAttendanceList(
        date: String,
        classMap: Map<String, String>,
    ): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.userGetAttendanceListByDate(date, classMap)
                if (response.code == 200) {
                    _studentAttendanceListLiveData.postValue(response.attendanceList)
                    _toastMessage.postValue("Refreshed!")
                } else {
                    _toastMessage.postValue("Failed to refresh: Check network connection.")
                }
            } catch (e: Exception) {
                Log.d("professorStudentAttendanceListError", e.message.toString())
                _toastMessage.postValue("Failed to refresh: Check network connection.")
            }
        }
        return _studentAttendanceListLiveData
    }
}
