package com.example.goclass.Network.DataClass;

import com.google.gson.annotations.SerializedName;

public class ClassClassroom {
    @SerializedName("buildingNumber")
    String buildingNumber;
    @SerializedName("roomNumber")
    String roomNumber;
    @SerializedName("classId")
    int classId;

    public ClassClassroom(String buildingNumber, String roomNumber, int classId) {
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
        this.classId = classId;
    }
}