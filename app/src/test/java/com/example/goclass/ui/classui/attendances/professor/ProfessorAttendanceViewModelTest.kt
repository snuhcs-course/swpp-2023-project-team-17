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
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
            val classMap = mapOf("classId" to "1", "userType" to "1")
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

            // Define the mock behavior
            coEvery { mockRepository.attendanceGetDateList(any()) } returns mockAttendanceDateListsResponse

            // Invoke the function
            viewModel.getProfessorAttendanceList(classMap)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.professorAttendanceListLiveData.getOrAwaitValue()
            assertEquals(1, liveDataValue.size)
            assertEquals("attendanceDate", liveDataValue[0].attendanceDate)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
