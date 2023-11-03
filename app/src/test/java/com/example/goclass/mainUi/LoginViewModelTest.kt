package com.example.goclass.mainUi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.Repository
import com.example.goclass.dataClass.UsersResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.getScopeId

class LoginViewModelTest{
    private lateinit var viewModel: LoginViewModel
    private val mockRepository = mockk<Repository>()
    private val testDispatcher = UnconfinedTestDispatcher()
    val userEmail = "test@email.com"

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
            val mockUsersResponse =
                UsersResponse(
                    "email",
                    1,
                    "name",
                    0,
                    200,
                    "message",
                )

            coEvery { mockRepository.userLogin(userEmail) } returns mockUsersResponse

            viewModel.userLogin(userEmail)

            val userId = viewModel.userId.getOrAwaitValue()
            assertEquals(1, userId)
        }

    @Test
    fun userLogin_failure() =
        runTest {
            val mockUsersResponse = UsersResponse(
                400,
                "Login failed"
            )

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
