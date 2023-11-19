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
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.goclass.ui.classui.attendances.service.BleScanService
import com.example.goclass.utility.Constants
import java.lang.NumberFormatException
import java.util.UUID

class BleAdvertService : Service() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var advertiseHandler: Handler? = null

    private var classId = -1
    private var durationMillis = 6300000L // default: 105 min

    private lateinit var advertiseData: AdvertiseData
//    = AdvertiseData.Builder()
//        .setIncludeDeviceName(true)
//        .addServiceUuid(ParcelUuid.fromString(uuid)) // Replace with your actual service UUID
//        .build()

    private val advertiseSettings = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .setConnectable(false)
        //.setTimeout(0) // Advertise indefinitely (or set a specific duration)
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
                    durationMillis = ((endHour*60 + endMinute) - (startHour*60 + startMinute)).toLong()
                    val formattedClassId = classId.toString().padEnd(2,'0')
                    Log.d(TAG, "$formattedClassId")
                    val formattedUuid = "$formattedClassId-0000-1000-8000-00805f9b34fb"
                    val sampleUuid = UUID.randomUUID().toString()
                    try {
                        val parcelUuid = ParcelUuid.fromString(formattedUuid)
                        advertiseData = AdvertiseData.Builder()
                            .setIncludeDeviceName(true)
                            .addServiceUuid(parcelUuid)
                            .build()

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "필요한 블루투스 광고 권한이 없습니다.")
            return
        }
        bluetoothAdapter?.bluetoothLeAdvertiser?.startAdvertising(
            advertiseSettings,
            advertiseData,
            advertiseCallback
        )

        // Schedule to stop advertising after a certain duration (e.g., 10 seconds)
        advertiseHandler = Handler()
        advertiseHandler?.postDelayed({
            stopAdvertising()
        }, durationMillis)
    }

    private fun stopAdvertising() {
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
        stopSelf()
    }

    companion object {
        private const val TAG = "BleAdvertService"
        const val EXTRA_DURATION_MILLIS = "extra_duration_millis"
        private const val DEFAULT_ADVERTISING_DURATION_MILLIS = 6300000L // 105 seconds
    }
}
