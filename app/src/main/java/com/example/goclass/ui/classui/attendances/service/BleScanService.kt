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
import androidx.core.app.ActivityCompat
import com.example.goclass.ui.classui.attendances.callback.BleScanCallback

class BleScanningService : Service() {

    private var bleScanCallback: BleScanCallback? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null

    override fun onCreate() {
        super.onCreate()
        initializeBluetooth()
        startScanning()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        stopScanning()
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
            super.onScanResult(callbackType, result)
            result?.device?.let {
                Log.i(TAG, "Device found: ${it.address}")
                if (isTargetDevice(result)) {
                    bleScanCallback?.onDeviceFound()
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.e(TAG, "Scan failed with error code: $errorCode")
            bleScanCallback?.onScanFailed(errorCode)
        }
    }

    private fun isTargetDevice(result: ScanResult): Boolean {
        // For example, check if the device advertises a specific service UUID
        val targetServiceUuid = ParcelUuid.fromString("Your Target Service UUID")
        return result.scanRecord?.serviceUuids?.contains(targetServiceUuid) == true
    }

    private fun startScanning() {
        val scanFilters: List<ScanFilter> = listOf(
            ScanFilter.Builder()
                .setDeviceAddress("TargetDeviceAddress")
                .build()
        )

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted; TODO: show a notification
            return
        }
        bluetoothLeScanner?.startScan(scanFilters, scanSettings, scanCallback)

        // Stop scanning after a certain period (e.g., 10 seconds)
        Handler().postDelayed({
            stopScanning()
        }, 10000)
    }

    private fun stopScanning() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted; TODO: show a notification
            return
        }
        bluetoothLeScanner?.stopScan(scanCallback)
    }

    companion object {
        private const val TAG = "BleScanningService"
    }
}
