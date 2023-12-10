package com.example.goclass.ui.classui.attendances.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.android.inject

class AttendanceService : Service() {
    private val viewModel: AttendanceServiceViewModel by inject()

    private var userId = -1
    private var classId = -1
    private var scanCount: Int = 0
    private var firstSuccess: Int = 0 // scan count at first successful scan

    private val scanResultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.goclass.SCAN_RESULTS") {
                val scanResults = intent.getStringArrayExtra("scanResults")
                firstSuccess = intent.getIntExtra(BleScanService.FIRST_SCAN_AT, 0) + 1
                scanCount = intent.getIntExtra(BleScanService.EXTRA_SCAN_COUNT, 0)
                Log.i(TAG, "Received first scan at: $firstSuccess")
                Log.i(TAG, "Received scanCount: $scanCount")

                scanResults?.let{
                    val scanResultsList = it.toList()
                    performAttendanceCheck(scanResultsList)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "AttendanceService 생성됨")

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(scanResultsReceiver, IntentFilter("com.example.goclass.SCAN_RESULTS"))
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(scanResultsReceiver)

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
                    val durationMillis = ((endHour*60 + endMinute) - (startHour*60 + startMinute))
                    startBleScanning(durationMillis);
                } else {
                    Log.d("Error", "Invalid userId or classId")
                }
            }
        }

        return START_STICKY
    }

    private fun startBleScanning(durationMillis: Int) {
        val durationMillisLong = durationMillis.toLong() * 60000 // Convert minutes to milliseconds

        // Start the BleScanService
        val serviceIntent = Intent(this, BleScanService::class.java)
        serviceIntent.putExtra(BleScanService.EXTRA_DURATION_MILLIS, durationMillisLong)
        serviceIntent.putExtra("classId", classId)
        startService(serviceIntent)

        // Bind to the BleScanService
        val bindIntent = Intent(this, BleScanService::class.java)
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun performAttendanceCheck(scanResults: List<String>) {
        Log.d(TAG, "performAttendanceCheck")

        val attendanceStatus =
            if (scanCount == 0) {
                0
            } else if (firstSuccess <= 5) {
                2 // present
            } else if (firstSuccess <= 30) {
                1 // late
            } else {
                0 // absent
            }

        val attendanceDuration = scanCount
        Log.d(TAG, "attendanceDuration: $attendanceDuration")

        Log.d(TAG, scanResults.toString())

        viewModel.saveAttendance(attendanceStatus, attendanceDuration, userId, classId, scanResults)
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
