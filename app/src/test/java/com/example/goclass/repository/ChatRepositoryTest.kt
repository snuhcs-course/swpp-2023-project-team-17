package com.example.goclass.repository

import com.example.goclass.network.ServiceApi
import io.mockk.mockk
import org.junit.After
import org.junit.Before

class ChatRepositoryTest {
    private lateinit var repository: ChatRepository
    private val mockServiceApi: ServiceApi = mockk()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }
}
