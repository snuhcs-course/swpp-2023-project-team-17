package com.example.goclass.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.network.dataclass.ClassJoinResponse
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.ClassesResponse
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
class ClassRepositoryTest {
    private lateinit var repository: ClassRepository
    private val mockServiceApi: ServiceApi = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
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
    fun classCreate_test() =
        runTest {
            val classes =
                Classes("name", "code")
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.classCreate(classes) } returns mockCodeMessageResponse

            val actualResponse = repository.classCreate(classes)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
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
    fun classJoin_test() =
        runTest {
            val userId = 1
            val classes =
                Classes("name", "code")
            val mockClassJoinResponse =
                ClassJoinResponse(
                    1,
                    "TestTime",
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.classJoin(userId, classes) } returns mockClassJoinResponse

            val actualResponse = repository.classJoin(userId, classes)

            TestCase.assertEquals(mockClassJoinResponse, actualResponse)
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
    fun classGet_test() =
        runTest {
            val classId = 1
            val mockClassesResponse =
                ClassesResponse(
                    "TestName",
                    "TestCode",
                    1,
                    "TestTime",
                    "TestBuildingNumber",
                    "TestRoomNumber",
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.classGet(classId) } returns mockClassesResponse

            val actualResponse = repository.classGet(classId)

            TestCase.assertEquals(mockClassesResponse, actualResponse)
        }

    @Test
    fun classDelete_call() =
        runBlocking {
            val classId = 1

            coEvery { mockServiceApi.classDelete(classId) } returns mockk()

            repository.classDelete(classId)

            coVerify { mockServiceApi.classDelete(classId) }
        }

    @Test
    fun classDelete_test() =
        runTest {
            val classId = 1
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.classDelete(classId) } returns mockCodeMessageResponse

            val actualResponse = repository.classDelete(classId)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
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

    @Test
    fun classGetAttendanceListByUserId_test() =
        runTest {
            val classId = 1
            val userId = 1
            val mockAttendanceListsResponse =
                AttendanceListsResponse(
                    listOf(
                        AttendancesResponse(
                            1234,
                            0,
                            "attendanceDate",
                            0,
                            0,
                            1,
                            1,
                        )
                    ),
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.classGetAttendanceListByUserId(classId, userId) } returns mockAttendanceListsResponse

            val actualResponse = repository.classGetAttendanceListByUserId(classId, userId)

            TestCase.assertEquals(mockAttendanceListsResponse, actualResponse)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
