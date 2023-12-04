package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class UsersResponse {
    @SerializedName("userEmail")
    String userEmail;
    @SerializedName("userId")
    int userId;
    @SerializedName("userName")
    String userName;
    @SerializedName("userType")
    int userType;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public UsersResponse(String userEmail, int userId, String userName, int userType, int code, String message) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.code = code;
        this.message = message;
    }

    public UsersResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

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