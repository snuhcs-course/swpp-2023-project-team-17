package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceDetailListResponse {
    @SerializedName("attendanceDetailList")
    private List<String> attendanceDetailList;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public AttendanceDetailListResponse(List<String> attendanceDetailList, int code, String message) {
        this.attendanceDetailList = attendanceDetailList;
        this.code = code;
        this.message = message;
    }

    public List<String> getAttendanceDetailList() {
        return attendanceDetailList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}