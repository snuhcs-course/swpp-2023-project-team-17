package com.example.goclass.ui.classui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.goclass.ui.classui.attendances.reciever.AttendanceReceiver

class ClassScheduler {
    fun scheduleClass(
        context: Context,
        userId: Int,
        classId: Int,
        dayOfWeek: Int,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AttendanceReceiver::class.java)

        // Pass class info to the receiver
        intent.putExtra("userId", userId)
        intent.putExtra("classId", classId)
        intent.putExtra("startHour", startHour)
        intent.putExtra("startMinute", startMinute)
        intent.putExtra("endHour", endHour)
        intent.putExtra("endMinute", endMinute)

        val pendingIntent = PendingIntent.getBroadcast(context, uniqueRequestId(userId, classId), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val now = Calendar.getInstance()
        val alarmTime = Calendar.getInstance()

        // Set the day of the week, hour, and minute for the alarm
        alarmTime.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        alarmTime.set(Calendar.HOUR_OF_DAY, startHour)
        alarmTime.set(Calendar.MINUTE, startMinute)
        alarmTime.set(Calendar.SECOND, 0)
        alarmTime.set(Calendar.MILLISECOND, 0)

        // If the alarm time is in the past, add a week to it
        if (alarmTime.before(now)) {
            alarmTime.add(Calendar.WEEK_OF_YEAR, 1)
        }

        // Schedule the alarm to repeat weekly
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent,
        )
    }

    private fun uniqueRequestId(
        userId: Int,
        classId: Int,
    ): Int {
        return userId.hashCode() * 31 + classId.hashCode()
    }
}
