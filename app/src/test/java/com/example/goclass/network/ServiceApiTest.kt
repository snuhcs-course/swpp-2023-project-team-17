package com.example.goclass.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.goclass.network.dataclass.AttendanceDateListsResponse
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.network.dataclass.ClassJoinResponse
import com.example.goclass.network.dataclass.ClassListsResponse
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.CommentListsResponse
import com.example.goclass.network.dataclass.Comments
import com.example.goclass.network.dataclass.CommentsResponse
import com.example.goclass.network.dataclass.MessageListsResponse
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.network.dataclass.MessagesResponse
import com.example.goclass.network.dataclass.Users
import com.example.goclass.network.dataclass.UsersResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
class ServiceApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ServiceApi
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockWebServer = MockWebServer()

        // Initialize Moshi to parse JSON responses
        val moshi =
            Moshi.Builder()
                .build()

        apiService =
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ServiceApi::class.java)
    }

    @Test
    fun userLogin_test() =
        runTest {
            // Given
            val email = "test@example.com"
            val expectedResponse =
                UsersResponse(
                    email,
                    1,
                    "TestName",
                    1,
                    200,
                    "Success",
                )

            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<UsersResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(UsersResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.userLogin(email)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.userEmail, actualResponse.userEmail)
            TestCase.assertEquals(expectedResponse.userId, actualResponse.userId)
            TestCase.assertEquals(expectedResponse.userName, actualResponse.userName)
            TestCase.assertEquals(expectedResponse.userType, actualResponse.userType)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun userGet_test() =
        runTest {
            // Given
            val userId = 1234
            val expectedResponse =
                UsersResponse(
                    "test@example.com",
                    userId,
                    "TestName",
                    1,
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<UsersResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(UsersResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.userGet(userId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.userEmail, actualResponse.userEmail)
            TestCase.assertEquals(expectedResponse.userId, actualResponse.userId)
            TestCase.assertEquals(expectedResponse.userName, actualResponse.userName)
            TestCase.assertEquals(expectedResponse.userType, actualResponse.userType)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun userEdit_test() =
        runTest {
            // Given
            val userId = 1234
            val users = Users("test@example.com", userId, "TestName", 0)
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.userEdit(userId, users)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun userGetClassList_test() =
        runTest {
            // Given
            val users = mapOf("userId" to "1", "userType" to "0")
            val expectedResponse =
                ClassListsResponse(
                    listOf(
                        ClassesResponse(
                            1,
                            "TestName",
                            "TestCode",
                            1,
                            "TestTime",
                            "TestBuilding",
                            "TestRoom",
                        ),
                    ),
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<ClassListsResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(ClassListsResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.userGetClassList(users)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.classList.size, actualResponse.classList.size)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(expectedResponse.classList[0].classId, actualResponse.classList[0].classId)
            TestCase.assertEquals(expectedResponse.classList[0].className, actualResponse.classList[0].className)
            TestCase.assertEquals(expectedResponse.classList[0].classCode, actualResponse.classList[0].classCode)
            TestCase.assertEquals(expectedResponse.classList[0].professorId, actualResponse.classList[0].professorId)
            TestCase.assertEquals(expectedResponse.classList[0].classTime, actualResponse.classList[0].classTime)
            TestCase.assertEquals(expectedResponse.classList[0].buildingNumber, actualResponse.classList[0].buildingNumber)
            TestCase.assertEquals(expectedResponse.classList[0].roomNumber, actualResponse.classList[0].roomNumber)
        }

    @Test
    fun userGetAttendanceListByDate_test() =
        runTest {
            // Given
            val date = "2023-11-16"
            val classes = mapOf("classId" to "1", "userType" to "1")
            val expectedResponse =
                AttendanceListsResponse(
                    listOf(
                        AttendancesResponse(
                            1234,
                            0,
                            "attendanceDate",
                            0,
                            0,
                            1,
                            1,
                        ),
                    ),
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<AttendanceListsResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(AttendanceListsResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.userGetAttendanceListByDate(date, classes)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.attendanceList.size, actualResponse.attendanceList.size)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceId,
                actualResponse.attendanceList[0].attendanceId,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceStatus,
                actualResponse.attendanceList[0].attendanceStatus,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceDate,
                actualResponse.attendanceList[0].attendanceDate,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceDuration,
                actualResponse.attendanceList[0].attendanceDuration,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].isSent,
                actualResponse.attendanceList[0].isSent,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].studentId,
                actualResponse.attendanceList[0].studentId,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].classId,
                actualResponse.attendanceList[0].classId,
            )
        }

    @Test
    fun attendanceGetDateList_test() =
        runTest {
            // Given
            val classMap = mapOf("classId" to "1", "userType" to "1")
            val expectedResponse =
                AttendanceDateListsResponse(
                    listOf(
                        AttendancesResponse(
                            "attendanceDate",
                        )
                    ),
                    200,
                    "Success",
                )

            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<AttendanceDateListsResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(AttendanceDateListsResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.attendanceGetDateList(classMap)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(
                expectedResponse.attendanceDateList[0].attendanceDate,
                actualResponse.attendanceDateList[0].attendanceDate,
            )
        }

    @Test
    fun classCreate_test() =
        runTest {
            // Given
            val classes = Classes("TestName", "1234", 1, "TestTime", "TestBuildingNumber", "TestRoomNumber")
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.classCreate(classes)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun classJoin_test() =
        runTest {
            // Given
            val userId = 1
            val classes = Classes("TestName", "1234")
            val expectedResponse =
                ClassJoinResponse(
                    1234,
                    "TestTime",
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<ClassJoinResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(ClassJoinResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.classJoin(userId, classes)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(expectedResponse.classId, actualResponse.classId)
            TestCase.assertEquals(expectedResponse.classTime, actualResponse.classTime)
        }

    @Test
    fun classGet_test() =
        runTest {
            // Given
            val classId = 1
            val expectedResponse =
                ClassesResponse(
                    "TestName",
                    "TestCode",
                    1,
                    "TestTime",
                    "TestBuilding",
                    "TestRoom",
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<ClassesResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(ClassesResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.classGet(classId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(expectedResponse.className, actualResponse.className)
            TestCase.assertEquals(expectedResponse.classCode, actualResponse.classCode)
            TestCase.assertEquals(expectedResponse.professorId, actualResponse.professorId)
            TestCase.assertEquals(expectedResponse.classTime, actualResponse.classTime)
            TestCase.assertEquals(expectedResponse.buildingNumber, actualResponse.buildingNumber)
            TestCase.assertEquals(expectedResponse.roomNumber, actualResponse.roomNumber)
        }

    @Test
    fun classDelete_test() =
        runTest {
            // Given
            val classId = 1
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.classDelete(classId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun classGetAttendanceListByUserId_test() =
        runTest {
            // Given
            val classId = 1
            val userId = 1
            val expectedResponse =
                AttendanceListsResponse(
                    listOf(
                        AttendancesResponse(
                            1234,
                            0,
                            "attendanceDate",
                            0,
                            0,
                            1,
                            1,
                        ),
                    ),
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<AttendanceListsResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(AttendanceListsResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.classGetAttendanceListByUserId(classId, userId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.attendanceList.size, actualResponse.attendanceList.size)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceId,
                actualResponse.attendanceList[0].attendanceId,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceStatus,
                actualResponse.attendanceList[0].attendanceStatus,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceDate,
                actualResponse.attendanceList[0].attendanceDate,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].attendanceDuration,
                actualResponse.attendanceList[0].attendanceDuration,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].isSent,
                actualResponse.attendanceList[0].isSent,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].studentId,
                actualResponse.attendanceList[0].studentId,
            )
            TestCase.assertEquals(
                expectedResponse.attendanceList[0].classId,
                actualResponse.attendanceList[0].classId,
            )
        }

    @Test
    fun chatChannelGetList_test() =
        runTest {
            // Given
            val classId = 1234
            val expectedResponse =
                MessageListsResponse(
                    listOf(
                        MessagesResponse(
                            1,
                            1,
                            classId,
                            "2023-11-16",
                            1,
                            "TestName",
                            "TestContent",
                        )
                    ),
                    200,
                    "Success",
                )

            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<MessageListsResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(MessageListsResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.chatChannelGetList(classId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.messageList.size, actualResponse.messageList.size)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(expectedResponse.messageList[0].messageId, actualResponse.messageList[0].messageId)
            TestCase.assertEquals(expectedResponse.messageList[0].commentId, actualResponse.messageList[0].commentId)
            TestCase.assertEquals(expectedResponse.messageList[0].classId, actualResponse.messageList[0].classId)
            TestCase.assertEquals(expectedResponse.messageList[0].timeStamp, actualResponse.messageList[0].timeStamp)
            TestCase.assertEquals(expectedResponse.messageList[0].senderId, actualResponse.messageList[0].senderId)
            TestCase.assertEquals(expectedResponse.messageList[0].senderName, actualResponse.messageList[0].senderName)
            TestCase.assertEquals(expectedResponse.messageList[0].content, actualResponse.messageList[0].content)
        }

    @Test
    fun chatChannelSend_test() =
        runTest {
            // Given
            val classId = 1234
            val messages = Messages(1, "TestContent")
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.chatChannelSend(classId, messages)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun chatChannelEdit_test() =
        runTest {
            // Given
            val classId = 1234
            val messages = Messages(1, "TestContent")
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.chatChannelEdit(classId, messages)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun chatCommentGetList_test() =
        runTest {
            // Given
            val classId = 1234
            val messageId = 123
            val expectedResponse =
                CommentListsResponse(
                    listOf(
                        CommentsResponse(
                            messageId,
                            1,
                            classId,
                            "2023-11-16",
                            1,
                            "TestName",
                            "TestContent",
                        )
                    ),
                    200,
                    "Success",
                )

            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CommentListsResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CommentListsResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.chatCommentGetList(classId, messageId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.commentList.size, actualResponse.commentList.size)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
            TestCase.assertEquals(expectedResponse.commentList[0].messageId, actualResponse.commentList[0].messageId)
            TestCase.assertEquals(expectedResponse.commentList[0].commentId, actualResponse.commentList[0].commentId)
            TestCase.assertEquals(expectedResponse.commentList[0].classId, actualResponse.commentList[0].classId)
            TestCase.assertEquals(expectedResponse.commentList[0].timeStamp, actualResponse.commentList[0].timeStamp)
            TestCase.assertEquals(expectedResponse.commentList[0].senderId, actualResponse.commentList[0].senderId)
            TestCase.assertEquals(expectedResponse.commentList[0].senderName, actualResponse.commentList[0].senderName)
            TestCase.assertEquals(expectedResponse.commentList[0].content, actualResponse.commentList[0].content)
        }

    @Test
    fun chatCommentSend_test() =
        runTest {
            // Given
            val classId = 1234
            val messageId = 123
            val userId = 1
            val content = "TestContent"
            val sendComments = Comments(userId, content)
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.chatCommentSend(classId, messageId, sendComments)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun chatCommentEdit_test() =
        runTest {
            // Given
            val classId = 1234
            val messageId = 123
            val userId = 1
            val content = "TestContent"
            val sendComments = Comments(userId, content)
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.chatCommentEdit(classId, messageId, sendComments)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun attendanceGet_test() =
        runTest {
            // Given
            val attendanceId = 1234
            val expectedResponse =
                AttendancesResponse(
                    attendanceId,
                    2,
                    "2023-11-16",
                    60,
                    0,
                    1,
                    1,
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<AttendancesResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(AttendancesResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.attendanceGet(attendanceId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.attendanceId, actualResponse.attendanceId)
            TestCase.assertEquals(expectedResponse.attendanceStatus, actualResponse.attendanceStatus)
            TestCase.assertEquals(expectedResponse.attendanceDate, actualResponse.attendanceDate)
            TestCase.assertEquals(expectedResponse.attendanceDuration, actualResponse.attendanceDuration)
            TestCase.assertEquals(expectedResponse.isSent, actualResponse.isSent)
            TestCase.assertEquals(expectedResponse.studentId, actualResponse.studentId)
            TestCase.assertEquals(expectedResponse.classId, actualResponse.classId)
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun attendanceEdit_test() =
        runTest {
            // Given
            val attendanceId = 1234
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.attendanceEdit(attendanceId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun attendanceDelete_test() =
        runTest {
            // Given
            val attendanceId = 1234
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.attendanceDelete(attendanceId)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @Test
    fun attendanceAdd_test() =
        runTest {
            // Given
            val attendanceId = 1234
            val attendances = Attendances(2, 60, 1)
            val expectedResponse =
                CodeMessageResponse(
                    200,
                    "Success",
                )
            // Convert the expected response to JSON using Moshi
            val jsonAdapter: JsonAdapter<CodeMessageResponse> =
                Moshi.Builder()
                    .build()
                    .adapter(CodeMessageResponse::class.java)

            val expectedResponseBody = jsonAdapter.toJson(expectedResponse)

            // Enqueue a mock response with the expected JSON
            mockWebServer.enqueue(MockResponse().setBody(expectedResponseBody))

            // When
            val actualResponse = apiService.attendanceAdd(attendanceId, attendances)

            // Then
            // Compare the expected and actual responses directly
            TestCase.assertEquals(expectedResponse.code, actualResponse.code)
            TestCase.assertEquals(expectedResponse.message, actualResponse.message)
        }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }
}