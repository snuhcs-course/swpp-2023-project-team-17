package com.example.goclass.mainUi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.Repository
import com.example.goclass.dataClass.ClassListsResponse
import com.example.goclass.dataClass.Classes
import com.example.goclass.dataClass.CodeMessageResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfessorMainViewModelTest {
    private lateinit var viewModel: ProfessorMainViewModel
    private val mockRepository = mockk<Repository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfessorMainViewModel(mockRepository)
    }

    @Test
    fun createClass_success() =
        runTest {
            val mockResponse = CodeMessageResponse(200, "Message")

            coEvery { mockRepository.classCreate(any()) } returns mockResponse

            viewModel.createClass("TestName", "TestCode", 1, "TestTime", "TestBuilding", "TestRoom")

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Successfully created!", toastValue)
            coVerify { viewModel.getClassList(mapOf("userId" to "1", "userType" to "1")) }
        }

    @Test
    fun createClass_failure() =
        runTest {
            // Given a failed response from the repository
            val mockFailureResponse = CodeMessageResponse(400, "Failed to create class") // 400 or any non-success code

            coEvery { mockRepository.classCreate(any()) } returns mockFailureResponse

            // When calling createClass
            viewModel.createClass("TestName", "TestCode", 1, "TestTime", "TestBuilding", "TestRoom")

            // Then we expect a failure message in the toastMessage LiveData
            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("create failed", toastValue) // Adjust this expected value as per your ViewModel's logic
        }

    @Test
    fun createClass_exception() =
        runTest {
            // Given that the repository throws an exception
            val exceptionMessage = "Network error"
            coEvery { mockRepository.classCreate(any()) } throws Exception(exceptionMessage)

            // When calling createClass
            viewModel.createClass("TestName", "TestCode", 1, "TestTime", "TestBuilding", "TestRoom")

            // Then we expect an error message in the toastMessage LiveData
            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Error: $exceptionMessage", toastValue)
        }

    @Test
    fun getClassList_success() =
        runTest {
            val mockUserMap = mapOf("userId" to "1", "userType" to "1")
            val mockClassListResponse =
                ClassListsResponse(
                    listOf(
                        Classes(
                            "TestName",
                            "TestCode",
                            1,
                            "TestTime",
                            "TestBuilding",
                            "TestRoom")),
                    200,
                    "Success",
                )

            // Define the mock behavior
            coEvery { mockRepository.userGetClassList(any()) } returns mockClassListResponse

            // Invoke the function
            viewModel.getClassList(mockUserMap)

            // Check if the LiveData has been updated
            val liveDataValue = viewModel.classListLiveData.getOrAwaitValue()
            assertEquals(1, liveDataValue.size)
            assertEquals("TestName", liveDataValue[0].className)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
