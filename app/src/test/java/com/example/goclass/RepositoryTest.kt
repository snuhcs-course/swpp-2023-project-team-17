package com.example.goclass

import com.example.goclass.dataClass.Classes
import com.example.goclass.dataClass.Users
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private lateinit var repository: Repository
    private val mockServiceApi: ServiceApi = mockk()

    @Before
    fun setUp() {
        repository = Repository(mockServiceApi)
    }

    @Test
    fun userGet_call() =
        runBlocking {
            val userId = 1

            coEvery { mockServiceApi.userGet(userId) } returns mockk()

            repository.userGet(userId)

            coVerify { mockServiceApi.userGet(userId) }
        }

    @Test
    fun userEdit_call() =
        runBlocking {
            val userId = 1
            val user = Users(0, "name")

            coEvery { mockServiceApi.userEdit(userId, user) } returns mockk()

            repository.userEdit(userId, user)

            coVerify { mockServiceApi.userEdit(userId, user) }
        }

    @Test
    fun userGetClassList_call() =
        runBlocking {
            val userMap = mapOf("key1" to "value1", "key2" to "value2")

            coEvery { mockServiceApi.userGetClassList(userMap) } returns mockk()

            repository.userGetClassList(userMap)

            coVerify { mockServiceApi.userGetClassList(userMap) }
        }

    @Test
    fun classCreate_call() =
        runBlocking {
            val classes = Classes("name", "code")

            coEvery { mockServiceApi.classCreate(classes) } returns mockk()

            repository.classCreate(classes)

            coVerify { mockServiceApi.classCreate(classes) }
        }

    @Test
    fun classJoin_call() =
        runBlocking {
            val userId = 1
            val classes = Classes("name", "code")

            coEvery { mockServiceApi.classJoin(userId, classes) } returns mockk()

            repository.classJoin(userId, classes)

            coVerify { mockServiceApi.classJoin(userId, classes) }
        }
}
