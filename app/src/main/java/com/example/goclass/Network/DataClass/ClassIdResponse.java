package com.example.goclass.Network.DataClass;

import com.google.gson.annotations.SerializedName;

public class ClassIdResponse {
    @SerializedName("classId")
    int classId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getClassId() {
        return classId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}