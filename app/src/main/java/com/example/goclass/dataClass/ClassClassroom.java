package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class ClassClassroom {
    @SerializedName("building_number")
    String buildingNumber;
    @SerializedName("room_number")
    String roomNumber;
    @SerializedName("class_id")
    String classId;

    public ClassClassroom(String buildingNumber, String roomNumber, String classId) {
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
        this.classId = classId;
    }
}