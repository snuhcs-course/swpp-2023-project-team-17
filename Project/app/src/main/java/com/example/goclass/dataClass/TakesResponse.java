package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class TakesResponse {
    @SerializedName("student_id")
    String studentId;
    @SerializedName("class_id")
    String classId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getStudentId() {
        return studentId;
    }

    public String getClassId() {
        return classId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}