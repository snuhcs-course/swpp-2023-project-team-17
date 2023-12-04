package com.example.goclass.network

import com.example.goclass.network.dataclass.AttendanceDateListsResponse
import com.example.goclass.network.dataclass.AttendanceDetailListsResponse
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.network.dataclass.ClassCreateResponse
import com.example.goclass.network.dataclass.ClassJoinResponse
import com.example.goclass.network.dataclass.ClassListsResponse
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.CommentCountResponse
import com.example.goclass.network.dataclass.CommentListsResponse
import com.example.goclass.network.dataclass.Comments
import com.example.goclass.network.dataclass.MessageListsResponse
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.network.dataclass.Users
import com.example.goclass.network.dataclass.UsersResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ServiceApi {
    @POST("/login/{email}")
    suspend fun userLogin(
        @Path("email") email: String,
    ): UsersResponse

    @GET("/users/{id}")
    suspend fun userGet(
        @Path("id") userId: Int,
    ): UsersResponse

    @PUT("/users/{id}")
    suspend fun userEdit(
        @Path("id") userId: Int,
        @Body users: Users,
    ): CodeMessageResponse

    @GET("/users/classes")
    suspend fun userGetClassList(
        @QueryMap users: Map<String, String>,
    ): ClassListsResponse

    @GET("users/attendance/{date}")
    suspend fun userGetAttendanceListByDate(
        @Path("date") date: String,
        @QueryMap classes: Map<String, String>,
    ): AttendanceListsResponse

    @GET("users/attendance")
    suspend fun attendanceGetDateList(
        @QueryMap classMap: Map<String, String>,
    ): AttendanceDateListsResponse

    @POST("/class/create")
    suspend fun classCreate(
        @Body classes: Classes,
    ): ClassCreateResponse

    @POST("/class/join/{user_id}")
    suspend fun classJoin(
        @Path("user_id") userId: Int,
        @Body classes: Classes,
    ): ClassJoinResponse

    @GET("/class/{id}")
    suspend fun classGet(
        @Path("id") classId: Int,
    ): ClassesResponse

    @DELETE("/class/{id}")
    suspend fun classDelete(
        @Path("id") classId: Int,
    ): CodeMessageResponse

    @GET("class/{id}/attendance/{user_id}")
    suspend fun classGetAttendanceListByUserId(
        @Path("id") classId: Int,
        @Path("user_id") userId: Int,
    ): AttendanceListsResponse

    @GET("/chat_channel/count/{message_id}")
    suspend fun chatChannelGetCommentCount(
        @Path("message_id") messageId: Int,
    ): CommentCountResponse

    @GET("/chat_channel/{class_id}")
    suspend fun chatChannelGetList(
        @Path("class_id") classId: Int,
    ): MessageListsResponse

    @POST("/chat_channel/{class_id}")
    suspend fun chatChannelSend(
        @Path("class_id") classId: Int,
        @Body messages: Messages,
    ): CodeMessageResponse

    @PUT("/chat_channel/{class_id}")
    suspend fun chatChannelEdit(
        @Path("class_id") classId: Int,
        @Body messages: Messages,
    ): CodeMessageResponse

    @GET("/chat_channel/{class_id}/comment/{id}")
    suspend fun chatCommentGetList(
        @Path("class_id") classId: Int,
        @Path("id") id: Int,
    ): CommentListsResponse

    @POST("/chat_channel/{class_id}/comment/{id}")
    suspend fun chatCommentSend(
        @Path("class_id") classId: Int,
        @Path("id") id: Int,
        @Body comments: Comments,
    ): CodeMessageResponse

    @PUT("/chat_channel/{class_id}/comment/{id}")
    suspend fun chatCommentEdit(
        @Path("class_id") classId: Int,
        @Path("id") id: Int,
        @Body comments: Comments,
    ): CodeMessageResponse

    @GET("/attendance/{id}")
    suspend fun attendanceGet(
        @Path("id") attendanceId: Int,
    ): AttendancesResponse

    @PUT("/attendance/{id}")
    suspend fun attendanceEdit(
        @Path("id") attendanceId: Int,
    ): CodeMessageResponse

    @DELETE("/attendance/{id}")
    suspend fun attendanceDelete(
        @Path("id") attendanceId: Int,
    ): CodeMessageResponse

    @POST("/attendance/{user_id}")
    suspend fun attendanceAdd(
        @Path("user_id") userId: Int,
        @Body attendances: Attendances,
    ): CodeMessageResponse

    @GET("/attendance/detail/{attendance_id}")
    suspend fun attendanceDetailListGet(
        @Path("attendance_id") attendanceId: Int,
    ): AttendanceDetailListsResponse

    @PUT("/attendance/detail/{attendance_id}")
    suspend fun attendanceDetailListAdd(
        @Path("attendance_id") attendanceId: Int,
        @QueryMap isAttendMap: Map<String, String>,
    ): CodeMessageResponse
}
