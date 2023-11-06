package com.example.goclass.mainUi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.Repository.Repository
import com.example.goclass.UI.MainUI.UserMain.StudentMainViewModel
import com.example.goclass.Network.DataClass.ClassListsResponse
import com.example.goclass.Network.DataClass.Classes
import com.example.goclass.Network.DataClass.CodeMessageResponse
import io.mockk.coEvery
import io.mockk.coVerify
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
class StudentMainViewModelTest {
    private lateinit var viewModel: StudentMainViewModel
    private val mockRepository = mockk<Repository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StudentMainViewModel(mockRepository)
    }

    @Test
    fun classJoin_success() =
        runTest {
            val mockResponse = CodeMessageResponse(200, "Message")

            coEvery { mockRepository.classJoin(any(), any()) } returns mockResponse

            viewModel.classJoin(1, "TestName", "TestCode")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Successfully joined!", toastValue)
            coVerify { viewModel.getClassList(mapOf("userId" to "1", "userType" to "0")) }
        }

    @Test
    fun classJoin_failure() =
        runTest {
            val mockFailureResponse = CodeMessageResponse(400, "Failed to join class")

            coEvery { mockRepository.classJoin(any(), any()) } returns mockFailureResponse

            viewModel.classJoin(1, "TestName", "TestCode")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("join failed", toastValue)
        }

    @Test
    fun classJoin_exception() =
        runTest {
            val exceptionMessage = "Network error"
            coEvery { mockRepository.classJoin(any(), any()) } throws Exception(exceptionMessage)

            viewModel.classJoin(1, "TestName", "TestCode")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Error: $exceptionMessage", toastValue)
        }

    @Test
    fun getClassList_success() =
        runTest {
            val mockUserMap = mapOf("userId" to "1", "userType" to "0")
            val mockClassListResponse =
                ClassListsResponse(
                    listOf(
                        Classes(
                            "TestName",
                            "TestCode",
                            1,
                            "TestTime",
                            "TestBuilding",
                            "TestRoom",
                        ),
                    ),
                    200,
                    "Success",
                )

            coEvery { mockRepository.userGetClassList(any()) } returns mockClassListResponse

            viewModel.getClassList(mockUserMap)

            val liveDataValue = viewModel.classListLiveData.getOrAwaitValue()
            assertEquals(1, liveDataValue.size)
            assertEquals("TestName", liveDataValue[0].className)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
