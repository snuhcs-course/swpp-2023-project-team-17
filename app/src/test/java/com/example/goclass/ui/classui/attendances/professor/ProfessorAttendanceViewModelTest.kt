package com.example.goclass.ui.classui.attendances.professor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.AttendanceDateListsResponse
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
class ProfessorAttendanceViewModelTest {
    private lateinit var viewModel: ProfessorAttendanceViewModel
    private val mockRepository = mockk<UserRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfessorAttendanceViewModel(mockRepository)
    }

    @Test
    fun getProfessorAttendanceList_success() =
        runTest {
            val successMessage = "Success"
            val classMap = mapOf("classId" to "1", "userType" to "1")
            val attendancesResponse =
                AttendancesResponse(
                    "attendanceDate",
                )
            val mockAttendanceDateListsResponse =
                AttendanceDateListsResponse(
                    listOf(
                        attendancesResponse,
                    ),
                    200,
                    successMessage,
                )

            // Define the mock behavior
            coEvery { mockRepository.attendanceGetDateList(any()) } returns mockAttendanceDateListsResponse

            // Invoke the function
            viewModel.getProfessorAttendanceList(classMap)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.professorAttendanceListLiveData.getOrAwaitValue()
            assertEquals(1, liveDataValue.size)
            assertEquals(attendancesResponse, liveDataValue[0])
        }

    @Test
    fun getProfessorAttendanceList_exception() =
        runTest {
            val classMap = mapOf("classId" to "1", "userType" to "1")
            val exceptionMessage = "Network error"
            coEvery { mockRepository.attendanceGetDateList(any()) } throws Exception(exceptionMessage)

            viewModel.getProfessorAttendanceList(classMap)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Error: $exceptionMessage", toastValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
