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
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceApi {
    // naming format: model+description (e.g. user+login -> userLogin)

    // TODO: handle response that are lists -> return on index or return on all list and handle on front?
    @POST("/signup")
    Call<UsersResponse> userSignup(@Body Users users);
    @POST("/login/{email}")
    Call<UsersResponse> userLogin(@Path("email") String email);
    @POST("/logout")
    Call<CodeMessageResponse> userLogout();
    @GET("/users/{id}")
    Call<UsersResponse> userGet(@Path("id") int userId);
    @PUT("/users/{id}")
    Call<CodeMessageResponse> userEdit(@Path("id") int userId, @Body Users users);
    @GET("/users/classes")
    Call<ClassListsResponse> userGetClassList(@Body Users users);
    @GET("users/attendance/{date}")
    Call<AttendanceListsResponse> userGetAttendanceListByDate(@Path("date") String date, @Body Users users);
    @GET("users/attendance")
    Call<AttendanceDateListsResponse> attendanceGetDateList(@Body Users users);
    @POST("/class/create")
    Call<CodeMessageResponse> classCreate(@Body Classes classes);
    @POST("/class/join/{user_id}")
    Call<CodeMessageResponse> classJoin(@Path("user_id") int userId, @Body Classes classes);
    @GET("/class/{id}")
    Call<ClassesResponse> classGet(@Path("id") int classId);
    @DELETE("/class/{id}")
    Call<CodeMessageResponse> classDelete(@Path("id") int classId);
    @GET("class/{id}/chat_channel/{type}")
    Call<ChannelsResponse> classGetChannel(@Path("id") int classId, @Path("type") int channelType);
    @GET("class/{id}/attendance/{user_id}")
    Call<AttendanceListsResponse> classGetAttendanceListByUserId(@Path("id") int classId, @Path("user_id") int userId);
    @GET("/chat_channel/{id}")
    Call<CodeMessageResponse> chatChannelGetList(@Path("id") int channelId);
    @POST("/chat_channel/{id}")
    Call<MessagesResponse> chatChannelSend(@Path("id") int channelId, @Body Messages messages);
    @GET("/attendance/{id}")
    Call<AttendancesResponse> attendanceGet(@Path("id") int attendanceId);
    @PUT("/attendance/{id}")
    Call<CodeMessageResponse> attendanceEdit(@Path("id") int attendanceId);
    @DELETE("/attendance/{id}")
    Call<CodeMessageResponse> attendanceDelete(@Path("id") int attendanceId);
    @POST("/attendance/{user_id}")
    Call<CodeMessageResponse> attendanceAdd(@Path("user_id") int userId, @Body Attendances attendances);
}
