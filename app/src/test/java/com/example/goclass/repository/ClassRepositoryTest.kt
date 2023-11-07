package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Classes
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class ClassRepositoryTest {
    private lateinit var repository: ClassRepository
    private val mockServiceApi: ServiceApi = mockk()

    @Before
    fun setUp() {
        repository = ClassRepository(mockServiceApi)
    }

    @Test
    fun classCreate_call() =
        runBlocking {
            val classes =
                Classes("name", "code")

            coEvery { mockServiceApi.classCreate(classes) } returns mockk()

            repository.classCreate(classes)

            coVerify { mockServiceApi.classCreate(classes) }
        }

    @Test
    fun classJoin_call() =
        runBlocking {
            val userId = 1
            val classes =
                Classes("name", "code")

            coEvery { mockServiceApi.classJoin(userId, classes) } returns mockk()

            repository.classJoin(userId, classes)

            coVerify { mockServiceApi.classJoin(userId, classes) }
        }

    @Test
    fun classGet_call() =
        runBlocking {
            val classId = 1

            coEvery { mockServiceApi.classGet(classId) } returns mockk()

            repository.classGet(classId)

            coVerify { mockServiceApi.classGet(classId) }
        }

    @Test
    fun classGetAttendanceListByUserId() =
        runBlocking {
            val classId = 1
            val userId = 1

            coEvery { mockServiceApi.classGetAttendanceListByUserId(classId, userId) } returns mockk()

            repository.classGetAttendanceListByUserId(classId, userId)

            coVerify { mockServiceApi.classGetAttendanceListByUserId(classId, userId) }
        }

    @After
    fun tearDown() {
    }
}
