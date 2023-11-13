package com.example.goclass.ui.mainui.usermain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.ClassListsResponse
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.repository.ClassRepository
import com.example.goclass.repository.UserRepository
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
class ProfessorMainViewModelTest {
    private lateinit var viewModel: ProfessorMainViewModel
    private val mockUserRepository = mockk<UserRepository>()
    private val mockClassRepository = mockk<ClassRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfessorMainViewModel(mockUserRepository, mockClassRepository)
    }

    @Test
    fun createClass_success() =
        runTest {
            val mockResponse = CodeMessageResponse(200, "Message")

            coEvery { mockClassRepository.classCreate(any()) } returns mockResponse

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

            coEvery { mockClassRepository.classCreate(any()) } returns mockFailureResponse

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
            coEvery { mockClassRepository.classCreate(any()) } throws Exception(exceptionMessage)

            // When calling createClass
            viewModel.createClass("TestName", "TestCode", 1, "TestTime", "TestBuilding", "TestRoom")

            // Then we expect an error message in the toastMessage LiveData
            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            assertEquals("Error: $exceptionMessage", toastValue)
        }

    @Test
    fun getClassList_success() =
        runTest {
            val userMap = mapOf("userId" to "1", "userType" to "1")
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

            // Define the mock behavior
            coEvery { mockUserRepository.userGetClassList(any()) } returns mockClassListResponse

            // Invoke the function
            viewModel.getClassList(userMap)

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
