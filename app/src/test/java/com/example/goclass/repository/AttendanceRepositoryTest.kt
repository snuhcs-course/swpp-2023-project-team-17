package com.example.goclass.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.network.dataclass.CodeMessageResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AttendanceRepositoryTest {
    private lateinit var repository: AttendanceRepository
    private val mockServiceApi: ServiceApi = mockk()
    private val attendanceId = 1
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = AttendanceRepository(mockServiceApi)
    }

    @Test
    fun attendanceGet_call() =
        runBlocking {
            coEvery { mockServiceApi.attendanceGet(attendanceId) } returns mockk()

            repository.attendanceGet(attendanceId)

            coVerify { mockServiceApi.attendanceGet(attendanceId) }
        }

    @Test
    fun attendanceGet_test() =
        runTest {
            val mockAttendanceResponse =
                AttendancesResponse(
                    1234,
                    0,
                    "attendanceDate",
                    0,
                    0,
                    1,
                    1,
                )

            coEvery { mockServiceApi.attendanceGet(attendanceId) } returns mockAttendanceResponse

            val actualResponse = repository.attendanceGet(attendanceId)

            TestCase.assertEquals(mockAttendanceResponse, actualResponse)
        }

    @Test
    fun attendanceEdit_call() =
        runBlocking {
            coEvery { mockServiceApi.attendanceEdit(attendanceId) } returns mockk()

            repository.attendanceEdit(attendanceId)

            coVerify { mockServiceApi.attendanceEdit(attendanceId) }
        }

    @Test
    fun attendanceEdit_test() =
        runTest {
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.attendanceEdit(attendanceId) } returns mockCodeMessageResponse

            val actualResponse = repository.attendanceEdit(attendanceId)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
        }

    @Test
    fun attendanceDelete_call() =
        runBlocking {
            coEvery { mockServiceApi.attendanceDelete(attendanceId) } returns mockk()

            repository.attendanceDelete(attendanceId)

            coVerify { mockServiceApi.attendanceDelete(attendanceId) }
        }

    @Test
    fun attendanceDelete_test() =
        runTest {
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.attendanceDelete(attendanceId) } returns mockCodeMessageResponse

            val actualResponse = repository.attendanceDelete(attendanceId)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
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

    @Test
    fun attendanceAdd_test() =
        runTest {
            val userId = 1
            val attendance = Attendances(1, 1, 1)

            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.attendanceAdd(userId, attendance) } returns mockCodeMessageResponse

            val actualResponse = repository.attendanceAdd(userId, attendance)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
