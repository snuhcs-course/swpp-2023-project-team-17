package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class ClassroomsResponse {
    @SerializedName("building_number")
    String buildingNumber;
    @SerializedName("room_number")
    String roomNumber;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("latitude")
    String latitude;
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

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
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