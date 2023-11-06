package com.example.goclass

import com.example.goclass.dataClass.AttendanceDateListsResponse
import com.example.goclass.dataClass.AttendanceListsResponse
import com.example.goclass.dataClass.Attendances
import com.example.goclass.dataClass.AttendancesResponse
import com.example.goclass.dataClass.ClassIdResponse
import com.example.goclass.dataClass.ClassListsResponse
import com.example.goclass.dataClass.Classes
import com.example.goclass.dataClass.ClassesResponse
import com.example.goclass.dataClass.CodeMessageResponse
import com.example.goclass.dataClass.MessageListsResponse
import com.example.goclass.dataClass.Messages
import com.example.goclass.dataClass.MessagesResponse
import com.example.goclass.dataClass.Users
import com.example.goclass.dataClass.UsersResponse
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
        @QueryMap users: Map<String, String>,
    ): AttendanceListsResponse

    @GET("users/attendance")
    suspend fun attendanceGetDateList(
        @QueryMap users: Map<String, String>,
    ): AttendanceDateListsResponse

    @POST("/class/create")
    suspend fun classCreate(
        @Body classes: Classes,
    ): CodeMessageResponse

    @POST("/class/join/{user_id}")
    suspend fun classJoin(
        @Path("user_id") userId: Int,
        @Body classes: Classes,
    ): ClassIdResponse

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

    @GET("/chat_channel/{class_id}/{channel_type}")
    suspend fun chatChannelGetList(
        @Path("class_id") classId: Int,
        @Path("channel_type") channelType: Int,
    ): MessageListsResponse

    @POST("/chat_channel/{class_id}/{channel_type}")
    suspend fun chatChannelSend(
        @Path("class_id") classId: Int,
        @Path("channel_type") channelType: Int,
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
