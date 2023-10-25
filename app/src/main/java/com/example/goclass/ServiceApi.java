package com.example.goclass;

import com.example.goclass.dataClass.Users;
import com.example.goclass.dataClass.Classes;
import com.example.goclass.dataClass.Messages;
import com.example.goclass.dataClass.Attendances;
import com.example.goclass.dataClass.UsersResponse;
import com.example.goclass.dataClass.CodeMessageResponse;
import com.example.goclass.dataClass.ClassListsResponse;
import com.example.goclass.dataClass.AttendanceListsResponse;
import com.example.goclass.dataClass.AttendanceDateListsResponse;
import com.example.goclass.dataClass.ClassesResponse;
import com.example.goclass.dataClass.ChannelsResponse;
import com.example.goclass.dataClass.MessagesResponse;
import com.example.goclass.dataClass.AttendancesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServiceApi {
    // naming format: model+description (e.g. user+login -> userLogin)

    // TODO: handle response that are lists -> return on index or return on all list and handle on front?
    @POST("/signup")
    Call<UsersResponse> userSignup(@Body Users users);
    @POST("/login/:email")
    Call<UsersResponse> userLogin();
    @POST("/logout")
    Call<CodeMessageResponse> userLogout();
    @GET("/users/:id")
    Call<UsersResponse> userGet();
    @PUT("/users/:id")
    Call<CodeMessageResponse> userEdit(@Body Users users);
    @GET("/users/classes")
    Call<ClassListsResponse> userGetClassList(@Body Users users);
    @GET("users/attendance/:date")
    Call<AttendanceListsResponse> userGetAttendanceListByDate(@Body Users users);
    @GET("users/attendance")
    Call<AttendanceDateListsResponse> attendanceGetDateList(@Body Users users);
    @POST("/class/create")
    Call<CodeMessageResponse> classCreate(@Body Classes classes);
    @POST("/class/join/:user_id")
    Call<CodeMessageResponse> classJoin(@Body Classes classes);
    @GET("/class/:id")
    Call<ClassesResponse> classGet();
    @DELETE("/class/:id")
    Call<CodeMessageResponse> classDelete();
    @GET("class/:id/chat_channel/:type")
    Call<ChannelsResponse> classGetChannel();
    @GET("class/:id/attendance/:user_id")
    Call<AttendanceListsResponse> classGetAttendanceListByUserId();
    @GET("/chat_channel/:id")
    Call<CodeMessageResponse> chatChannelGetList();
    @POST("/chat_channel/:id")
    Call<MessagesResponse> chatChannelSend(@Body Messages messages);
    @GET("/attendance/:id")
    Call<AttendancesResponse> attendanceGet();
    @PUT("/attendance/:id")
    Call<CodeMessageResponse> attendanceEdit();
    @DELETE("/attendance/:id")
    Call<CodeMessageResponse> attendanceDelete();
    @POST("/attendance/:user_id")
    Call<CodeMessageResponse> attendanceAdd(@Body Attendances attendances);
}
