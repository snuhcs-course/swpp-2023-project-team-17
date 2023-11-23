package com.example.goclass.ui.mainui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.UsersResponse
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
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private val mockRepository = mockk<UserRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(mockRepository)
    }

    @Test
    fun userEdit_success() =
        runTest {
            val mockResponse = CodeMessageResponse(200, "Message")

            coEvery { mockRepository.userEdit(any(), any()) } returns mockResponse

            viewModel.userEdit(1, 0, "TestName")

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val editSuccess = viewModel.accessEditSuccess().getOrAwaitValue()
            val isLoading = viewModel.accessIsLoading().getOrAwaitValue()
            assertEquals("Success", toastValue)
            assertEquals(true, editSuccess)
            assertEquals(false, isLoading)
        }

    @Test
    fun userEdit_failure() =
        runTest {
            val mockFailureResponse = CodeMessageResponse(400, "Failed to edit profile")

            coEvery { mockRepository.userEdit(any(), any()) } returns mockFailureResponse

            viewModel.userEdit(1, 0, "TestName")

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val editSuccess = viewModel.accessEditSuccess().getOrAwaitValue()
            val isLoading = viewModel.accessIsLoading().getOrAwaitValue()
            assertEquals("Failure", toastValue)
            assertEquals(false, editSuccess)
            assertEquals(false, isLoading)
        }

    @Test
    fun userEdit_exception() =
        runTest {
            val exceptionMessage = "Network error"
            coEvery { mockRepository.userEdit(any(), any()) } throws Exception(exceptionMessage)

            viewModel.userEdit(1, 0, "TestName")

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val editSuccess = viewModel.accessEditSuccess().getOrAwaitValue()
            val isLoading = viewModel.accessIsLoading().getOrAwaitValue()
            assertEquals("Error: $exceptionMessage", toastValue)
            assertEquals(false, editSuccess)
            assertEquals(false, isLoading)
        }

    @Test
    fun userGet_success() =
        runTest {
            val mockUserResponse = UsersResponse("TestEmail", 1, "TestName", 0, 200, "Success")

            coEvery { mockRepository.userGet(any()) } returns mockUserResponse

            viewModel.userGet(1)

            val userName = viewModel.accessUserName().getOrAwaitValue()
            assertEquals(mockUserResponse.userName, userName)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
