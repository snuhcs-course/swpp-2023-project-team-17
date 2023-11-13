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
    @SerializedName("classTime")
    int buildingNumber;
    @SerializedName("classTime")
    int roomNumber;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public ClassesResponse(int code, String message, String className, String classCode, int professorId, String classTime, int buildingNumber, int roomNumber) {
        this.code = code;
        this.message = message;
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

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}