package com.example.goclass

import android.util.Log
import com.example.goclass.dataClass.Attendances
import com.example.goclass.dataClass.Classes
import com.example.goclass.dataClass.Messages
import com.example.goclass.dataClass.Users

class Repository(private val serviceApi: ServiceApi) {
    // User related functions
    fun userSignup(users: Users) = serviceApi.userSignup(users)
    fun userLogin(email: String) = serviceApi.userLogin(email)
    fun userLogout() = serviceApi.userLogout()
    fun userGet(userId: Int) = serviceApi.userGet(userId)
    fun userEdit(userId: Int, users: Users) = serviceApi.userEdit(userId, users)
    fun userGetClassList(users: Users) = serviceApi.userGetClassList(users)
    fun userGetAttendanceListByDate(date: String, users: Users) = serviceApi.userGetAttendanceListByDate(date, users)
    fun attendanceGetDateList(users: Users) = serviceApi.attendanceGetDateList(users)

    // Class related functions
    fun classCreate(classes: Classes) {
        serviceApi.classCreate(classes)
        Log.d("okhttp", "create")
    }
    fun classJoin(userId: Int, classes: Classes) = serviceApi.classJoin(userId, classes)
    fun classGet(classId: Int) = serviceApi.classGet(classId)
    fun classDelete(classId: Int) = serviceApi.classDelete(classId)
    fun classGetChannel(classId: Int, channelType: Int) = serviceApi.classGetChannel(classId, channelType)
    fun classGetAttendanceListByUserId(classId: Int, userId: Int) = serviceApi.classGetAttendanceListByUserId(classId, userId)

    // Chat Channel related functions
    fun chatChannelGetList(channelId: Int) = serviceApi.chatChannelGetList(channelId)
    fun chatChannelSend(channelId: Int, messages: Messages) = serviceApi.chatChannelSend(channelId, messages)

    // Attendance related functions
    fun attendanceGet(attendanceId: Int) = serviceApi.attendanceGet(attendanceId)
    fun attendanceEdit(attendanceId: Int) = serviceApi.attendanceEdit(attendanceId)
    fun attendanceDelete(attendanceId: Int) = serviceApi.attendanceDelete(attendanceId)
    fun attendanceAdd(userId: Int, attendances: Attendances) = serviceApi.attendanceAdd(userId, attendances)
}
