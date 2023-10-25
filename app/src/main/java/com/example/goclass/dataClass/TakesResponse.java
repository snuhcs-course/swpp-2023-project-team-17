package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class TakesResponse {
    @SerializedName("student_id")
    int studentId;
    @SerializedName("class_id")
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