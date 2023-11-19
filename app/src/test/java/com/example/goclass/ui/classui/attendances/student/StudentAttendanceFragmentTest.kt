package com.example.goclass.ui.classui.attendances.student

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class StudentAttendanceFragmentTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mock(clazz.java)
    }

    // Some mocked data
    private val userId = 1
    private val classId = 1
    private val className = "TestClassName"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)

        val mockedUserSharedPreferences: SharedPreferences = mock(SharedPreferences::class.java)
        `when`(mockedUserSharedPreferences.getInt("userId", -1)).thenReturn(userId)
        `when`(mockedUserSharedPreferences.getString("className", "")).thenReturn(className)

        val mockedClassSharedPreferences: SharedPreferences = mock(SharedPreferences::class.java)
        `when`(mockedClassSharedPreferences.getInt("classId", -1)).thenReturn(classId)

        loadKoinModules(module {
            viewModel { mock(StudentAttendanceViewModel::class.java) }
            single<SharedPreferences>(qualifier = named("UserPrefs")) { mockedUserSharedPreferences }
            single<SharedPreferences>(qualifier = named("ClassPrefs")) { mockedClassSharedPreferences }
        })

        every { Navigation.findNavController(any()) } returns mockk<NavController>()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
