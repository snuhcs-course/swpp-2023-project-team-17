package com.example.goclass.ui.classui.attendances.service

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import com.example.goclass.ui.classui.attendances.callback.BleScanCallback
import com.example.goclass.utility.Constants

class BleScanService : Service() {

    private var bleScanCallback: BleScanCallback? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanIntervalHandler: Handler? = null
    private var scanCount = 0
    private var successfulScanCount = 0
    private var classId = -1

    private val handler = Handler()

    override fun onCreate() {
        super.onCreate()
        initializeBluetooth()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val durationMillis = intent?.getLongExtra(EXTRA_DURATION_MILLIS, DEFAULT_DURATION_MILLIS)
            ?: DEFAULT_DURATION_MILLIS
        classId = intent?.getIntExtra("classId", -1)?: -1

        // Schedule a task to stop the service after the designated duration
        handler.postDelayed({
            stopSelf() // This will stop the service after the duration
        }, durationMillis)

        startScanningWithInterval()
        return START_STICKY
    }

    override fun onDestroy() {
        sendSuccessfulScanCount()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun setBleScanCallback(callback: BleScanCallback?) {
        this.bleScanCallback = callback
    }

    private fun initializeBluetooth() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Log.e(TAG, "Bluetooth is not enabled")
            stopSelf()
        }

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.d(TAG, "스캔 결과 수신: ${result?.device?.address}")
            super.onScanResult(callbackType, result)
            result?.device?.let {
                Log.i(TAG, "Device found: ${it.address}")
                if (isTargetDevice(result)) {
                    bleScanCallback?.onDeviceFound(scanCount)
                    successfulScanCount++
                }
            }

//            super.onScanResult(callbackType, result)
//            if (result == null) {
//                Log.i(TAG, "Scan result is null")
//                return
//            }
//
//            val device = result.device
//            val deviceName = "Unknown Device"
//            val deviceAddress = device.address ?: "No Address"
//            val rssi = result.rssi
//
//            // 스캔 결과에 대한 로그
//            Log.i(TAG, "Device found: Name: $deviceName, Address: $deviceAddress, RSSI: $rssi")
//
//            // 타겟 장치 확인 (여기서는 필터링 없이 모든 장치를 로그로 출력)
//            if (isTargetDevice(result)) {
//                bleScanCallback?.onDeviceFound(scanCount)
//                successfulScanCount++
//            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.e(TAG, "Scan failed with error code: $errorCode")
            bleScanCallback?.onScanFailed(errorCode)
        }
    }

    private fun isTargetDevice(result: ScanResult): Boolean {
        val formattedClassId = classId.toString().padStart(6, '0')
        val targetUuid = formattedClassId + Constants.UUID_STRING
        val beaconId = "2cdbdd00-13ee-11e4-9b6c-0002a5d5c51b"
        val targetServiceUuid = ParcelUuid.fromString(beaconId)
        return result.scanRecord?.serviceUuids?.contains(targetServiceUuid) == true
    }

    private fun startScanningWithInterval() {
        // Initial scan
        startScanning()

        // Schedule periodic scans with a 1-minute interval
        scanIntervalHandler = Handler()
        scanIntervalHandler?.postDelayed({
            stopScanning()
            startScanningWithInterval()
        }, SCAN_INTERVAL_MILLIS)
    }

    private fun stopScanning() {
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return
        }
        bluetoothLeScanner?.stopScan(scanCallback)

//        // Pass scanCount to AttendanceService using Intent
//        val intent = Intent(ACTION_BLE_SCAN_RESULT)
//        intent.putExtra(EXTRA_SCAN_COUNT, scanCount)
//        sendBroadcast(intent)

        scanCount++
    }

    private fun sendSuccessfulScanCount() {
        // Pass scanCount to AttendanceService using Intent
        val intent = Intent(ACTION_BLE_SCAN_RESULT)
        intent.putExtra(EXTRA_SCAN_COUNT, successfulScanCount)
        sendBroadcast(intent)
        bleScanCallback?.onScanFinish()
    }

    private fun startScanning() {
        Log.d(TAG, "start scan")

        val formattedClassId = classId.toString().padStart(6, '0')

        val beaconId = "2cdbdd00-13ee-11e4-9b6c-0002a5d5c51b"
        val targetUuid = ParcelUuid.fromString(beaconId)

        val scanFilters: List<ScanFilter> = listOf(
            ScanFilter.Builder()
                .setServiceUuid(targetUuid)
                .build()
        )

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return
        }
        bluetoothLeScanner?.startScan(scanFilters, scanSettings, scanCallback)
    }

    companion object {
        const val ACTION_BLE_SCAN_RESULT = "com.example.yourapp.BLE_SCAN_RESULT"
        const val EXTRA_SCAN_COUNT = "extra_scan_count"
        private const val TAG = "BleScanningService"
        const val EXTRA_DURATION_MILLIS = "extra_duration_millis"
        private const val DEFAULT_DURATION_MILLIS = 6300000L // 105 minutes (1hr 45min)
        private const val SCAN_INTERVAL_MILLIS = 60000L // 1 minute
    }
}
