package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Attendances
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class AttendanceRepositoryTest {
    private lateinit var repository: AttendanceRepository
    private val mockServiceApi: ServiceApi = mockk()

    @Before
    fun setUp() {
        repository = AttendanceRepository(mockServiceApi)
    }

    @Test
    fun attendanceEdit_call() =
        runBlocking {
            val attendanceId = 1

            coEvery { mockServiceApi.attendanceEdit(attendanceId) } returns mockk()

            repository.attendanceEdit(attendanceId)

            coVerify { mockServiceApi.attendanceEdit(attendanceId) }
        }

    @Test
    fun attendanceAdd_call() =
        runBlocking {
            val userId = 1
            val attendance = Attendances(1, 1, 1)

            coEvery { mockServiceApi.attendanceAdd(userId, attendance) } returns mockk()

            repository.attendanceAdd(userId, attendance)

            coVerify { mockServiceApi.attendanceAdd(userId, attendance) }
        }

    @After
    fun tearDown() {
    }
}
