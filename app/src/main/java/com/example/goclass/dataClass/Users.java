package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("user_email")
    String userEmail = "";
    @SerializedName("user_id")
    int userId = -1;
    @SerializedName("user_name")
    String userName = "";
    @SerializedName("user_type")
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
        this.userType = userType;
        this.userType = userType;
    }

    public Users(int userId, String userName) {
        this.userName = userName;
        this.userType = userType;
    }
}