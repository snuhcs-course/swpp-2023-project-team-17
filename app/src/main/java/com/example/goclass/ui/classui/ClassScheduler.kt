/*
 * ClassScheduler is a utility class responsible for scheduling class attendance checks in the GoClass app.
 * It uses the Android AlarmManager to trigger AttendanceReceiver at specified class times.
 *
 * @param context: The context in which the alarm is scheduled.
 * @param userId: User ID associated with the class attendance.
 * @param classId: Class ID for uniquely identifying the class.
 * @param dayOfWeek: Day of the week when the class occurs (Calendar.DAY_OF_WEEK format).
 * @param startHour: Starting hour of the class.
 * @param startMinute: Starting minute of the class.
 * @param endHour: Ending hour of the class.
 * @param endMinute: Ending minute of the class.
 * @param userType: User type for distinguishing between different users (students, professors).
 */

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
    // Schedule a class attendance alarm based on provided parameters.
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
        // Get the AlarmManager service.
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an intent to be triggered by the alarm, passing necessary information.
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

        // Create a unique PendingIntent using class parameters for later retrieval and modification.
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                uniqueRequestId(userId, classId, dayOfWeek, startHour, startMinute),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        // Get the current time and set the alarm time to the specified class time.
        val now = Calendar.getInstance()
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If the specified time is in the past, set the alarm for the next week.
            if (before(now)) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        // Schedule the alarm to repeat weekly.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !alarmManager.canScheduleExactAlarms()) {
            // Request permission for exact alarm scheduling (handled in the activity).
            val permissionIntent = Intent("com.example.goclass.REQUEST_EXACT_ALARM")
            context.sendBroadcast(permissionIntent)
        } else {
            // Set the exact alarm based on the Android version.
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

        // Log the scheduled alarm details.
        Log.d("classScheduler", "Alarm scheduled: $dayOfWeek, $startHour:$startMinute")
    }

    // Generate a unique request ID based on class parameters.
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
