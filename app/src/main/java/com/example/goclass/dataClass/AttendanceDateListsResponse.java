package com.example.goclass.dataClass;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AttendanceDateListsResponse {
    @SerializedName("attendanceDateList")
    private List<AttendancesResponse> attendanceDateList;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public List<AttendancesResponse> getAttendanceDateList() {
        return attendanceDateList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}