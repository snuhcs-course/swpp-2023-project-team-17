package com.example.goclass.ui.classui.attendances.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
//<<<<<<< HEAD
import androidx.core.app.ActivityCompat
//=======
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.goclass.R
//>>>>>>> 40de6801afd7a2dcf7ac48a46f53a9e8c16cabe6
import com.example.goclass.ui.mainui.MainActivity
import java.util.regex.Pattern

class BleScanService : Service() {

    //    private var bleScanCallback: BleScanCallback? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanIntervalHandler: Handler? = null
    private var scanCount = 0
    private var successfulScanCount = 0
    private var classId = -1
    private var scanning = false

    private var deviceFound = false
    private var firstScan = true
    private var firstScanTime = 0

    private var durationSec = 0

    private val scanResultsList = mutableListOf<String>()
    private var minutesElapsed = 0

    private val handler = Handler()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Blescan create")
        initializeBluetooth()

        // Initialize the handler for incrementing scanCount every minute
        scanIntervalHandler = Handler()
        createNotificationChannel()
    }

    private fun initializeParams() {
        scanCount = 0
        successfulScanCount = 0
        scanning = false
        deviceFound = false
        firstScan = true
        firstScanTime = 0
        durationSec = 0
        minutesElapsed = 0
        scanResultsList.clear()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val durationMillis = intent?.getLongExtra(EXTRA_DURATION_MILLIS, DEFAULT_DURATION_MILLIS)
            ?: DEFAULT_DURATION_MILLIS
        classId = intent?.getIntExtra("classId", -1)?: -1
        durationSec = (durationMillis / 1000).toInt()

        // Schedule a task to stop the service after the designated duration
        Log.d(TAG, "durationMillis: $durationMillis")
        handler.postDelayed({
            Log.d(TAG, "durationMillis over")
            stopScanningService()
        }, durationMillis)

        startForegroundNotification()
        startScanningWithInterval()
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: Blescan destroy")
        scanIntervalHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun initializeBluetooth() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Log.e(TAG, "Bluetooth is not enabled")
            stopSelf()
        }

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d(TAG, "onScanResult: ${result?.device?.address}")

            result?.device?.let {
                Log.i(TAG, "Device found with address: ${it.address}")
                if (isTargetDevice(result)) {
                    Log.i(TAG, "Device is TargetDevice")
                    if (firstScan) {
                        deviceFound()
                        firstScan = false
                    }
                    if (!deviceFound) { // 1분 동안 한 번이라도 신호가 잡히면 successfulScanCount 올림
                        successfulScanCount++
                        deviceFound = true
                        while (scanResultsList.size <= minutesElapsed) {
                            scanResultsList += "0"
                        }
                        scanResultsList += "1"
                        Log.i(TAG, "successfulScanCount incremented: $successfulScanCount")
                    } else {
                        Log.i(TAG, "successfulScanCount already incremented: $successfulScanCount")
                    }
                } else {
                    Log.i(TAG, "Device is not TargetDevice")
                }
            } ?: run {
                Log.e(TAG, "No device found")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "Scan failed with error code: $errorCode")
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.d(TAG, "Scan Batch")
        }
    }

    private fun isTargetDevice(result: ScanResult): Boolean {
        Log.d(TAG, "check isTargetDevice")
        val formattedClassId = classId.toString().padEnd(8, '0')
        val targetUuid = "$formattedClassId-0000-1100-8000-00805f9b34fc"
        Log.d(TAG, "target uuid: $targetUuid")
        val beaconId = "8ec90001-f315-4f60-9fb8-838830daea50"//"2cdbdd00-13ee-11e4-9b6c-0002a5d5c518"
        val targetBeaconUuid = ParcelUuid.fromString(beaconId)
        val targetDeviceUuid = ParcelUuid.fromString(targetUuid)
        Log.d(TAG, "detected serviceUuids: ${result.scanRecord?.serviceUuids}")
        Log.d(TAG, "isTargetDevice: ${result.scanRecord?.serviceUuids?.contains(targetDeviceUuid)}")

        return (result.scanRecord?.serviceUuids?.contains(targetDeviceUuid) == true) //&&
//                (result.scanRecord?.serviceUuids?.contains(targetBeaconUuid) == true)
    }

    private fun startScanningWithInterval() {
        Log.d(TAG, "startScanningWithInterval success")
        // Initial scan
        startScanning()

        // Schedule periodic scans with a 1-minute interval
        scanIntervalHandler?.postDelayed({
            deviceFound = false
            minutesElapsed++
            stopScanning()
            startScanningWithInterval()
        }, SCAN_INTERVAL_MILLIS)
    }

    private fun stopScanningService() {
        Log.d(TAG, "stopScanningService")
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
        scanIntervalHandler?.removeCallbacksAndMessages(null)

        sendScanResults()

        initializeParams()

        stopForeground(true)

        stopSelf()
    }

    private fun stopScanning() {
        Log.d(TAG, "stopScanning")
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

        deviceFound = false
        scanCount++
        Log.d(TAG, "scanCount in stopScanning: $scanCount")
    }
    private fun deviceFound() {
        firstScanTime = scanCount
        Log.d(TAG, "FirstScanTime: $firstScanTime")
    }

    private fun startScanning() {

        Log.d(TAG, "start scan")
        val formattedClassId = classId.toString().padEnd(8, '0')

        val targetBeaconId = "8ec90001-f315-4f60-9fb8-838830daea50"//"2cdbdd00-13ee-11e4-9b6c-0002a5d5c518"
        val targetDeviceId = "$formattedClassId-0000-1100-8000-00805f9b34fc"
        val targetDeviceUuid32BitPattern = "5f9b34fc"

        val targetBeaconUuid = ParcelUuid.fromString(targetBeaconId)
        val targetDeviceUuid = ParcelUuid.fromString(targetDeviceId)
        val beaconMask = ParcelUuid.fromString("00000000-0000-0000-0000-0000FFFF0000")
        val deviceMask = ParcelUuid.fromString("00000000-0000-0000-0000-00000000FFFF")


        val scanFilterDevice = ScanFilter.Builder()
            .setServiceUuid(targetDeviceUuid, deviceMask)
            .build()
        // TODO: modify scanFilterBeacon to filter based on unique signal info of SNU attendance beacons
        val scanFilterBeacon = ScanFilter.Builder()
            .setServiceUuid(targetBeaconUuid, beaconMask)
            .build()

        val scanFilters: List<ScanFilter> = listOf(scanFilterDevice)
//        val scanFilters: List<ScanFilter> = listOf(scanFilterDevice, scanFilterBeacon)

        Log.d(TAG, "scanFilters: $scanFilters")


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
            Log.d(TAG, "no bleScanning permission")
            Log.d(TAG, checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN).toString())
            return
        }
        Log.d(TAG, scanFilters.toString())
        Log.d(TAG, scanSettings.toString())
        Log.d(TAG, scanCallback.toString())
        Log.d(TAG, bluetoothLeScanner.toString())

        val bluetoothConnectPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            null // 이전 버전에서는 필요 없음
        }

        val hasScanPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        val hasConnectPermission = bluetoothConnectPermission?.let { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED } ?: true

        if (!hasScanPermission || !hasConnectPermission) {
            Log.e(TAG, "필요한 블루투스 권한이 없습니다.")
//            return
        }

