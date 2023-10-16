package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class ClassClassroomResponse {
    @SerializedName("building_number")
    String buildingNumber;
    @SerializedName("room_number")
    String roomNumber;
    @SerializedName("class_id")
    String classId;
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

    public String getClassId() {
        return classId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}