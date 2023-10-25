package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Classes {
    @SerializedName("class_id")
    int classId = -1;
    @SerializedName("class_name")
    String className = "";
    @SerializedName("class_code")
    String classCode = "";
    @SerializedName("professor_id")
    int professorId = -1;
    @SerializedName("class_time")
    String classTime = "";
    @SerializedName("building_numer")
    String buildingNumber = "";
    @SerializedName("room_numer")
    String roomNumber = "";

    public Classes(int classId, String className, String classCode, int professorId, String classTime) {
        this.classId = classId;
        this.className = className;
        this.classCode = classCode;
        this.professorId = professorId;
        this.classTime = classTime;
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
    }

    public Classes(String className, String classCode) {
        this.className = className;
        this.classCode = classCode;
    }
}