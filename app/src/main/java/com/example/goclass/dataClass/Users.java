package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("userEmail")
    String userEmail = "";
    @SerializedName("userId")
    int userId = -1;
    @SerializedName("userName")
    String userName = "";
    @SerializedName("userType")
    int userType = -1;

    public Users(String userEmail, int userId, String userName, int userType) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
    }

    public Users(String userEmail, String userName, int userType) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userType = userType;
    }

    public Users(int userId, int userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public Users(int userType, String userName) {
        this.userName = userName;
        this.userType = userType;
    }
}