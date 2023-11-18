package com.example.goclass.ui.classui.attendances.reciever

import BleAdvertService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.goclass.ui.classui.attendances.service.AttendanceService

class AttendanceReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (intent != null) {
            val action = intent.action
            if (action == "ATTENDANCE_ALARM_ACTION") {
                // Extract the alarm details from the intent
                val userId = intent.getIntExtra("userId", -1)
                val classId = intent.getIntExtra("classId", -1)
                val startHour = intent.getIntExtra("startHour", -1)
                val startMinute = intent.getIntExtra("startMinute", -1)
                val endHour = intent.getIntExtra("endHour", -1)
                val endMinute = intent.getIntExtra("endMinute", -1)
                val userType = intent.getIntExtra("userType", -1)

                if (userType == 0) {
                    // Create an intent to start the AttendanceService
                    val serviceIntent = Intent(context, AttendanceService::class.java)
                    serviceIntent.action = "ATTENDANCE_CHECK_ACTION"
                    intent.putExtra("userId", userId)
                    intent.putExtra("classId", classId)
                    intent.putExtra("startHour", startHour)
                    intent.putExtra("startMinute", startMinute)
                    intent.putExtra("endHour", endHour)
                    intent.putExtra("endMinute", endMinute)

                    // Start the AttendanceService to perform the attendance check
                    context.startService(serviceIntent)
                } else if (userType == 1) {
                    // Create an intent to start the BleAdvertService
                    val serviceIntent = Intent(context, BleAdvertService::class.java)
                    serviceIntent.action = "BLE_ADVERT_ACTION"
                    intent.putExtra("classId", classId)
                    intent.putExtra("startHour", startHour)
                    intent.putExtra("startMinute", startMinute)
                    intent.putExtra("endHour", endHour)
                    intent.putExtra("endMinute", endMinute)

                    // Start the AttendanceService to perform the attendance check
                    context.startService(serviceIntent)
                } else {
                    Log.d("AttendanceReceiver", "Error in userType")
                }
            }
        }
    }
}
