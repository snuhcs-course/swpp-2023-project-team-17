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
    @SerializedName("buildingNumber")
    String buildingNumber;
    @SerializedName("roomNumber")
    String roomNumber;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public ClassesResponse(String className, String classCode, int professorId, String classTime, String buildingNumber, String roomNumber, int code, String message) {
        this.className = className;
        this.classCode = classCode;
        this.professorId = professorId;
        this.classTime = classTime;
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
        this.code = code;
        this.message = message;
    }

    public ClassesResponse(int classId, String className, String classCode, int professorId, String classTime, String buildingNumber, String roomNumber) {
        this.classId = classId;
        this.className = className;
        this.classCode = classCode;
        this.professorId = professorId;
        this.classTime = classTime;
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
    }

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

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}