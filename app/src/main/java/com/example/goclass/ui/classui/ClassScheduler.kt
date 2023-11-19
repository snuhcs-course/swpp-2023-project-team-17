package com.example.goclass.ui.classui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
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
        userType: Int,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AttendanceReceiver::class.java).apply {
            action = "ATTENDANCE_ALARM_ACTION"
            putExtra("userId", userId)
            putExtra("classId", classId)
            putExtra("startHour", startHour)
            putExtra("startMinute", startMinute)
            putExtra("endHour", endHour)
            putExtra("endMinute", endMinute)
            putExtra("userType", userType)
        }

        Log.d("classScheduler", "dayOfWeek: $dayOfWeek")
        Log.d("classScheduler", "classId: $classId")

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                uniqueRequestId(userId, classId),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val now = Calendar.getInstance()
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(now)) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        // Schedule the alarm to repeat weekly
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent,
        )

        Log.d("classScheduler", "알람 설정됨: $dayOfWeek, $startHour:$startMinute")
    }

    private fun uniqueRequestId(
        userId: Int,
        classId: Int,
    ): Int {
        return userId.hashCode() * 31 + classId.hashCode()
    }
}
