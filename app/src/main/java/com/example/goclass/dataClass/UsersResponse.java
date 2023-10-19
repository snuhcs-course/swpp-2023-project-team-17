package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class UsersResponse {
    @SerializedName("user_email")
    String userEmail;
    @SerializedName("user_id")
    String userId;
    @SerializedName("user_name")
    String userName;
    @SerializedName("user_type")
    String userType;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserType() {
        return userType;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}