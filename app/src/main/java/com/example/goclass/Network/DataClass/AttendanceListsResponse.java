package com.example.goclass.Network.DataClass;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AttendanceListsResponse {
    @SerializedName("attendanceList")
    private List<AttendancesResponse> attendanceList;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public List<AttendancesResponse> getAttendanceList() {
        return attendanceList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}