package com.example.goclass.ui.classui.attendances.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.goclass.ui.classui.attendances.service.AttendanceService
import com.example.goclass.ui.classui.attendances.service.BleAdvertService

class AttendanceReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        Log.d("atreceiver", "called")
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
                Log.d("usertypecheck", "$userType")
                Log.d("classIdcheck", "$classId")

                if (userType == 0) {
                    // Create an intent to start the AttendanceService
                    val serviceIntent = Intent(context, AttendanceService::class.java)
                    //함수로 분리 가능?
                    serviceIntent.action = "ATTENDANCE_CHECK_ACTION"
                    serviceIntent.putExtra("userId", userId)
                    serviceIntent.putExtra("classId", classId)
                    serviceIntent.putExtra("startHour", startHour)
                    serviceIntent.putExtra("startMinute", startMinute)
                    serviceIntent.putExtra("endHour", endHour)
                    serviceIntent.putExtra("endMinute", endMinute)

                    // Start the AttendanceService to perform the attendance check
                    context.startService(serviceIntent)
                    Log.d("AttendanceReceiver", "start AttendanceService")
                } else if (userType == 1) {
                    // Create an intent to start the BleAdvertService
                    val serviceIntent = Intent(context, BleAdvertService::class.java)
                    //여기도
                    serviceIntent.action = "BLE_ADVERT_ACTION"
                    serviceIntent.putExtra("classId", classId)
                    serviceIntent.putExtra("startHour", startHour)
                    serviceIntent.putExtra("startMinute", startMinute)
                    serviceIntent.putExtra("endHour", endHour)
                    serviceIntent.putExtra("endMinute", endMinute)
                    Log.d("AttendanceReceiver", "endHour: $endHour")
                    Log.d("AttendanceReceiver", "endMinute: $endMinute")
                    Log.d("AttendanceReceiver", "startHour: $startHour")
                    Log.d("AttendanceReceiver", "startMinute: $startMinute")

                    // Start the AttendanceService to perform the attendance check
                    context.startService(serviceIntent)
                    Log.d("AttendanceReceiver", "start BleAdvertService")
                } else {
                    Log.d("AttendanceReceiver", "Error in userType")
                }
            }
        }
    }
}
