package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Classes

class ClassRepository(private val serviceApi: ServiceApi) {
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
}
