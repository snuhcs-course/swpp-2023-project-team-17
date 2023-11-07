package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class ClassesResponse {
    @SerializedName("classId")
    int classId;
    @SerializedName("className")
    String className;
    @SerializedName("classCode")
    String classCode;
    @SerializedName("professorId")
    int professorId;

    @SerializedName("classTime")
    String classTime;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getClassCode() {
        return classCode;
    }

    public int getProfessorId() {
        return professorId;
    }

    public String getClassTime() {
        return classTime;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}