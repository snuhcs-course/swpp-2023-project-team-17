/*
 * StudentMainViewModel is a ViewModel class that provides data and business logic for the StudentMainFragment.
 * It handles actions related to class joining and retrieving the list of classes for a student user.
 */

package com.example.goclass.ui.mainui.usermain

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.ui.classui.ClassScheduler
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.repository.ClassRepository
import com.example.goclass.repository.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.lang.Exception

class StudentMainViewModel(
    private val userRepository: UserRepository,
    private val classRepository: ClassRepository,
    application: Application,
) : AndroidViewModel(application), KoinComponent {
    private val _snackbarMessage = MutableLiveData<String>()
    private val _classListLiveData: MutableLiveData<List<ClassesResponse>> = MutableLiveData()

    val snackbarMessage: LiveData<String> get() = _snackbarMessage
    val classListLiveData: LiveData<List<ClassesResponse>> get() = _classListLiveData

    data class TimeElement(
        val dayOfWeek: Int,
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
    )

    /**
     * Attempts to join a class by making a network request and scheduling class attendance.
     * Updates the LiveData with the result and retrieves the updated class list.
     */
    fun classJoin(
        userId: Int,
        className: String,
        classCode: String,
        classScheduler: ClassScheduler,
    ) {
        viewModelScope.launch {
            val joinClass =
                Classes(
                    className,
                    classCode,
                )
            try {
                // Join class
                val response = classRepository.classJoin(userId, joinClass)
                val userType = 0

                // Schedule class attendance
                val classId = response.classId
                val classTime = response.classTime

                val timeElements = classTime.split(",")

                val timeRegex = """(\d+) (\d+):(\d+)-(\d+):(\d+)""".toRegex()

                val parsedTimes =
                    timeElements.mapNotNull { element ->
                        val match = timeRegex.find(element)
                        if (match != null) {
                            val dayOfWeek = match.groupValues[1].toInt()
                            val startHour = match.groupValues[2].toInt()
                            val startMinute = match.groupValues[3].toInt()
                            val endHour = match.groupValues[4].toInt()
                            val endMinute = match.groupValues[5].toInt()
                            TimeElement(dayOfWeek, startHour, startMinute, endHour, endMinute)
                        } else {
                            null
                        }
                    }

                for ((dayOfWeek, startHour, startMinute, endHour, endMinute) in parsedTimes) {
                    classScheduler.scheduleClass(
                        getApplication(),
                        userId,
                        classId,
                        dayOfWeek,
                        startHour,
                        startMinute,
                        endHour,
                        endMinute,
                        userType,
                    )
                }

                if (response.code == 200) {
                    _snackbarMessage.postValue("Successfully joined!")
                    getClassList(mapOf("userId" to userId.toString(), "userType" to "0"))
                } else {
                    _snackbarMessage.postValue("Failed to join: Check class name or class code again.")
                }
            } catch (e: Exception) {
                _snackbarMessage.postValue("Failed to join: Check class name or class code again.")
            }
        }
    }

    /**
     * Retrieves the list of classes for the student user.
     */
    fun getClassList(user: Map<String, String>): MutableLiveData<List<ClassesResponse>> {
        viewModelScope.launch {
            try {
                val response = userRepository.userGetClassList(user)
                if (response.code == 200) {
                    _classListLiveData.postValue(response.classList)
                }
            } catch (e: Exception) {
                _snackbarMessage.postValue("Cannot load class list. Check your network connection.")
                Log.d("classListError", e.message.toString())
            }
        }
        return _classListLiveData
    }
}
