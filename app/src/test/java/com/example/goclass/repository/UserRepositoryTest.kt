package com.example.goclass.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.AttendanceDateListsResponse
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.network.dataclass.ClassListsResponse
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.Users
import com.example.goclass.network.dataclass.UsersResponse
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
class UserRepositoryTest {
    private lateinit var repository: UserRepository
    private val mockServiceApi: ServiceApi = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
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
    fun userLogin_success() =
        runTest {
            val userEmail = "a@snu.ac.kr"
            val mockUsersResponse =
                UsersResponse(
                    "TestEmail",
                    1,
                    "TestName",
                    0,
                    200,
                    "message",
                )

            coEvery { mockServiceApi.userLogin(userEmail) } returns mockUsersResponse

            val actualResponse = repository.userLogin(userEmail)

            TestCase.assertEquals(mockUsersResponse, actualResponse)
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
    fun userGet_success() =
        runTest {
            val userId = 1
            val mockUsersResponse =
                UsersResponse(
                    "TestEmail",
                    1,
                    "TestName",
                    0,
                    200,
                    "message",
                )

            coEvery { mockServiceApi.userGet(userId) } returns mockUsersResponse

            val actualResponse = repository.userGet(userId)

            TestCase.assertEquals(mockUsersResponse, actualResponse)
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
    fun userEdit_success() =
        runTest {
            val userId = 1
            val user = Users(0, "name")
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.userEdit(userId, user) } returns mockCodeMessageResponse

            val actualResponse = repository.userEdit(userId, user)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
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
    fun userGetClassList_success() =
        runTest {
            val userMap = mapOf("key1" to "value1", "key2" to "value2")
            val mockClassListsResponse =
                ClassListsResponse(
                    listOf(
                        ClassesResponse(
                            "TestName",
                            "TestCode",
                            1,
                            "TestTime",
                            "TestBuilding",
                            "TestRoom",
                        ),
                    ),
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.userGetClassList(userMap) } returns mockClassListsResponse

            val actualResponse = repository.userGetClassList(userMap)

            TestCase.assertEquals(mockClassListsResponse, actualResponse)
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
    fun userGetAttendanceListByDate_success() =
        runTest {
            val date = "YYYY-MM-DD"
            val userMap = mapOf("key1" to "value1", "key2" to "value2")
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

            coEvery { mockServiceApi.userGetAttendanceListByDate(date, userMap) } returns mockAttendanceListsResponse

            val actualResponse = repository.userGetAttendanceListByDate(date, userMap)

            TestCase.assertEquals(mockAttendanceListsResponse, actualResponse)
        }

    @Test
    fun attendanceGetDateList_call() =
        runBlocking {
            val classMap = mapOf("key1" to "value1", "key2" to "value2")

            coEvery { mockServiceApi.attendanceGetDateList(classMap) } returns mockk()

            repository.attendanceGetDateList(classMap)

            coVerify { mockServiceApi.attendanceGetDateList(classMap) }
        }

    @Test
    fun attendanceGetDateList_success() =
        runTest {
            val classMap = mapOf("key1" to "value1", "key2" to "value2")
            val mockAttendanceDateListsResponse =
                AttendanceDateListsResponse(
                    listOf(
                        AttendancesResponse(
                            "attendanceDate",
                        )
                    ),
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.attendanceGetDateList(classMap) } returns mockAttendanceDateListsResponse

            val actualResponse = repository.attendanceGetDateList(classMap)

            TestCase.assertEquals(mockAttendanceDateListsResponse, actualResponse)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
