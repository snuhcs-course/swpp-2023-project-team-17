package com.example.goclass.ui.classui.attendances.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

    private lateinit var bleScanService: BleScanService
    private var classEnd = false

    private var attendanceStatus = 0
    private var userId = -1
    private var classId = -1

    private var scanCount: Int = 0

    private val bleScanResultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BleScanService.ACTION_BLE_SCAN_RESULT) {
                scanCount = intent.getIntExtra(BleScanService.EXTRA_SCAN_COUNT, 0)
                Log.i(TAG, "Received scanCount: $scanCount")
                //performAttendanceCheck(scanCount)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "AttendanceService 생성됨")
        // Register the BroadcastReceiver to receive scan results
        val filter = IntentFilter(BleScanService.ACTION_BLE_SCAN_RESULT)
        registerReceiver(bleScanResultReceiver, filter)
    }

    override fun onDestroy() {
        performAttendanceCheck()
        super.onDestroy()

        // Unregister the BroadcastReceiver
        unregisterReceiver(bleScanResultReceiver)

        // Unbind from the BleScanService
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
        Log.i(TAG, "AttendanceService 시작됨, Intent action: ${intent?.action}")
        if (intent != null) {
            val action = intent.action

            if (action == "ATTENDANCE_CHECK_ACTION") {
                userId = intent.getIntExtra("userId", -1)
                classId = intent.getIntExtra("classId", -1)
                val startHour = intent.getIntExtra("startHour", -1)
                val startMinute = intent.getIntExtra("startMinute", -1)
                val endHour = intent.getIntExtra("endHour", -1)
                val endMinute = intent.getIntExtra("endMinute", -1)
                if (userId != -1 && classId != -1) {
                    val durationMillis = ((endHour*60 + endMinute) - (startHour*60 + startMinute) * 60 * 1000).toLong()
                    startBleScanning(durationMillis);
                } else {
                    Log.d("Error", "Invalid userId or classId")
                }
            }
        }

        return START_STICKY
    }

    private fun startBleScanning(durationMillis: Long) {
        bleScanService = BleScanService()
        bleScanService.setBleScanCallback(this)

        // Start the BleScanService
        val serviceIntent = Intent(this, BleScanService::class.java)
        serviceIntent.putExtra(BleScanService.EXTRA_DURATION_MILLIS, durationMillis)
        serviceIntent.putExtra("classId", classId)
        startService(serviceIntent)

        // Bind to the BleScanService
        val bindIntent = Intent(this, BleScanService::class.java)
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun performAttendanceCheck() {
        var attendanceDuration = scanCount

        // val classDuration = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)

//        if (inClass()) {
//            // initial in_class check
//        }
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(60000) // 60 seconds
//            if (inClass()) {
//                attendanceDuration += 1 // add 1 minute ie. 60 sec delayed
//            }
//        }

//        val minPresentDuration = classDuration * 0.85
//        val minLateDuration = classDuration * 0.6
//
//        // determine attendanceStatus
//        if (attendanceDuration >= minPresentDuration) {
//            attendanceStatus = 2
//        } else if (attendanceDuration >= minLateDuration) {
//            attendanceStatus = 1
//        }

        // save attendance to DB
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(60000) // 60 seconds
//            saveAttendance(attendanceStatus, attendanceDuration, userId, classId)
//        }
        saveAttendance(attendanceStatus, attendanceDuration, userId, classId)
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

    override fun onDeviceFound(scanCount: Int) {
        // Handle the device found event
        //isDeviceFound = true
        if (scanCount <= 10) {
            attendanceStatus = 2 // present
        } else if (scanCount <= 30) {
            attendanceStatus = 1 // late
        } else {
            attendanceStatus = 0 // absent
        }
        Log.i(TAG, "Device found: $attendanceStatus")
    }

    override fun onScanFailed(errorCode: Int) {
        // Handle the scan failure event
        //isDeviceFound = false
        // Perform actions based on scan failure
        Log.e(TAG, "Scan failed with error code: $errorCode")
    }

    override fun onScanFinish() {
        classEnd = true
        stopSelf() // stop AttendanceCheckService on callback
        Log.e(TAG, "Scan finished; class ended: $classEnd")
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
//        const val EXTRA_CLASS_DURATION_MILLIS = "extra_duration_millis"
//        private const val DEFAULT_CLASS_DURATION_MILLIS = 6300000L // 105 minutes
    }
}
