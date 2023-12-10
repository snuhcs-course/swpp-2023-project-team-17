/*
 * AttendanceService is a background service responsible for managing and handling BLE attendance checking in the GoClass app.
 * It starts the BleScanService, listens for BLE scan results, and performs attendance checks based on scan results.
 *
 * @param userId: User ID associated with the attendance check.
 * @param classId: Class ID for uniquely identifying the class.
 * @param scanCount: Number of BLE scans performed during the attendance check.
 * @param firstSuccess: Scan count at the first successful scan during the attendance check.
 * @param viewModel: ViewModel for handling data related to attendance.
 * @param scanResultsReceiver: BroadcastReceiver for receiving BLE scan results.
 * @param serviceConnection: ServiceConnection for binding to the BleScanService.
 */

package com.example.goclass.ui.classui.attendances.service

import android.app.Service
import android.content.*
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

    // BroadcastReceiver for handling BLE scan results.
    private val scanResultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.goclass.SCAN_RESULTS") {
                // Extract relevant information from the received intent.
                val scanResults = intent.getStringArrayExtra("scanResults")
                firstSuccess = intent.getIntExtra(BleScanService.FIRST_SCAN_AT, 0) + 1
                scanCount = intent.getIntExtra(BleScanService.EXTRA_SCAN_COUNT, 0)
                Log.i(TAG, "Received first scan at: $firstSuccess")
                Log.i(TAG, "Received scanCount: $scanCount")

                // Perform attendance check using the received scan results.
                scanResults?.let {
                    val scanResultsList = it.toList()
                    performAttendanceCheck(scanResultsList)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "AttendanceService created")

        // Register the BroadcastReceiver for receiving BLE scan results.
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(scanResultsReceiver, IntentFilter("com.example.goclass.SCAN_RESULTS"))
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        // Unregister the BroadcastReceiver when the service is destroyed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(scanResultsReceiver)

        // Unbind from the BleScanService.
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
        Log.i(TAG, "AttendanceService started, Intent action: ${intent?.action}")
        if (intent != null) {
            val action = intent.action

            if (action == "ATTENDANCE_CHECK_ACTION") {
                userId = intent.getIntExtra("userId", -1)
                classId = intent.getIntExtra("classId", -1)
                val startHour = intent.getIntExtra("startHour", -1)
                val startMinute = intent.getIntExtra("startMinute", -1)
                val endHour = intent.getIntExtra("endHour", -1)
                val endMinute = intent.getIntExtra("endMinute", -1)

                // Check if user ID and class ID are valid before initiating BLE scanning.
                if (userId != -1 && classId != -1) {
                    val durationMillis = ((endHour * 60 + endMinute) - (startHour * 60 + startMinute))
                    startBleScanning(durationMillis)
                } else {
                    Log.d("Error", "Invalid userId or classId")
                }
            }
        }

        return START_STICKY
    }

    // Start BLE scanning for the specified duration.
    private fun startBleScanning(durationMillis: Int) {
        val durationMillisLong = durationMillis.toLong() * 60000 // Convert minutes to milliseconds

        // Start the BleScanService.
        val serviceIntent = Intent(this, BleScanService::class.java)
        serviceIntent.putExtra(BleScanService.EXTRA_DURATION_MILLIS, durationMillisLong)
        serviceIntent.putExtra("classId", classId)
        startService(serviceIntent)

        // Bind to the BleScanService.
        val bindIntent = Intent(this, BleScanService::class.java)
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    // Perform attendance check based on BLE scan results.
    private fun performAttendanceCheck(scanResults: List<String>) {
        Log.d(TAG, "performAttendanceCheck")

        // Determine attendance status based on scan count and first successful scan.
        val attendanceStatus =
            if (scanCount == 0) {
                0
            } else if (firstSuccess <= 10) {
                2 // present
            } else if (firstSuccess <= 30) {
                1 // late
            } else {
                0 // absent
            }

        val attendanceDuration = scanCount
        Log.d(TAG, "attendanceDuration: $attendanceDuration")

        Log.d(TAG, scanResults.toString())

        // Save attendance information using the ViewModel.
        viewModel.saveAttendance(attendanceStatus, attendanceDuration, userId, classId, scanResults)
    }

    // ServiceConnection for binding to the BleScanService.
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // Called when the connection is established.
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // Called when the connection is unexpectedly disconnected.
        }
    }

    companion object {
        private const val TAG = "AttendanceService"
    }
}
