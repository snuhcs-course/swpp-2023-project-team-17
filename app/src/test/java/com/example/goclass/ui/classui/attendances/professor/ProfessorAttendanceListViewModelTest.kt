package com.example.goclass.ui.classui.attendances.professor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.UserRepository
import io.mockk.coEvery
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
class ProfessorAttendanceListViewModelTest {
    private lateinit var viewModel: ProfessorAttendanceListViewModel
    private val mockRepository = mockk<UserRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfessorAttendanceListViewModel(mockRepository)
    }

    @Test
    fun getStudentAttendanceList_success() =
        runTest {
            val successMessage = "Success"
            val date = "YYYY-MM-DD"
            val classMap = mapOf("classId" to "1", "userType" to "1")
            val attendancesResponse =
                AttendancesResponse(
                    1234,
                    0,
                    "TestAttendanceDate",
                    0,
                    0,
                    1,
                    "TestStudentName",
                    1,
                    "0011",
                )
            val mockAttendanceListsResponse =
                AttendanceListsResponse(
                    listOf(
                        attendancesResponse,
                    ),
                    200,
                    successMessage,
                )

            // Define the mock behavior
            coEvery { mockRepository.userGetAttendanceListByDate(date, any()) } returns mockAttendanceListsResponse

            // Invoke the function
            viewModel.getStudentAttendanceList(date, classMap)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.studentAttendanceListLiveData.getOrAwaitValue()
            assertEquals(1, liveDataValue.size)
            assertEquals(attendancesResponse, liveDataValue[0])
        }

    @Test
    fun getStudentAttendanceList_failure() =
        runTest {
            val failureMessage = "Failed to refresh: Check network connection."
            val date = "YYYY-MM-DD"
            val classMap = mapOf("classId" to "1", "userType" to "1")
            val mockAttendanceListsResponse =
                AttendanceListsResponse(
                    400,
                    failureMessage,
                )

            // Define the mock behavior
            coEvery { mockRepository.userGetAttendanceListByDate(date, any()) } returns mockAttendanceListsResponse

            // Invoke the function
            viewModel.getStudentAttendanceList(date, classMap)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals(failureMessage, liveDataValue)
        }

    @Test
    fun getStudentAttendanceList_exception() =
        runTest {
            val date = "YYYY-MM-DD"
            val classMap = mapOf("classId" to "1", "userType" to "1")
            val exceptionMessage = "Failed to refresh: Check network connection."
            coEvery { mockRepository.userGetAttendanceListByDate(date, any()) } throws Exception(exceptionMessage)

            viewModel.getStudentAttendanceList(date, classMap)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals(exceptionMessage, toastValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
