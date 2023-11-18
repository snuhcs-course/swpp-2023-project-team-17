package com.example.goclass.ui.classui.chats.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.goclass.LiveDataTestUtil.getOrAwaitValue
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.MessageListsResponse
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.network.dataclass.MessagesResponse
import com.example.goclass.repository.ChatRepository
import io.mockk.coEvery
import io.mockk.impl.recording.JvmAutoHinter.Companion.exceptionMessage
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ChatViewModelTest {
    private lateinit var viewModel: ChatViewModel
    private val mockChatRepository = mockk<ChatRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChatViewModel(mockChatRepository)
    }
    @Test
    fun chatChannelSend_success() =
        runTest {
            val classId = 1
            val senderId = 1
            val content = "TestContent"
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatChannelGetList(classId) } returns mockMessageListsResponse
            coEvery { mockChatRepository.chatChannelSend(any(), any()) } returns mockCodeMessageResponse

            viewModel.chatChannelSend(classId, senderId, content)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            val sendSuccess = viewModel.sendSuccess.getOrAwaitValue()
            TestCase.assertEquals("Success", toastValue)
            TestCase.assertEquals(true, sendSuccess)
        }

    @Test
    fun chatChannelSend_failure() =
        runTest {
            val classId = 1
            val senderId = 1
            val content = "TestContent"
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    400,
                    "Failure",
                )
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatChannelGetList(classId) } returns mockMessageListsResponse
            coEvery { mockChatRepository.chatChannelSend(any(), any()) } returns mockCodeMessageResponse

            viewModel.chatChannelSend(classId, senderId, content)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            val sendSuccess = viewModel.sendSuccess.getOrAwaitValue()
            TestCase.assertEquals("Failure", toastValue)
            TestCase.assertEquals(false, sendSuccess)
        }

    @Test
    fun chatChannelSend_exception() =
        runTest {
            val classId = 1
            val senderId = 1
            val content = "TestContent"
            val exceptionMessage = "Network error"
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatChannelGetList(classId) } returns mockMessageListsResponse
            coEvery { mockChatRepository.chatChannelSend(any(), any()) } throws Exception(exceptionMessage)

            viewModel.chatChannelSend(classId, senderId, content)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            val sendSuccess = viewModel.sendSuccess.getOrAwaitValue()
            TestCase.assertEquals("Error: $exceptionMessage", toastValue)
            TestCase.assertEquals(false, sendSuccess)
        }

    @Test
    fun chatChannelEdit_success() =
        runTest {
            val classId = 1
            val content = "TestContent"
            val messageId = 1
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatChannelGetList(classId) } returns mockMessageListsResponse
            coEvery { mockChatRepository.chatChannelEdit(any(), any()) } returns mockCodeMessageResponse

            viewModel.chatChannelEdit(classId, content, messageId)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            val editSuccess = viewModel.editSuccess.getOrAwaitValue()
            TestCase.assertEquals("Success", toastValue)
            TestCase.assertEquals(true, editSuccess)
        }

    @Test
    fun chatChannelEdit_failure() =
        runTest {
            val classId = 1
            val content = "TestContent"
            val messageId = 1
            val mockCodeMessageResponse =
                CodeMessageResponse(
                    400,
                    "Failure",
                )
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatChannelGetList(classId) } returns mockMessageListsResponse
            coEvery { mockChatRepository.chatChannelEdit(any(), any()) } returns mockCodeMessageResponse

            viewModel.chatChannelEdit(classId, content, messageId)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            val editSuccess = viewModel.editSuccess.getOrAwaitValue()
            TestCase.assertEquals("Failure", toastValue)
            TestCase.assertEquals(false, editSuccess)
        }

    @Test
    fun chatChannelEdit_exception() =
        runTest {
            val classId = 1
            val content = "TestContent"
            val messageId = 1
            val exceptionMessage = "Network error"
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatChannelGetList(classId) } returns mockMessageListsResponse
            coEvery { mockChatRepository.chatChannelEdit(any(), any()) } throws Exception(exceptionMessage)

            viewModel.chatChannelEdit(classId, content, messageId)

            val toastValue = viewModel.toastMessage.getOrAwaitValue()
            val editSuccess = viewModel.editSuccess.getOrAwaitValue()
            TestCase.assertEquals("Error: $exceptionMessage", toastValue)
            TestCase.assertEquals(false, editSuccess)
        }

    @Test
    fun chatChannelGetList_success() =
        runTest {
            val classId = 1
            val messagesResponse =
                MessagesResponse(
                    1,
                    1,
                    1,
                    "TestTime",
                    1,
                    "TestName",
                    "TestContent",
                )
            val mockMessageListsResponse =
                MessageListsResponse(
                    listOf(
                        messagesResponse,
                    ),
                    200,
                    "Success",
                )

            coEvery { mockChatRepository.chatChannelGetList(classId) } returns mockMessageListsResponse

            viewModel.chatChannelGetList(classId)

            val liveDataValue = viewModel.messageListLiveData.getOrAwaitValue()
            TestCase.assertEquals(1, liveDataValue.size)
            TestCase.assertEquals(messagesResponse, liveDataValue[0])
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}