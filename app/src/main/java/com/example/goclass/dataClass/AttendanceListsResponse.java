package com.example.goclass.dataClass;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AttendanceListsResponse {
    @SerializedName("attendanceList")
    private List<HashMap<String, String>> attendanceList;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public List<HashMap<String, String>> getAttendanceList() {
        return attendanceList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}