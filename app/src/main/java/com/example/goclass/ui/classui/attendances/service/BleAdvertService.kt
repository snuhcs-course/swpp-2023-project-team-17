package com.example.goclass.ui.classui.attendances.service

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.goclass.ui.classui.attendances.service.BleScanService
import com.example.goclass.utility.Constants
import java.util.Timer
import java.util.TimerTask
import java.lang.NumberFormatException
import java.util.UUID

class BleAdvertService : Service() {

    private lateinit var advertiseTimer: Timer

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var advertiseHandler: Handler? = null

    private var classId = -1
    private var durationMillis = 6300000L // default: 105 min

    private lateinit var advertiseData: AdvertiseData
//    = AdvertiseData.Builder()
//        .setIncludeDeviceName(true)
//        .addServiceUuid(ParcelUuid.fromString(uuid)) // Replace with your actual service UUID
//        .build()

    private var advertiseSettings = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .setConnectable(false)
        .setTimeout(0) // Advertise indefinitely (or set a specific duration)
        .build()

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.i(TAG, "BLE advertising started successfully")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.e(TAG, "BLE advertising onStartFailure: $errorCode")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "BleAdvertService가 생성됨")
        initializeBluetooth()

        if (!bluetoothAdapter!!.isMultipleAdvertisementSupported) {
            Log.e(TAG, "이 기기는 블루투스 LE 광고를 지원하지 않습니다.")
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "BleAdvertService 시작됨, Intent action: ${intent?.action}")
        if (intent != null) {
            val action = intent.action

            if (action == "BLE_ADVERT_ACTION") {
                classId = intent.getIntExtra("classId", -1)
                val startHour = intent.getIntExtra("startHour", -1)
                val startMinute = intent.getIntExtra("startMinute", -1)
                val endHour = intent.getIntExtra("endHour", -1)
                val endMinute = intent.getIntExtra("endMinute", -1)
                if (classId != -1) {
                    Log.d(TAG, "endHour: $endHour")
                    Log.d(TAG, "endMinute: $endMinute")
                    Log.d(TAG, "startHour: $startHour")
                    Log.d(TAG, "startMinute: $startMinute")
                    durationMillis = (((endHour*60 + endMinute) - (startHour*60 + startMinute)) * 60 * 1000).toLong()
                    Log.d(TAG, "durationMillis: $durationMillis")
                    val formattedClassId = classId.toString().padEnd(8,'0')
                    Log.d(TAG, "$formattedClassId")
                    val formattedUuid = "$formattedClassId-0000-1100-8000-00805f9b34fc"
                    val sampleUuid = UUID.randomUUID().toString()
                    try {
                        val parcelUuid = ParcelUuid.fromString(formattedUuid)
//                        val deviceName = Constants.advertisingDeviceName
//                        advertiseData = AdvertiseData.Builder()
//                            .setIncludeDeviceName(false)
//                            .addServiceUuid(parcelUuid)
//                            .build()
                        advertiseData = AdvertiseData.Builder()
//                            .setIncludeDeviceName(true) // Include the device name
                            .addServiceUuid(parcelUuid)
                            .setIncludeTxPowerLevel(false) // Include if you want to include TX power level
                            .build()

                        // Set the device name
//                        advertiseSettings = advertiseSettings.setLocalName(deviceName)

                        Log.d(TAG, "AdvertiseSettings: $advertiseSettings")
                        Log.d(TAG, "AdvertiseData: $advertiseData")

                        startAdvertising()
                    } catch (e: NumberFormatException) {
                        Log.e("BleAdvertService", "Invalid UUID format: $formattedUuid", e)
                        stopSelf()
                    }
                } else {
                    Log.d("Error", "Invalid userId or classId")
                    stopSelf()
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        stopAdvertising()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun initializeBluetooth() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Log.e(TAG, "Bluetooth is not enabled")
            stopSelf()
        } else {
            Log.d(TAG, "블루투스 어댑터 준비 완료")
        }
    }

    private fun startAdvertising() {
        Log.d(TAG, "startAdvertising 호출됨")
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_ADVERTISE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
        // Android 12 (API 레벨 31) 이상에서는 BLUETOOTH_CONNECT 권한도 필요
        val bluetoothConnectPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            null // 이전 버전에서는 필요 없음
        }

        val hasAdvertisePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
        val hasConnectPermission = bluetoothConnectPermission?.let { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED } ?: true

        if (!hasAdvertisePermission || !hasConnectPermission) {
            Log.e(TAG, "필요한 블루투스 광고 권한이 없습니다.")
            return
        }

        Log.d(TAG, "in startAdvertising - advertiseData: $advertiseData")

        Log.d(TAG, "bluetoothLeAdvertiser: $bluetoothAdapter.bluetoothLeAdvertiser")
        bluetoothAdapter?.bluetoothLeAdvertiser?.startAdvertising(
            advertiseSettings,
            advertiseData,
            advertiseCallback
        )
        Log.d(TAG, "After startAdvertising")

        // Schedule to stop advertising after a certain duration (e.g., durationMillis)
        Log.d(TAG, "durationMillis: $durationMillis")
        advertiseHandler = Handler()
        advertiseHandler?.postDelayed({
            stopAdvertising()
        }, durationMillis)
//        Log.d(TAG, "durationMillis: $durationMillis")
//        advertiseTimer = Timer()
//        advertiseTimer.schedule(object : TimerTask() {
//            override fun run() {
//                stopAdvertising()
//            }
//        }, durationMillis)
    }

    private fun stopAdvertising() {
        Log.d(TAG, "stopAdvertising")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        bluetoothAdapter?.bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
//        advertiseTimer.cancel()
        stopSelf()
    }

    companion object {
        private const val TAG = "BleAdvertService"
        const val EXTRA_DURATION_MILLIS = "extra_duration_millis"
        private const val DEFAULT_ADVERTISING_DURATION_MILLIS = 6300000L // 105 seconds
    }
}
