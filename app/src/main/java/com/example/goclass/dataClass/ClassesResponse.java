package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class ClassesResponse {
    @SerializedName("class_id")
    String classId;
    @SerializedName("class_name")
    String className;
    @SerializedName("class_code")
    String classCode;
    @SerializedName("professor_id")
    String professorId;

    @SerializedName("class_time")
    String classTime;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getProfessorId() {
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