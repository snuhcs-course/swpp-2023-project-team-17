package com.example.goclass.ui.classui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
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
            Log.d("classScheduler", "endHour: $endHour")
            Log.d("classScheduler", "endMinute: $endMinute")
            Log.d("classScheduler", "startHour: $startHour")
            Log.d("classScheduler", "startMinute: $startMinute")
        }

        Log.d("classScheduler", "dayOfWeek: $dayOfWeek")
        Log.d("classScheduler", "classId: $classId")

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                uniqueRequestId(userId, classId, dayOfWeek, startHour, startMinute),
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !alarmManager.canScheduleExactAlarms()) {
            // 권한 요청을 위한 인텐트 생성 및 전송 필요 (Activity에서 처리)
            // 예를 들어, 권한 요청 인텐트를 Broadcast로 보내고, Activity에서 이를 받아 처리
            val permissionIntent = Intent("com.example.goclass.REQUEST_EXACT_ALARM")
            context.sendBroadcast(permissionIntent)
        } else {
            Log.d("classScheduler", "setexact call")
            // 정확한 알람 설정
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime.timeInMillis,
                    pendingIntent
                )
            }
        }

        Log.d("classScheduler", "알람 설정됨: $dayOfWeek, $startHour:$startMinute")
    }

    fun uniqueRequestId(
        userId: Int,
        classId: Int,
        dayOfWeek: Int,
        startHour: Int,
        startMinute: Int,
    ): Int {
        var result = userId.hashCode()
        result = 31 * result + classId.hashCode()
        result = 31 * result + dayOfWeek
        result = 31 * result + startHour
        result = 31 * result + startMinute
        return result
    }
}
