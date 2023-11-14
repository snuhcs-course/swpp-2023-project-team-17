package com.example.goclass.ui.classui.attendances.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.ui.classui.attendances.callback.BleScanCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AttendanceService : Service(), BleScanCallback {
    private lateinit var repository: AttendanceRepository

    private lateinit var bleScanningService: BleScanningService
    private var isDeviceFound = false

    override fun onCreate() {
        super.onCreate()

        bleScanningService = BleScanningService()
        bleScanningService.setBleScanCallback(this)

        // Start the BleScanningService
        val serviceIntent = Intent(this, BleScanningService::class.java)
        startService(serviceIntent)

        // Bind to the BleScanningService
        val bindIntent = Intent(this, BleScanningService::class.java)
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unbind from the BleScanningService
        unbindService(serviceConnection)
    }

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
                val endHour = intent.getIntExtra("endHour", -1)
                val endMinute = intent.getIntExtra("endMinute", -1)
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

    override fun onDeviceFound() {
        // Handle the device found event
        isDeviceFound = true
        // Perform actions based on device found
        Log.i(TAG, "Device found!")
    }

    override fun onScanFailed(errorCode: Int) {
        // Handle the scan failure event
        isDeviceFound = false
        // Perform actions based on scan failure
        Log.e(TAG, "Scan failed with error code: $errorCode")
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // Called when the connection is established
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // Called when the connection is unexpectedly disconnected
        }
    }

    companion object {
        private const val TAG = "AttendanceService"
    }
}
