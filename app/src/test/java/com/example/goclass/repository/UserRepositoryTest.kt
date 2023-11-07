package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Users
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    private lateinit var repository: UserRepository
    private val mockServiceApi: ServiceApi = mockk()

    @Before
    fun setUp() {
        repository = UserRepository(mockServiceApi)
    }

    @Test
    fun userLogin_call() =
        runBlocking {
            val userEmail = "a@snu.ac.kr"

            coEvery { mockServiceApi.userLogin(userEmail) } returns mockk()

            repository.userLogin(userEmail)

            coVerify { mockServiceApi.userLogin(userEmail) }
        }

    @Test
    fun userGet_call() =
        runBlocking {
            val userId = 1

            coEvery { mockServiceApi.userGet(userId) } returns mockk()

            repository.userGet(userId)

            coVerify { mockServiceApi.userGet(userId) }
        }

    @Test
    fun userEdit_call() =
        runBlocking {
            val userId = 1
            val user = Users(0, "name")

            coEvery { mockServiceApi.userEdit(userId, user) } returns mockk()

            repository.userEdit(userId, user)

            coVerify { mockServiceApi.userEdit(userId, user) }
        }

    @Test
    fun userGetClassList_call() =
        runBlocking {
            val userMap = mapOf("key1" to "value1", "key2" to "value2")

            coEvery { mockServiceApi.userGetClassList(userMap) } returns mockk()

            repository.userGetClassList(userMap)

            coVerify { mockServiceApi.userGetClassList(userMap) }
        }

    @Test
    fun userGetAttendanceListByDate_call() =
        runBlocking {
            val date = "YYYY-MM-DD"
            val userMap = mapOf("key1" to "value1", "key2" to "value2")

            coEvery { mockServiceApi.userGetAttendanceListByDate(date, userMap) } returns mockk()

            repository.userGetAttendanceListByDate(date, userMap)

            coVerify { mockServiceApi.userGetAttendanceListByDate(date, userMap) }
        }

    @Test
    fun attendanceGetDateList_call() =
        runBlocking {
            val userMap = mapOf("key1" to "value1", "key2" to "value2")

            coEvery { mockServiceApi.attendanceGetDateList(userMap) } returns mockk()

            repository.attendanceGetDateList(userMap)

            coVerify { mockServiceApi.attendanceGetDateList(userMap) }
        }

    @After
    fun tearDown() {
    }
}
