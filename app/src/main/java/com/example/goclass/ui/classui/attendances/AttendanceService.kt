package com.example.goclass.ui.classui.attendances

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.goclass.repository.Repository
import com.example.goclass.network.dataclass.Attendances
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AttendanceService : Service() {
    private lateinit var repository: Repository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        if (intent != null) {
            val action = intent.action

            if (action == "ATTENDANCE_CHECK_ACTION") {
                val userId = intent.getIntExtra("userId", -1)
                val classId = intent.getIntExtra("classId", -1)
                val startHour = intent.getIntExtra("startHour", -1)
                val startMinute = intent.getIntExtra("startMinute", -1)
                val endHour = intent.getIntExtra("startHour", -1)
                val endMinute = intent.getIntExtra("startMinute", -1)
                if (userId != -1 && classId != -1) {
                    val attendances =
                        performAttendanceCheck(userId, classId, startHour, startMinute, endHour, endMinute)
                } else {
                    Log.d("Error", "Invalid userId or classId")
                }
            }
        }

        return START_STICKY
    }

    private fun performAttendanceCheck(
        userId: Int,
        classId: Int,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ) {
        var attendanceStatus = 0
        var attendanceDuration = 0

        val classDuration = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)

        // TODO: implement attendance check logic (while (class-time has not ended) {})
        if (inClass()) {
            // initial in_class check
        }
        GlobalScope.launch(Dispatchers.IO) {
            delay(60000) // 60 seconds
            if (inClass()) {
                attendanceDuration += 1 // add 1 minute ie. 60 sec delayed
            }
        }

        // TODO: professor should set this
        val minPresentDuration = classDuration * 0.85
        val minLateDuration = classDuration * 0.6

        // determine attendanceStatus
        if (attendanceDuration >= minPresentDuration) {
            attendanceStatus = 2
        } else if (attendanceDuration >= minLateDuration) {
            attendanceStatus = 1
        }

        // save attendance to DB
        GlobalScope.launch(Dispatchers.IO) {
            delay(60000) // 60 seconds
            saveAttendance(attendanceStatus, attendanceDuration, userId, classId)
        }
    }

    private fun inClass(): Boolean {
        // TODO: implement checkGPS() and checkBLE() and checkProf() elsewhere
//        if (checkGPS() and checkBLE() and checkProf()) {
//            return true
//        }
//        return false
        return true
    }

    private fun saveAttendance(
        attendanceStatus: Int,
        attendanceDuration: Int,
        userId: Int,
        classId: Int,
    ) {
        val attendances = Attendances(attendanceStatus, attendanceDuration, classId)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                repository.attendanceAdd(userId, attendances)
                Log.d("AttendanceSaveSuccess", "$attendanceStatus, $attendanceDuration")
            } catch (e: Exception) {
                Log.e("AttendanceSaveError", e.message.toString())
            }
        }
    }
}
