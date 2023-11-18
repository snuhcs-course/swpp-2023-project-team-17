package com.example.goclass.network

import com.example.goclass.network.dataclass.AttendanceDateListsResponse
import com.example.goclass.network.dataclass.AttendanceListsResponse
import com.example.goclass.network.dataclass.Attendances
import com.example.goclass.network.dataclass.AttendancesResponse
import com.example.goclass.network.dataclass.ClassCreateResponse
import com.example.goclass.network.dataclass.ClassJoinResponse
import com.example.goclass.network.dataclass.ClassListsResponse
import com.example.goclass.network.dataclass.Classes
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.network.dataclass.CodeMessageResponse
import com.example.goclass.network.dataclass.MessageListsResponse
import com.example.goclass.network.dataclass.Messages
import com.example.goclass.network.dataclass.MessagesResponse
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

    @GET("/chat_channel/{channel_id}")
    suspend fun chatChannelGetList(
        @Path("channel_id") channelId: Int,
    ): MessageListsResponse

    @POST("/chat_channel/{channel_id}")
    suspend fun chatChannelSend(
        @Path("channel_id") channelId: Int,
        @Body messages: Messages,
    ): MessagesResponse

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
}
