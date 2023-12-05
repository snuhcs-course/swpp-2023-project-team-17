package com.example.goclass.ui.classui.attendances

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.AttendanceDateListsResponse
import com.example.goclass.network.dataclass.AttendanceDetailListsResponse
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.repository.UserRepository
import com.example.goclass.ui.classui.attendances.professor.ProfessorAttendanceViewModel
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
    fun getProfessorAttendanceList_success() =
        runTest {
            val attendanceId = 12345
            val successMessage = "Success"
            val attendanceDetailList: List<String> =
                listOf(
                    "0",
                    "1",
                    "1",
                    "0",
                )
            val mockAttendanceDetailListsResponse =
                AttendanceDetailListsResponse(
                    attendanceDetailList,
                    200,
                    successMessage,
                )
            // Define the mock behavior
            coEvery { mockRepository.attendanceDetailListGet(any()) } returns mockAttendanceDetailListsResponse

            // Invoke the function
            viewModel.getAttendanceDetail(attendanceId)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.attendanceDetailListLiveDate.getOrAwaitValue()
            TestCase.assertEquals(4, liveDataValue.size)
            TestCase.assertEquals(attendanceDetailList, liveDataValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}