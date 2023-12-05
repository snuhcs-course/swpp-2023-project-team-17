package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Attendances

class AttendanceRepository(private val serviceApi: ServiceApi) {
    suspend fun attendanceGet(attendanceId: Int) = serviceApi.attendanceGet(attendanceId)

    suspend fun attendanceEdit(attendanceId: Int) = serviceApi.attendanceEdit(attendanceId)

    suspend fun attendanceDelete(attendanceId: Int) = serviceApi.attendanceDelete(attendanceId)

    suspend fun attendanceAdd(
        userId: Int,
        attendances: Attendances,
    ) = serviceApi.attendanceAdd(userId, attendances)
}
