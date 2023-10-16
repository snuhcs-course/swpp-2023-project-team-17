package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Takes {
    @SerializedName("student_id")
    String studentId;
    @SerializedName("class_id")
    String classId;

    public Takes(String studentId, String classId) {
        this.studentId = studentId;
        this.classId = classId;
    }
}