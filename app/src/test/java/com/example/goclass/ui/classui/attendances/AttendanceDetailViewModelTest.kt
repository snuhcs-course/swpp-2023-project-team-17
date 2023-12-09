package com.example.goclass.ui.classui.attendances

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
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
class AttendanceDetailViewModelTest {
    private lateinit var viewModel: AttendanceDetailViewModel
    private val mockRepository = mockk<AttendanceRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AttendanceDetailViewModel(mockRepository)
    }

    @Test
    fun getAttendance_success() =
        runTest {
            val attendanceId = 12345
            val successMessage = "Success"
            val mockAttendancesResponse =
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
                    200,
                    successMessage,
                )
            // Define the mock behavior
            coEvery { mockRepository.attendanceGet(any()) } returns mockAttendancesResponse

            // Invoke the function
            viewModel.getAttendance(attendanceId)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.attendanceLiveData.getOrAwaitValue()
            TestCase.assertEquals(mockAttendancesResponse, liveDataValue)
        }

    @Test
    fun getAttendance_failure() =
        runTest {
            val attendanceId = 12345
            val failureMessage = "Error: Database error"
            val mockAttendancesResponse =
                AttendancesResponse(
                    500,
                    failureMessage,
                )

            // Define the mock behavior
            coEvery { mockRepository.attendanceGet(any()) } returns mockAttendancesResponse

            // Invoke the function
            viewModel.getAttendance(attendanceId)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.toastMessage.getOrAwaitValue()
            TestCase.assertEquals(failureMessage, liveDataValue)
        }

    @Test
    fun getAttendance_exception() =
        runTest {
            val attendanceId = 12345
            val exceptionMessage = "Failed to get"

            // Define the mock behavior
            coEvery { mockRepository.attendanceGet(any()) } throws Exception(exceptionMessage)

            // Invoke the function
            viewModel.getAttendance(attendanceId)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.toastMessage.getOrAwaitValue()
            TestCase.assertEquals("Error: $exceptionMessage", liveDataValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}