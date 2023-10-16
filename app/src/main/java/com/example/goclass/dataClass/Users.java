package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("user_email")
    String userEmail;
    @SerializedName("user_id")
    String userId;
    @SerializedName("user_name")
    String userName;
    @SerializedName("user_type")
    String userType;

    public Users(String userEmail, String userId, String userName, String userType) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
    }
}