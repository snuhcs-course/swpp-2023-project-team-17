package com.example.goclass.ui.mainui.usermain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.GoClassApplication
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.ClassJoinResponse
import com.example.goclass.network.dataclass.ClassListsResponse
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.repository.ClassRepository
import com.example.goclass.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
            val mockResponse = ClassJoinResponse(1, "TestTime", 200, "Message")

            coEvery { mockClassRepository.classJoin(any(), any()) } returns mockResponse

            viewModel.classJoin(1, "TestName", "TestCode")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Successfully joined!", toastValue)
            coVerify { viewModel.getClassList(mapOf("userId" to "1", "userType" to "0")) }
        }

    @Test
    fun classJoin_failure() =
        runTest {
            val mockFailureResponse = ClassJoinResponse(1, "TestTime", 400, "Failed to join class")

            coEvery { mockClassRepository.classJoin(any(), any()) } returns mockFailureResponse

            viewModel.classJoin(1, "TestName", "TestCode")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("join failed", toastValue)
        }

    @Test
    fun classJoin_exception() =
        runTest {
            val exceptionMessage = "Network error"
            coEvery { mockClassRepository.classJoin(any(), any()) } throws Exception(exceptionMessage)

            viewModel.classJoin(1, "TestName", "TestCode")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Error: $exceptionMessage", toastValue)
        }

    @Test
    fun classJoin_success_time_match() =
        runTest {
            //val classTime = "1 15:30-16:45"
            val classTime = ""
            val mockResponse = ClassJoinResponse(1, classTime, 200, "Message")

            coEvery { mockClassRepository.classJoin(any(), any()) } returns mockResponse

            viewModel.classJoin(1, "TestName", "TestCode")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Successfully joined!", toastValue)
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
