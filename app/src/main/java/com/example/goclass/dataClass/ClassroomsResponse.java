package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class ClassroomsResponse {
    @SerializedName("building_number")
    String buildingNumber;
    @SerializedName("room_number")
    String roomNumber;
    @SerializedName("longitude")
    float longitude;
    @SerializedName("latitude")
    float latitude;
    @SerializedName("beacon_id")
    String beaconId;
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

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}