package com.example.goclass.Network.DataClass;

import com.google.gson.annotations.SerializedName;

public class ClassroomsResponse {
    @SerializedName("buildingNumber")
    String buildingNumber;
    @SerializedName("roomNumber")
    String roomNumber;
    @SerializedName("longitude")
    float longitude;
    @SerializedName("latitude")
    float latitude;
    @SerializedName("beaconId")
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