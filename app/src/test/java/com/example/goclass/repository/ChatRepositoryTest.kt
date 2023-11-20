package com.example.goclass.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.network.ServiceApi
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.CommentListsResponse
import com.example.goclass.network.dataclass.Comments
import com.example.goclass.network.dataclass.CommentsResponse
import com.example.goclass.network.dataclass.MessageListsResponse
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.network.dataclass.MessagesResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChatRepositoryTest {
    private lateinit var repository: ChatRepository
    private val mockServiceApi: ServiceApi = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = ChatRepository(mockServiceApi)
    }

    @Test
    fun chatChannelGetList_call() =
        runBlocking {
            val classId = 1
            coEvery { mockServiceApi.chatChannelGetList(classId) } returns mockk()

            repository.chatChannelGetList(classId)

            coVerify { mockServiceApi.chatChannelGetList(classId) }
        }

    @Test
    fun chatChannelGetList_test() =
        runTest {
            val classId = 1
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(
                        MessagesResponse(
                            1,
                            1,
                            1,
                            "YYYY-MM-DD",
                            1,
                            "TestName",
                            "TestContent",
                        )
                    ),
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.chatChannelGetList(classId) } returns mockMessageListsResponse

            val actualResponse = repository.chatChannelGetList(classId)

            TestCase.assertEquals(mockMessageListsResponse, actualResponse)
        }

    @Test
    fun chatChannelSend_call() =
        runBlocking {
            val classId = 1
            val messages = Messages(1, "TestMsg")
            coEvery { mockServiceApi.chatChannelSend(classId, messages) } returns mockk()

            repository.chatChannelSend(classId, messages)

            coVerify { mockServiceApi.chatChannelSend(classId, messages) }
        }

    @Test
    fun chatChannelSend_test() =
        runTest {
            val classId = 1
            val messages = Messages(1, "TestMsg")
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.chatChannelSend(classId, messages) } returns mockCodeMessageResponse

            val actualResponse = repository.chatChannelSend(classId, messages)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
        }

    @Test
    fun chatChannelEdit_call() =
        runBlocking {
            val classId = 1
            val messages = Messages(1, "TestMsg")
            coEvery { mockServiceApi.chatChannelEdit(classId, messages) } returns mockk()

            repository.chatChannelEdit(classId, messages)

            coVerify { mockServiceApi.chatChannelEdit(classId, messages) }
        }

    @Test
    fun chatChannelEdit_test() =
        runTest {
            val classId = 1
            val messages = Messages(1, "TestMsg")
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.chatChannelEdit(classId, messages) } returns mockCodeMessageResponse

            val actualResponse = repository.chatChannelEdit(classId, messages)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
        }

    @Test
    fun chatCommentGetList_call() =
        runBlocking {
            val classId = 1
            val id = 1
            coEvery { mockServiceApi.chatCommentGetList(classId, id) } returns mockk()

            repository.chatCommentGetList(classId, id)

            coVerify { mockServiceApi.chatCommentGetList(classId, id) }
        }

    @Test
    fun chatCommentGetList_test() =
        runTest {
            val classId = 1
            val id = 1
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(
                        CommentsResponse(
                            1,
                            1,
                            1,
                            "YYYY-MM-DD",
                            1,
                            "TestName",
                            "TestContent",
                        )
                    ),
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.chatCommentGetList(classId, id) } returns mockCommentListsResponse

            val actualResponse = repository.chatCommentGetList(classId, id)

            TestCase.assertEquals(mockCommentListsResponse, actualResponse)
        }

    @Test
    fun chatCommentSend_call() =
        runBlocking {
            val classId = 1
            val userId = 1
            val id = 1
            val content = "TestContent"
            val comments = Comments(userId, content)
            coEvery { mockServiceApi.chatCommentSend(classId, id, comments) } returns mockk()

            repository.chatCommentSend(classId, id, comments)

            coVerify { mockServiceApi.chatCommentSend(classId, id, comments) }
        }

    @Test
    fun chatCommentSend_test() =
        runTest {
            val classId = 1
            val userId = 1
            val id = 1
            val content = "TestContent"
            val comments = Comments(userId, content)
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.chatCommentSend(classId, id, comments) } returns mockCodeMessageResponse

            val actualResponse = repository.chatCommentSend(classId, id, comments)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
        }

    @Test
    fun chatCommentEdit_call() =
        runBlocking {
            val classId = 1
            val userId = 1
            val id = 1
            val content = "TestContent"
            val comments = Comments(userId, content)
            coEvery { mockServiceApi.chatCommentEdit(classId, id, comments) } returns mockk()

            repository.chatCommentEdit(classId, id, comments)

            coVerify { mockServiceApi.chatCommentEdit(classId, id, comments) }
        }

    @Test
    fun chatCommentEdit_test() =
        runTest {
            val classId = 1
            val userId = 1
            val id = 1
            val content = "TestContent"
            val comments = Comments(userId, content)
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )

            coEvery { mockServiceApi.chatCommentEdit(classId, id, comments) } returns mockCodeMessageResponse

            val actualResponse = repository.chatCommentEdit(classId, id, comments)

            TestCase.assertEquals(mockCodeMessageResponse, actualResponse)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
