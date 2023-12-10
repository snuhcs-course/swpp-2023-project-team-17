/*
 * AttendanceReceiver is a BroadcastReceiver responsible for handling attendance-related alarms in the GoClass app.
 * When an alarm is triggered, it extracts information from the intent and starts the appropriate service
 * (AttendanceService or BleAdvertService) based on the user type.
 *
 * @param context: The context in which the receiver is operating.
 * @param intent: The intent containing information about the triggered alarm.
 */

package com.example.goclass.ui.classui.attendances.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.goclass.ui.classui.attendances.service.AttendanceService
import com.example.goclass.ui.classui.attendances.service.BleAdvertService

class AttendanceReceiver : BroadcastReceiver() {
    // Called when the BroadcastReceiver receives an intent.
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        Log.d("atreceiver", "Receiver called")
        if (intent != null) {
            // Extract the action from the intent.
            val action = intent.action
            if (action == "ATTENDANCE_ALARM_ACTION") {
                // Extract attendance details from the intent.
                val userId = intent.getIntExtra("userId", -1)
                val classId = intent.getIntExtra("classId", -1)
                val startHour = intent.getIntExtra("startHour", -1)
                val startMinute = intent.getIntExtra("startMinute", -1)
                val endHour = intent.getIntExtra("endHour", -1)
                val endMinute = intent.getIntExtra("endMinute", -1)
                val userType = intent.getIntExtra("userType", -1)

                // Log extracted details for debugging.
                Log.d("usertypecheck", "$userType")
                Log.d("classIdcheck", "$classId")

                // Determine the user type and start the corresponding service.
                if (userType == 0) {
                    // Create an intent to start the AttendanceService.
                    val serviceIntent = Intent(context, AttendanceService::class.java)
                    serviceIntent.action = "ATTENDANCE_CHECK_ACTION"
                    serviceIntent.putExtra("userId", userId)
                    serviceIntent.putExtra("classId", classId)
                    serviceIntent.putExtra("startHour", startHour)
                    serviceIntent.putExtra("startMinute", startMinute)
                    serviceIntent.putExtra("endHour", endHour)
                    serviceIntent.putExtra("endMinute", endMinute)

                    // Start the AttendanceService to perform the attendance check.
                    context.startService(serviceIntent)
                    Log.d("AttendanceReceiver", "Started AttendanceService")
                } else if (userType == 1) {
                    // Create an intent to start the BleAdvertService.
                    val serviceIntent = Intent(context, BleAdvertService::class.java)
                    serviceIntent.action = "BLE_ADVERT_ACTION"
                    serviceIntent.putExtra("classId", classId)
                    serviceIntent.putExtra("startHour", startHour)
                    serviceIntent.putExtra("startMinute", startMinute)
                    serviceIntent.putExtra("endHour", endHour)
                    serviceIntent.putExtra("endMinute", endMinute)

                    // Start the BleAdvertService to perform the attendance check.
                    context.startService(serviceIntent)
                    Log.d("AttendanceReceiver", "Started BleAdvertService")
                } else {
                    Log.d("AttendanceReceiver", "Error in userType")
                }
            }
        }
    }
}
