package com.example.goclass;

import com.example.goclass.dataClass.Attendances;
import com.example.goclass.dataClass.Channels;
import com.example.goclass.dataClass.Classes;
import com.example.goclass.dataClass.Users;
import com.example.goclass.dataClass.AttendancesResponse;
import com.example.goclass.dataClass.ClassesResponse;
import com.example.goclass.dataClass.MessagesResponse;
import com.example.goclass.dataClass.TakesResponse;
import com.example.goclass.dataClass.UsersResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServiceApi {
    // naming format: model+description (e.g. user+login -> userLogin)
    @POST("/login")
    Call<UsersResponse> userLogin(@Body Users user);
    @POST("/logout")
    Call<UsersResponse> userLogout();
    @GET("/users/:id")
    Call<UsersResponse> userGet(@Body Users user);
    @PUT("/users/:id")
    Call<UsersResponse> userEdit(@Body Users user);
    @GET("/users/:id/classes")
    Call<TakesResponse> userGetClassList(@Body Users user);
    @GET("users/attendance/:date")
    Call<AttendancesResponse> userGetAttendanceListByDate(@Body Users user);
    @GET("users/attendance")
    Call<AttendancesResponse> attendanceGetDateList(@Body Users user);
    @POST("/class/create")
    Call<ClassesResponse> classCreate(@Body Classes class);
    @POST("/class/join")
    Call<TakesResponse> classJoin(@Body Classes class);
    @GET("/class/:id")
    Call<ClassesResponse> classGet(@Body Classes class);
    @DELETE("/class/:id")
    Call<ClassesResponse> classDelete(@Body Classes class);
    @GET("class/chat_channel/:type")
    Call<ChannelsResponse> classGetChannel(@Body Classes class);
    @GET("class/attendance/:user_id")
    Call<AttendancesResponse> classGetAttendanceListByUserId(@Body Classes class);
    @GET("/chat_channel/:id")
    Call<MessagesResponse> chatChannelGetList(@Body Channels channel);
    @POST("/chat_channel/:id")
    Call<MessagesResponse> chatChannelSend(@Body Messages message);
    @GET("/attendance/:id")
    Call<AttendancesResponse> attendanceGet(@Body Attendances attendance);
    @PUT("/attendance/:id")
    Call<AttendancesResponse> attendanceEdit(@Body Attendances attendance);
    @DELETE("/attendance/:id")
    Call<AttendancesResponse> attendanceDelete(@Body Attendances attendance);
    @POST("/attendance/:user_id")
    Call<AttendancesResponse> attendanceAdd(@Body Attendances attendance);
}
