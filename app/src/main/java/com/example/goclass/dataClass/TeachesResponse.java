package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class TeachesResponse {
    @SerializedName("professorId")
    int professorId;
    @SerializedName("classId")
    int classId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getProfessorId() {
        return professorId;
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