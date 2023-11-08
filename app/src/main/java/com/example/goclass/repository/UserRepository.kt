package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Users

class UserRepository(private val serviceApi: ServiceApi) {
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

    suspend fun attendanceGetDateList(classMap: Map<String, String>) = serviceApi.attendanceGetDateList(classMap)
}
