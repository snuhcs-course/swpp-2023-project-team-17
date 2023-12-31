package com.example.goclass.ui.mainui.usermain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.GoClassApplication
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.ClassJoinResponse
import com.example.goclass.network.dataclass.ClassListsResponse
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.repository.ClassRepository
import com.example.goclass.repository.UserRepository
import com.example.goclass.ui.classui.ClassScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StudentMainViewModelTest {
    private lateinit var viewModel: StudentMainViewModel
    private val mockUserRepository = mockk<UserRepository>()
    private val mockClassRepository = mockk<ClassRepository>()
    private val mockApplication = mockk<GoClassApplication>()
    private val mockClassScheduler = mockk<ClassScheduler>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StudentMainViewModel(mockUserRepository, mockClassRepository, mockApplication)
    }

    @Test
    fun classJoin_success() =
        runTest {
            val successMessage = "Successfully joined!"
            val mockClassJoinResponse = ClassJoinResponse(1, "TestTime", 200, successMessage)
            val mockClassListsResponse = ClassListsResponse(listOf(), 200, "Success")

            coEvery { mockClassRepository.classJoin(any(), any()) } returns mockClassJoinResponse
            coEvery { mockUserRepository.userGetClassList(any()) } returns mockClassListsResponse

            viewModel.classJoin(1, "TestName", "TestCode", mockClassScheduler)

            val snackbarValue = viewModel.snackbarMessage.getOrAwaitValue()
            assertEquals(successMessage, snackbarValue)
            coVerify { viewModel.getClassList(mapOf("userId" to "1", "userType" to "0")) }
        }

    @Test
    fun classJoin_failure() =
        runTest {
            val failureMessage = "Failed to join: Check class name or class code again."
            val mockClassJoinResponse = ClassJoinResponse(400, failureMessage)

            coEvery { mockClassRepository.classJoin(any(), any()) } returns mockClassJoinResponse

            viewModel.classJoin(1, "TestName", "TestCode", mockClassScheduler)

            val snackbarValue = viewModel.snackbarMessage.getOrAwaitValue()
            assertEquals(failureMessage, snackbarValue)
        }

    @Test
    fun classJoin_exception() =
        runTest {
            val exceptionMessage = "Failed to join: Check class name or class code again."
            coEvery { mockClassRepository.classJoin(any(), any()) } throws Exception(exceptionMessage)

            viewModel.classJoin(1, "TestName", "TestCode", mockClassScheduler)

            val snackbarValue = viewModel.snackbarMessage.getOrAwaitValue()
            assertEquals(exceptionMessage, snackbarValue)
        }

    @Test
    fun classJoin_success_time_match() =
        runTest {
            val successMessage = "Successfully joined!"
            val classTime = "1 15:30-16:45"
            val mockClassJoinResponse = ClassJoinResponse(1, classTime, 200, successMessage)
            val mockClassListsResponse = ClassListsResponse(listOf(), 200, "Success")

            coEvery { mockClassRepository.classJoin(any(), any()) } returns mockClassJoinResponse
            coEvery { mockUserRepository.userGetClassList(any()) } returns mockClassListsResponse
            every {
                mockClassScheduler.scheduleClass(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns Unit

            viewModel.classJoin(1, "TestName", "TestCode", mockClassScheduler)

            val snackbarValue = viewModel.snackbarMessage.getOrAwaitValue()
            assertEquals(successMessage, snackbarValue)
            coVerify { viewModel.getClassList(mapOf("userId" to "1", "userType" to "0")) }
        }

    @Test
    fun getClassList_success() =
        runTest {
            val userMap = mapOf("userId" to "1", "userType" to "0")
            val classesResponse =
                ClassesResponse(
                    1,
                    "TestName",
                    "TestCode",
                    1,
                    "TestTime",
                    "TestBuilding",
                    "TestRoom",
                )
            val mockClassListsResponse =
                ClassListsResponse(
                    listOf(
                        classesResponse,
                    ),
                    200,
                    "Success",
                )

            coEvery { mockUserRepository.userGetClassList(any()) } returns mockClassListsResponse

            viewModel.getClassList(userMap)

            val liveDataValue = viewModel.classListLiveData.getOrAwaitValue()
            assertEquals(1, liveDataValue.size)
            assertEquals(classesResponse, liveDataValue[0])
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
