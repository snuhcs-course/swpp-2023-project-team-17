/*
 * BleAdvertService is a background service responsible for advertising BLE (Bluetooth Low Energy) during class hours for attendance tracking.
 * It utilizes Bluetooth LE advertising to broadcast information about the ongoing class.
 *
 * @param advertiseTimer: Timer for managing the advertising duration.
 * @param bluetoothAdapter: BluetoothAdapter for handling Bluetooth operations.
 * @param advertiseHandler: Handler for scheduling the stop of advertising after a certain duration.
 * @param classId: Unique identifier for the class.
 * @param durationMillis: Duration for which BLE advertising should be active.
 * @param advertiseData: AdvertiseData containing information to be broadcasted.
 * @param advertiseSettings: AdvertiseSettings specifying the advertising behavior.
 * @param advertiseCallback: AdvertiseCallback for handling advertising success or failure.
 */

package com.example.goclass.ui.classui.attendances.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.goclass.R
import com.example.goclass.ui.mainui.MainActivity
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
        Log.i(TAG, "BleAdvertService created")
        initializeBluetooth()
        createNotificationChannel()

        if (!bluetoothAdapter!!.isMultipleAdvertisementSupported) {
            Log.e(TAG, "This device does not support Bluetooth LE advertising.")
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "BleAdvertService started, Intent action: ${intent?.action}")
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
                    durationMillis = (((endHour * 60 + endMinute) - (startHour * 60 + startMinute)) * 60 * 1000).toLong()
                    Log.d(TAG, "durationMillis: $durationMillis")
                    val formattedClassId = classId.toString().padEnd(8, '0')
                    Log.d(TAG, "$formattedClassId")
                    val formattedUuid = "$formattedClassId-0000-1100-8000-00805f9b34fc"
                    val sampleUuid = UUID.randomUUID().toString()
                    try {
                        val parcelUuid = ParcelUuid.fromString(formattedUuid)

                        advertiseData = AdvertiseData.Builder()
                            .addServiceUuid(parcelUuid)
                            .setIncludeTxPowerLevel(false) // Include if you want to include TX power level
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
            Log.d(TAG, "Bluetooth adapter ready")
        }
    }

    private fun startAdvertising() {
        Log.d(TAG, "startAdvertising called")

        val bluetoothConnectPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            null // Not needed in previous versions
        }

        val hasAdvertisePermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
        val hasConnectPermission =
            bluetoothConnectPermission?.let { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED } ?: true

        if (!hasAdvertisePermission || !hasConnectPermission) {
            Log.e(TAG, "Required Bluetooth advertising permissions are missing.")
            return
        }

        Log.d(TAG, "in startAdvertising - advertiseData: $advertiseData")
        startForeground(NOTIFICATION_ID, createNotification())

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
    }

    private fun stopAdvertising() {
        Log.d(TAG, "stopAdvertising")
        stopForeground(true)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothAdapter?.bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name_ble_advert)
            val descriptionText = getString(R.string.notification_channel_description_ble_advert)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntentFlags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                pendingIntentFlags,
            )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.ble_advert_notification_title))
            .setContentText(getString(R.string.ble_advert_notification_content))
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icon needs to be set
            .setContentIntent(pendingIntent)
            .build()
    }

    companion object {
        private const val TAG = "BleAdvertService"
        const val EXTRA_DURATION_MILLIS = "extra_duration_millis"
        private const val DEFAULT_ADVERTISING_DURATION_MILLIS = 6300000L // 105 minutes
        private const val NOTIFICATION_CHANNEL_ID = "ble_advert_service_channel"
        private const val NOTIFICATION_ID = 1
    }
}
