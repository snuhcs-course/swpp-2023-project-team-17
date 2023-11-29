package com.example.goclass.ui.classui.attendances.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.CodeMessageResponse
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
class AttendanceServiceViewModelTest {
    private lateinit var viewModel: AttendanceServiceViewModel
    private val mockRepository = mockk<AttendanceRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AttendanceServiceViewModel(mockRepository)
    }

    @Test
    fun saveAttendance_success() =
        runTest {
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockRepository.attendanceAdd(any(), any()) } returns mockCodeMessageResponse

            viewModel.saveAttendance(0, 0, 1, 1)

            val liveDataValue = viewModel.addSuccess.getOrAwaitValue()
            TestCase.assertTrue(liveDataValue)
        }

    @Test
    fun saveAttendance_failure() =
        runTest {
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    400,
                    "Failure",
                )

            coEvery { mockRepository.attendanceAdd(any(), any()) } returns mockCodeMessageResponse

            viewModel.saveAttendance(0, 0, 1, 1)

            val liveDataValue = viewModel.addSuccess.getOrAwaitValue()
            TestCase.assertFalse(liveDataValue)
        }

    @Test
    fun saveAttendance_exception() =
        runTest {
            val exceptionMessage = "AttendanceSaveError"

            coEvery { mockRepository.attendanceAdd(any(), any()) } throws Exception(exceptionMessage)

            viewModel.saveAttendance(0, 0, 1, 1)

            val liveDataValue = viewModel.addSuccess.getOrAwaitValue()
            TestCase.assertFalse(liveDataValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}
