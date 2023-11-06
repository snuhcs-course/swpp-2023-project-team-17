package com.example.goclass.mainUi

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.goclass.ClassScheduler
import com.example.goclass.Repository
import com.example.goclass.dataClass.Classes
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.lang.Exception

class StudentMainViewModel(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application), KoinComponent {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    val classListLiveData: MutableLiveData<List<Classes>> = MutableLiveData()

    private val classScheduler = ClassScheduler()

    fun classJoin(
        userId: Int,
        className: String,
        classCode: String,
    ) {
        viewModelScope.launch {
            val joinClass = Classes(className, classCode)
            try {
                // join class
                val response = repository.classJoin(userId, joinClass)

                // schedule class attendance
                val classId = response.classId

                // get class time info
                val classResponse = repository.classGet(classId)
                val classTime = classResponse.classTime

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
//                    val minPresentDuration = classResponse.minPresentDuration
//                    val minLateDuration = classResponse.minLateDuration
                    classScheduler.scheduleClass(
                        getApplication(),
                        userId,
                        classId,
                        dayOfWeek,
                        startHour,
                        startMinute,
                        endHour,
                        endMinute,
                    )
                }

                if (response.code == 200) {
                    _toastMessage.postValue("Successfully joined!")
                    getClassList(mapOf("userId" to userId.toString(), "userType" to "0"))
                } else {
                    _toastMessage.postValue("join failed")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun getClassList(user: Map<String, String>): MutableLiveData<List<Classes>> {
        viewModelScope.launch {
            try {
                val response = repository.userGetClassList(user)
                if (response.code == 200) {
                    classListLiveData.postValue(response.classList)
                }
            } catch (e: Exception) {
                Log.d("classListError", e.message.toString())
            }
        }
        return classListLiveData
    }

    data class TimeElement(
        val dayOfWeek: Int,
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
    )
}
