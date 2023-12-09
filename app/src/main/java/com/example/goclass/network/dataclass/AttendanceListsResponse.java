package com.example.goclass.network.dataclass;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AttendanceListsResponse {
    @SerializedName("attendanceList")
    private List<AttendancesResponse> attendanceList;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public AttendanceListsResponse(List<AttendancesResponse> attendanceList, int code, String message) {
        this.attendanceList = attendanceList;
        this.code = code;
        this.message = message;
    }

    public AttendanceListsResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

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