package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Classes {
    @SerializedName("classId")
    int classId = -1;
    @SerializedName("className")
    String className = "";
    @SerializedName("classCode")
    String classCode = "";
    @SerializedName("professorId")
    int professorId = -1;
    @SerializedName("classTime")
    String classTime = "";
    @SerializedName("buildingNumber")
    String buildingNumber = "";
    @SerializedName("roomNumber")
    String roomNumber = "";

    public Classes(String className, String classCode, int professorId, String classTime, String buildingNumber, String roomNumber) {
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

    public String getClassName() {
        return className;
    }
    public int getClassId() {
        return classId;
    }
}