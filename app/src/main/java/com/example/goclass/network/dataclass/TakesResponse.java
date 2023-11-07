package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class TakesResponse {
    @SerializedName("studentId")
    int studentId;
    @SerializedName("classId")
    int classId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getStudentId() {
        return studentId;
    }

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