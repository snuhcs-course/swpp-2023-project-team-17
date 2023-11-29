package com.example.goclass.ui.classui.attendances.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.repository.AttendanceRepository
import com.example.goclass.ui.classui.chats.ChatCommentViewModel
import io.mockk.mockk
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
class AttendanceServiceTest {
    private val service = AttendanceService()
    private val mockRepository = mockk<AttendanceRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun a() =
        runTest {

        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}