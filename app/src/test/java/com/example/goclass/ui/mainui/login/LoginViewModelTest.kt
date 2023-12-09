package com.example.goclass.ui.mainui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.UsersResponse
import com.example.goclass.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
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
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val mockRepository = mockk<UserRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val userEmail = "test@email.com"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(mockRepository)
    }

    @Test
    fun userLogin_success() =
        runTest {
            val successMessage = "Success"
            val mockUsersResponse =
                UsersResponse(
                    "TestEmail",
                    1,
                    "TestName",
                    0,
                    200,
                    successMessage,
                )

            coEvery { mockRepository.userLogin(userEmail) } returns mockUsersResponse

            viewModel.userLogin(userEmail)

            val userId = viewModel.userId.getOrAwaitValue()
            assertEquals(1, userId)
        }

    @Test
    fun userLogin_failure() =
        runTest {
            val failureMessage = "Failure"
            val mockUsersResponse = UsersResponse(400, failureMessage)

            coEvery { mockRepository.userLogin(userEmail) } returns mockUsersResponse

            viewModel.userLogin(userEmail)

            val userId = viewModel.userId.getOrAwaitValue()
            assertNull(userId)
        }

    @Test
    fun userLogin_exception() =
        runTest {
            val exceptionMessage = "Network error"

            coEvery { mockRepository.userLogin(userEmail) } throws Exception(exceptionMessage)

            viewModel.userLogin(userEmail)

            val userIdValue = viewModel.userId.getOrAwaitValue()
            assertNull(userIdValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
