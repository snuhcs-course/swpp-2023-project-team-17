package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class TeachesResponse {
    @SerializedName("professor_id")
    String professorId;
    @SerializedName("class_id")
    String classId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getProfessorId() {
        return professorId;
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