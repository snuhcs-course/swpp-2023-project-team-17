package com.example.goclass.ui.classui.attendances.student

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.repository.ClassRepository
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
class StudentAttendanceViewModelTest {
    private lateinit var viewModel: StudentAttendanceViewModel
    private val mockClassRepository = mockk<ClassRepository>()
    private val mockAttendanceRepository = mockk<AttendanceRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StudentAttendanceViewModel(mockClassRepository, mockAttendanceRepository)
    }

    @Test
    fun getStudentAttendanceList_success() =
        runTest {
            val successMessage = "Success"
            val classId = 1
            val userId = 1
            val attendancesResponse =
                AttendancesResponse(
                    1234,
                    0,
                    "attendanceDate",
                    0,
                    0,
                    1,
                    "TestStudentName",
                    1,
                    "0011"
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
            coEvery { mockClassRepository.classGetAttendanceListByUserId(classId, userId) } returns mockAttendanceListsResponse

            // Invoke the function
            viewModel.getStudentAttendanceList(classId, userId)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.studentAttendanceListLiveData.getOrAwaitValue()
            TestCase.assertEquals(1, liveDataValue.size)
            TestCase.assertEquals(attendancesResponse, liveDataValue[0])
        }

    @Test
    fun getStudentAttendanceList_exception() =
        runTest {
            val classId = 1
            val userId = 1
            val exceptionMessage = "Network error"
            coEvery { mockClassRepository.classGetAttendanceListByUserId(classId, userId) } throws Exception(exceptionMessage)

            viewModel.getStudentAttendanceList(classId, userId)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            TestCase.assertEquals("Error: $exceptionMessage", toastValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}