/*
 * ProfessorAttendanceViewModel is a ViewModel responsible for managing the data and business logic
 * related to the professor's attendance overview in the UI.
 *
 * @property repository: UserRepository, the repository responsible for handling user-related data operations.
 * @property _professorAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>>, holds the LiveData for professor attendance overview data.
 * @property _toastMessage: MutableLiveData<String>, holds the LiveData for displaying toast messages in the UI.
 *
 * getProfessorAttendanceList: Fetches the professor attendance overview data, updating the LiveData accordingly.
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

class ProfessorAttendanceViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _professorAttendanceListLiveData: MutableLiveData<List<AttendancesResponse>> = MutableLiveData()
    private val _toastMessage = MutableLiveData<String>()

    // professorAttendanceListLiveData: Exposes the LiveData for professor attendance overview data to observers.
    val professorAttendanceListLiveData: LiveData<List<AttendancesResponse>> get() = _professorAttendanceListLiveData
    // toastMessage: Exposes the LiveData for toast messages to observers.
    val toastMessage: LiveData<String> get() = _toastMessage

    /*
     * getProfessorAttendanceList: Fetches the professor attendance overview data,
     * updating the LiveData accordingly. Handles network and repository errors and notifies
     * the UI with appropriate toast messages.
     */
    fun getProfessorAttendanceList(classMap: Map<String, String>): MutableLiveData<List<AttendancesResponse>> {
        viewModelScope.launch {
            try {
                val response = repository.attendanceGetDateList(classMap)
                if (response.code == 200) {
                    _professorAttendanceListLiveData.postValue(response.attendanceDateList)
                }
            } catch (e: Exception) {
                Log.d("professorAttendanceListError", e.message.toString())
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
        return _professorAttendanceListLiveData
    }
}
