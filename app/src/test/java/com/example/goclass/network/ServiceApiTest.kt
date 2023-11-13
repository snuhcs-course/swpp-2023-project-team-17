package com.example.goclass.network

import com.example.goclass.network.dataclass.UsersResponse
import io.mockk.coEvery
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ServiceApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ServiceApi
    val userEmail = "test@email.com"

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ServiceApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}