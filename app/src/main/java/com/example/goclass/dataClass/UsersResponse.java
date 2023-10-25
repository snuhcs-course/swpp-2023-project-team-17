package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class UsersResponse {
    @SerializedName("user_email")
    String userEmail;
    @SerializedName("user_id")
    int userId;
    @SerializedName("user_name")
    String userName;
    @SerializedName("user_type")
    int userType;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserType() {
        return userType;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}