package com.example.goclass.Network.DataClass;

import com.google.gson.annotations.SerializedName;

public class ClassClassroomResponse {
    @SerializedName("buildingNumber")
    String buildingNumber;
    @SerializedName("roomNumber")
    String roomNumber;
    @SerializedName("classId")
    int classId;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getClassId() {
        return classId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}