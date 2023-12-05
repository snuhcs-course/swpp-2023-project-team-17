package com.example.goclass.ui.mainui.usermain

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.repository.ClassRepository
import com.example.goclass.repository.UserRepository
import com.example.goclass.ui.classui.ClassScheduler
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.lang.Exception

class ProfessorMainViewModel(
    private val userRepository: UserRepository,
    private val classRepository: ClassRepository,
    application: Application,
) : AndroidViewModel(application), KoinComponent {
    private val _snackbarMessage = MutableLiveData<String>()
    private val _classListLiveData: MutableLiveData<List<ClassesResponse>> = MutableLiveData()

    val classListLiveData: LiveData<List<ClassesResponse>> get() = _classListLiveData
    val snackbarMessage: LiveData<String> get() = _snackbarMessage

    fun createClass(
        className: String,
        classCode: String,
        professorId: Int,
        classTime: String,
        buildingNumber: String,
        roomNumber: String,
        classScheduler: ClassScheduler,
    ) {
        viewModelScope.launch {
            val newClass =
                Classes(
                    className,
                    classCode,
                    professorId,
                    classTime,
                    buildingNumber,
                    roomNumber,
                )
            try {
                val response = classRepository.classCreate(newClass)

                // schedule class attendance
                val classId = response.classId
                val userType = 1

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
                            StudentMainViewModel.TimeElement(
                                dayOfWeek,
                                startHour,
                                startMinute,
                                endHour,
                                endMinute
                            )
                        } else {
                            null
                        }
                    }

                for ((dayOfWeek, startHour, startMinute, endHour, endMinute) in parsedTimes) {
//                    val minPresentDuration = classResponse.minPresentDuration
//                    val minLateDuration = classResponse.minLateDuration
                    classScheduler.scheduleClass(
                        getApplication(),
                        0, // userId not needed for professors
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
                    _snackbarMessage.postValue("Successfully created!")
                    getClassList(mapOf("userId" to professorId.toString(), "userType" to "1"))
                } else {
                    _snackbarMessage.postValue("Failed to create...")
                }
            } catch (e: Exception) {
                _snackbarMessage.postValue("Error: ${e.message}")
                Log.d("createclass", "${e.message}")
            }
        }
    }

    fun getClassList(user: Map<String, String>): MutableLiveData<List<ClassesResponse>> {
        viewModelScope.launch {
            try {
                val response = userRepository.userGetClassList(user)
                if (response.code == 200) {
                    _classListLiveData.postValue(response.classList)
                }
            } catch (e: Exception) {
                Log.d("classListError", e.message.toString())
                _snackbarMessage.postValue("Cannot load class list. Check your network connection.")
            }
        }
        return _classListLiveData
    }

    fun deleteClass(classId: Int, professorId: Int) {
        viewModelScope.launch {
            try {
                val response = classRepository.classDelete(classId)
                if(response.code == 200) {
                    _snackbarMessage.postValue("Successfully deleted.")
                    getClassList(mapOf("userId" to professorId.toString(), "userType" to "1"))
                } else {
                    _snackbarMessage.postValue("Cannot delete class. Check your network connection.")
                }
            } catch (e: Exception) {
                Log.d("classDeleteError", e.message.toString())
                _snackbarMessage.postValue("Cannot delete class. Check your network connection.")
            }
        }
    }
}
