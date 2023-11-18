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
            val date = "YYYY-MM-DD"
            val classMap = mapOf("classId" to "1", "userType" to "1")
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

            // Define the mock behavior
            coEvery { mockRepository.userGetAttendanceListByDate(date, any()) } returns mockAttendanceListsResponse

            // Invoke the function
            viewModel.getStudentAttendanceList(date, classMap)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.studentAttendanceListLiveData.getOrAwaitValue()
            assertEquals(1, liveDataValue.size)
            assertEquals(1234, liveDataValue[0].attendanceId)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
