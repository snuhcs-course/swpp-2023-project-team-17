package com.example.goclass.ui.classui.attendances.student

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.AttendancesResponse
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
            val mockClassId = 1
            val mockUserId = 1
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
            coEvery { mockClassRepository.classGetAttendanceListByUserId(mockClassId, mockUserId) } returns mockAttendanceListsResponse

            // Invoke the function
            viewModel.getStudentAttendanceList(mockClassId, mockUserId)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.studentAttendanceListLiveData.getOrAwaitValue()
            TestCase.assertEquals(1, liveDataValue.size)
            TestCase.assertEquals(1234, liveDataValue[0].attendanceId)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}