//        if (scanCount < durationSec) {
        if (scanning) {
            Log.d(TAG, "IN SCANNING!")
            bluetoothLeScanner?.stopScan(scanCallback)
            bluetoothLeScanner?.startScan(scanFilters, scanSettings, scanCallback)
            Log.d(TAG, "after startScan")
        } else {
            bluetoothLeScanner?.startScan(scanFilters, scanSettings, scanCallback)
            Log.d(TAG, "first scan")
            scanning = true
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "BLE Scan Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun startForegroundNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntentFlags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, pendingIntentFlags
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("BLE Scanning")
            .setContentText("출석체크를 위한 BLE scanning 동작 중. 제거 시 출석정보가 수집되지 않을 수 있습니다.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun sendScanResults() {
        val intent = Intent("com.example.goclass.SCAN_RESULTS")

        intent.putExtra("scanResults", scanResultsList.toTypedArray())
        intent.putExtra(EXTRA_SCAN_COUNT, successfulScanCount)
        intent.putExtra(FIRST_SCAN_AT, firstScanTime)

        Log.d(TAG, scanResultsList.toString())
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val ACTION_BLE_SCAN_RESULT = "com.example.goclass.BLE_SCAN_RESULT"
        const val ACTION_BLE_FIRST_SCAN = "com.example.goclass.BLE_FIRST_SCAN"
        const val EXTRA_SCAN_COUNT = "extra_scan_count"
        const val FIRST_SCAN_AT = "first_scan_at"
        private const val TAG = "BleScanningService"
        const val EXTRA_DURATION_MILLIS = "extra_duration_millis"
        private const val DEFAULT_DURATION_MILLIS = 6300000L // 105 minutes (1hr 45min)
        private const val SCAN_INTERVAL_MILLIS = 60000L // 1 minute
        private const val CHANNEL_ID = "BleScanServiceChannel"
    }
}

