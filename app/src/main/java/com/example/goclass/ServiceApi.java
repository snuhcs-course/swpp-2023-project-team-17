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
    @POST("/login")
    Call<UsersResponse> userLogin(@Body Users users);
    @POST("/logout")
    Call<UsersResponse> userLogout();
    @GET("/users/:id")
    Call<UsersResponse> userGet(@Body Users users);
    @PUT("/users/:id")
    Call<UsersResponse> userEdit(@Body Users users);
    @GET("/users/:id/classes")
    Call<TakesResponse> userGetClassList(@Body Users users);
    @POST("/class/create")
    Call<ClassesResponse> classCreate(@Body Classes classes);
    @POST("/class/join")
    Call<TakesResponse> classJoin(@Body Classes classes);
    @GET("/class/:id")
    Call<ClassesResponse> classGet(@Body Classes classes);
    @DELETE("/class/:id")
    Call<ClassesResponse> classDelete(@Body Classes classes);
    @GET("/chat_channel/:id")
    Call<MessagesResponse> chatChannelGetList(@Body Channels channels);
    @POST("/chat_channel/:id")
    Call<MessagesResponse> chatChannelSend(@Body Channels channels);
    @GET("/attendance")
    Call<AttendancesResponse> attendanceGetDateList(@Body Attendances attendances);
    @GET("/attendance/:date")
    Call<AttendancesResponse> attendanceGetListByDate(@Body Attendances attendances);
    @GET("/attendance/:id")
    Call<AttendancesResponse> attendanceGet(@Body Attendances attendances);
    @PUT("/attendance/:id")
    Call<AttendancesResponse> attendanceEdit(@Body Attendances attendances);
    @DELETE("/attendance/:id")
    Call<AttendancesResponse> attendanceDelete(@Body Attendances attendances);
    @GET("/attendance/:user_id")
    Call<AttendancesResponse> attendanceGetListByUserId(@Body Attendances attendances);
    @POST("/attendance/:user_id")
    Call<AttendancesResponse> attendanceAdd(@Body Attendances attendances);

}
