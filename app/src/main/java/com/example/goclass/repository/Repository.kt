package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.network.dataclass.Users

class Repository(private val serviceApi: ServiceApi) {
    // User related functions
    suspend fun userLogin(email: String) = serviceApi.userLogin(email)

    suspend fun userGet(userId: Int) = serviceApi.userGet(userId)

    suspend fun userEdit(
        userId: Int,
        users: Users,
    ) = serviceApi.userEdit(userId, users)

    suspend fun userGetClassList(userMap: Map<String, String>) = serviceApi.userGetClassList(userMap)

    suspend fun userGetAttendanceListByDate(
        date: String,
        usersMap: Map<String, String>,
    ) = serviceApi.userGetAttendanceListByDate(date, usersMap)

    suspend fun attendanceGetDateList(usersMap: Map<String, String>) = serviceApi.attendanceGetDateList(usersMap)

    // Class related functions
    suspend fun classCreate(classes: Classes) = serviceApi.classCreate(classes)

    suspend fun classJoin(
        userId: Int,
        classes: Classes,
    ) = serviceApi.classJoin(userId, classes)

    suspend fun classGet(classId: Int) = serviceApi.classGet(classId)

    suspend fun classDelete(classId: Int) = serviceApi.classDelete(classId)

    suspend fun classGetAttendanceListByUserId(
        classId: Int,
        userId: Int,
    ) = serviceApi.classGetAttendanceListByUserId(classId, userId)

    // Chat Channel related functions
    suspend fun chatChannelGetList(
        classId: Int,
        channelType: Int,
    ) = serviceApi.chatChannelGetList(classId, channelType)

    suspend fun chatChannelSend(
        classId: Int,
        channelType: Int,
        messages: Messages,
    ) = serviceApi.chatChannelSend(classId, channelType, messages)

    // Attendance related functions
    suspend fun attendanceGet(attendanceId: Int) = serviceApi.attendanceGet(attendanceId)

    suspend fun attendanceEdit(attendanceId: Int) = serviceApi.attendanceEdit(attendanceId)

    suspend fun attendanceDelete(attendanceId: Int) = serviceApi.attendanceDelete(attendanceId)

    suspend fun attendanceAdd(
        userId: Int,
        attendances: Attendances,
    ) = serviceApi.attendanceAdd(userId, attendances)
}
