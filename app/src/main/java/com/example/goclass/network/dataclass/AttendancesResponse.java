package com.example.goclass.network.dataclass;

import com.google.gson.annotations.SerializedName;

public class AttendancesResponse {
    @SerializedName("attendanceId")
    int attendanceId;
    @SerializedName("attendanceDate")
    String attendanceDate;
    @SerializedName("attendanceStatus")
    int attendanceStatus;
    @SerializedName("attendanceDuration")
    int attendanceDuration;
    @SerializedName("isSent")
    int isSent;
    @SerializedName("studentId")
    int studentId;
    @SerializedName("classId")
    int classId;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public AttendancesResponse(int attendanceId, int attendanceStatus, String attendanceDate, int attendanceDuration, int isSent, int studentId, int classId) {
        this.attendanceId = attendanceId;
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
        this.attendanceDuration = attendanceDuration;
        this.isSent = isSent;
        this.studentId = studentId;
        this.classId = classId;
    }

    public AttendancesResponse(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public int getAttendanceStatus() {
        return attendanceStatus;
    }

    public int getAttendanceDuration() {
        return attendanceDuration;
    }

    public int getIsSent() {
        return isSent;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getClassId() {
        return classId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}