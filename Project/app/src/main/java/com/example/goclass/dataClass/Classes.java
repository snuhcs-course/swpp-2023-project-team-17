package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Classes {
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

    public Classes(String classId, String className, String classCode, String professorId, String classTime) {
        this.classId = classId;
        this.className = className;
        this.classCode = classCode;
        this.professorId = professorId;
        this.classTime = classTime;
    }
}