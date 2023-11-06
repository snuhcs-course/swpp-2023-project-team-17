package com.example.goclass.Network.DataClass;

import com.google.gson.annotations.SerializedName;

public class Takes {
    @SerializedName("studentId")
    int studentId;
    @SerializedName("classId")
    int classId;

    public Takes(int studentId, int classId) {
        this.studentId = studentId;
        this.classId = classId;
    }
}