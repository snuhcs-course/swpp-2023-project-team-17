package com.example.goclass

import com.example.goclass.dataClass.Attendances
import com.example.goclass.dataClass.Classes
import com.example.goclass.dataClass.Messages
import com.example.goclass.dataClass.Users

class Repository(private val serviceApi: ServiceApi) {

    // User related functions
    suspend fun userSignup(users: Users) = serviceApi.userSignup(users)

    suspend fun userLogin(email: String) = serviceApi.userLogin(email)

    suspend fun userLogout() = serviceApi.userLogout()

    suspend fun userGet(userId: Int) = serviceApi.userGet(userId)

    suspend fun userEdit(userId: Int, users: Users) = serviceApi.userEdit(userId, users)

    suspend fun userGetClassList(userMap: Map<String, String>) =
        serviceApi.userGetClassList(userMap)

    suspend fun userGetAttendanceListByDate(
        date: String,
        usersMap: Map<String, String>
    ) = serviceApi.userGetAttendanceListByDate(date, usersMap)

    suspend fun attendanceGetDateList(usersMap: Map<String, String>) =
        serviceApi.attendanceGetDateList(usersMap)

    // Class related functions
    suspend fun classCreate(classes: Classes) = serviceApi.classCreate(classes)

    suspend fun classJoin(userId: Int, classes: Classes) = serviceApi.classJoin(userId, classes)

    suspend fun classGet(classId: Int) = serviceApi.classGet(classId)

    suspend fun classDelete(classId: Int) = serviceApi.classDelete(classId)

    suspend fun classGetChannel(classId: Int, channelType: Int) =
        serviceApi.classGetChannel(classId, channelType)

    suspend fun classGetAttendanceListByUserId(classId: Int, userId: Int) =
        serviceApi.classGetAttendanceListByUserId(classId, userId)

    // Chat Channel related functions
    suspend fun chatChannelGetList(channelId: Int) = serviceApi.chatChannelGetList(channelId)

    suspend fun chatChannelSend(channelId: Int, messages: Messages) =
        serviceApi.chatChannelSend(channelId, messages)

    // Attendance related functions
    suspend fun attendanceGet(attendanceId: Int) = serviceApi.attendanceGet(attendanceId)

    suspend fun attendanceEdit(attendanceId: Int) = serviceApi.attendanceEdit(attendanceId)

    suspend fun attendanceDelete(attendanceId: Int) = serviceApi.attendanceDelete(attendanceId)

    suspend fun attendanceAdd(userId: Int, attendances: Attendances) =
        serviceApi.attendanceAdd(userId, attendances)
}
