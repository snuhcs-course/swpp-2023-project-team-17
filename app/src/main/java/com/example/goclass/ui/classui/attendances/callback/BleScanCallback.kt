package com.example.goclass.ui.classui.attendances.callback

interface BleScanCallback {
    fun onDeviceFound()
    fun onScanFailed(errorCode: Int)
}