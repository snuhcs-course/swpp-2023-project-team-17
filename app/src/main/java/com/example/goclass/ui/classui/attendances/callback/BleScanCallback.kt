package com.example.goclass.ui.classui.attendances.callback

interface BleScanCallback {
    fun onDeviceFound(scanCount: Int)

    fun onScanFailed(errorCode: Int)

    fun onScanFinish()
}
