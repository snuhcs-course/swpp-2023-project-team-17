package com.example.goclass

import com.example.goclass.dataClass.*
import retrofit2.http.*

interface ServiceApi {

    @POST("/signup")
    suspend fun userSignup(@Body users: Users): UsersResponse

    @POST("/login/{email}")
    suspend fun userLogin(@Path("email") email: String): UsersResponse

    @POST("/logout")
    suspend fun userLogout(): CodeMessageResponse

    @GET("/users/{id}")
    suspend fun userGet(@Path("id") userId: Int): UsersResponse

    @PUT("/users/{id}")
    suspend fun userEdit(@Path("id") userId: Int, @Body users: Users): CodeMessageResponse

    @GET("/users/classes")
    suspend fun userGetClassList(@Body users: Users): ClassListsResponse

    @GET("users/attendance/{date}")
    suspend fun userGetAttendanceListByDate(@Path("date") date: String, @Body users: Users): AttendanceListsResponse

    @GET("users/attendance")
    suspend fun attendanceGetDateList(@Body users: Users): AttendanceDateListsResponse

    @POST("/class/create")
    suspend fun classCreate(@Body classes: Classes): CodeMessageResponse

    @POST("/class/join/{user_id}")
    suspend fun classJoin(@Path("user_id") userId: Int, @Body classes: Classes): CodeMessageResponse

    @GET("/class/{id}")
    suspend fun classGet(@Path("id") classId: Int): ClassesResponse

    @DELETE("/class/{id}")
    suspend fun classDelete(@Path("id") classId: Int): CodeMessageResponse

    @GET("class/{id}/chat_channel/{type}")
    suspend fun classGetChannel(@Path("id") classId: Int, @Path("type") channelType: Int): ChannelsResponse

    @GET("class/{id}/attendance/{user_id}")
    suspend fun classGetAttendanceListByUserId(@Path("id") classId: Int, @Path("user_id") userId: Int): AttendanceListsResponse

    @GET("/chat_channel/{id}")
    suspend fun chatChannelGetList(@Path("id") channelId: Int): CodeMessageResponse

    @POST("/chat_channel/{id}")
    suspend fun chatChannelSend(@Path("id") channelId: Int, @Body messages: Messages): MessagesResponse

    @GET("/attendance/{id}")
    suspend fun attendanceGet(@Path("id") attendanceId: Int): AttendancesResponse

    @PUT("/attendance/{id}")
    suspend fun attendanceEdit(@Path("id") attendanceId: Int): CodeMessageResponse

    @DELETE("/attendance/{id}")
    suspend fun attendanceDelete(@Path("id") attendanceId: Int): CodeMessageResponse

    @POST("/attendance/{user_id}")
    suspend fun attendanceAdd(@Path("user_id") userId: Int, @Body attendances: Attendances): CodeMessageResponse
}
