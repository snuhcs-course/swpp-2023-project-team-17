package com.example.goclass.ui.classui.chats.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.CommentListsResponse
import com.example.goclass.network.dataclass.CommentsResponse
import com.example.goclass.repository.ChatRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
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
class ChatCommentViewModelTest {
    private lateinit var viewModel: ChatCommentViewModel
    private val mockChatRepository = mockk<ChatRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChatCommentViewModel(mockChatRepository)
    }
    @Test
    fun chatCommentSend_success() =
        runTest {
            val classId = 1
            val userId = 1
            val id = 1
            val content = "TestContent"
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse
            coEvery { mockChatRepository.chatCommentSend(any(), any(), any()) } returns mockCodeMessageResponse

            viewModel.chatCommentSend(classId, id, userId, content)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val sendSuccess = viewModel.accessSendSuccess().getOrAwaitValue()
            TestCase.assertEquals("Success", toastValue)
            TestCase.assertEquals(true, sendSuccess)
        }

    @Test
    fun chatCommentSend_failure() =
        runTest {
            val classId = 1
            val userId = 1
            val id = 1
            val content = "TestContent"
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    400,
                    "Failure",
                )
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse
            coEvery { mockChatRepository.chatCommentSend(any(), any(), any()) } returns mockCodeMessageResponse

            viewModel.chatCommentSend(classId, id, userId, content)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val sendSuccess = viewModel.accessSendSuccess().getOrAwaitValue()
            TestCase.assertEquals("Failure", toastValue)
            TestCase.assertEquals(false, sendSuccess)
        }

    @Test
    fun chatCommentSend_exception() =
        runTest {
            val classId = 1
            val userId = 1
            val id = 1
            val content = "TestContent"
            val exceptionMessage = "Network error"
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse
            coEvery { mockChatRepository.chatCommentSend(any(), any(), any()) } throws Exception(exceptionMessage)

            viewModel.chatCommentSend(classId, id, userId, content)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val sendSuccess = viewModel.accessSendSuccess().getOrAwaitValue()
            TestCase.assertEquals("Error: $exceptionMessage", toastValue)
            TestCase.assertEquals(false, sendSuccess)
        }

    @Test
    fun chatCommentEdit_success() =
        runTest {
            val classId = 1
            val content = "TestContent"
            val commentId = 1
            val messageId = 1
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse
            coEvery { mockChatRepository.chatCommentEdit(any(), any(), any()) } returns mockCodeMessageResponse

            viewModel.chatCommentEdit(classId, content, commentId, messageId)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val editSuccess = viewModel.accessEditSuccess().getOrAwaitValue()
            TestCase.assertEquals("Success", toastValue)
            TestCase.assertEquals(true, editSuccess)
        }

    @Test
    fun chatCommentEdit_failure() =
        runTest {
            val classId = 1
            val content = "TestContent"
            val commentId = 1
            val messageId = 1
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    400,
                    "Failure",
                )
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse
            coEvery { mockChatRepository.chatCommentEdit(any(), any(), any()) } returns mockCodeMessageResponse

            viewModel.chatCommentEdit(classId, content, commentId, messageId)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val editSuccess = viewModel.accessEditSuccess().getOrAwaitValue()
            TestCase.assertEquals("Failure", toastValue)
            TestCase.assertEquals(false, editSuccess)
        }

    @Test
    fun chatCommentEdit_exception() =
        runTest {
            val classId = 1
            val content = "TestContent"
            val commentId = 1
            val messageId = 1
            val exceptionMessage = "Network error"
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse
            coEvery { mockChatRepository.chatCommentEdit(any(), any(), any()) } throws Exception(exceptionMessage)

            viewModel.chatCommentEdit(classId, content, commentId, messageId)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            val editSuccess = viewModel.accessEditSuccess().getOrAwaitValue()
            TestCase.assertEquals("Error: $exceptionMessage", toastValue)
            TestCase.assertEquals(false, editSuccess)
        }

    @Test
    fun chatCommentGetList_success() =
        runTest {
            val classId = 1
            val commentId = 1
            val commentsResponse =
                CommentsResponse(
                    1,
                    1,
                    1,
                    "TestTime",
                    1,
                    "TestName",
                    "TestContent",
                )
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(
                        commentsResponse,
                    ),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse

            viewModel.chatCommentGetList(classId, commentId)

            val liveDataValue = viewModel.accessCommentListLiveData().getOrAwaitValue()
            TestCase.assertEquals(1, liveDataValue.size)
            TestCase.assertEquals(commentsResponse, liveDataValue[0])
        }

    @Test
    fun chatCommentGetList_failure() =
        runTest {
            val classId = 1
            val commentId = 1
            val mockCommentListsResponse =
                CommentListsResponse(
                    listOf(),
                    400,
                    "Failure",
                )

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } returns mockCommentListsResponse

            viewModel.chatCommentGetList(classId, commentId)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            TestCase.assertEquals("Failure", toastValue)
        }

    @Test
    fun chatCommentGetList_exception() =
        runTest {
            val classId = 1
            val commentId = 1
            val exceptionMessage = "Network error"

            coEvery { mockChatRepository.chatCommentGetList(any(), any()) } throws Exception(exceptionMessage)

            viewModel.chatCommentGetList(classId, commentId)

            val toastValue = viewModel.accessToastMessage().getOrAwaitValue()
            TestCase.assertEquals("Error: $exceptionMessage", toastValue)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
