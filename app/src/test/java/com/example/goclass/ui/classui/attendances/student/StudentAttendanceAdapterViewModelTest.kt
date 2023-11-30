package com.example.goclass.ui.classui.attendances.student

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.CommentListsResponse
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.repository.ClassRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.Exception

@ExperimentalCoroutinesApi
class StudentAttendanceAdapterViewModelTest {
    private lateinit var viewModel: StudentAttendanceAdapterViewModel
    private val mockRepository = mockk<AttendanceRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StudentAttendanceAdapterViewModel(mockRepository)
    }

    @Test
    fun editAttendance_success() =
        runTest {
            val attendanceId = 1234
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockRepository.attendanceEdit(attendanceId) } returns mockCodeMessageResponse

            viewModel.editAttendance(attendanceId)

            val editSuccess = viewModel.editSuccess.getOrAwaitValue()
            TestCase.assertTrue(editSuccess)
        }

    @Test
    fun editAttendance_failure() =
        runTest {
            val attendanceId = 1234
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    400,
                    "Failure",
                )

            coEvery { mockRepository.attendanceEdit(attendanceId) } returns mockCodeMessageResponse

            viewModel.editAttendance(attendanceId)

            val editSuccess = viewModel.editSuccess.getOrAwaitValue()
            TestCase.assertFalse(editSuccess)
        }

    @Test
    fun editAttendance_exception() =
        runTest {
            val attendanceId = 1234
            val exceptionMessage = "Error"

            coEvery { mockRepository.attendanceEdit(attendanceId) } throws Exception(exceptionMessage)

            viewModel.editAttendance(attendanceId)

            val editSuccess = viewModel.editSuccess.getOrAwaitValue()
            TestCase.assertFalse(editSuccess)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}