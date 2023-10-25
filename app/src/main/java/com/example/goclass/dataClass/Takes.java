package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Takes {
    @SerializedName("student_id")
    int studentId;
    @SerializedName("class_id")
    int classId;

    public Takes(int studentId, int classId) {
        this.studentId = studentId;
        this.classId = classId;
    }
}