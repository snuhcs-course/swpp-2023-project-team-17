package com.example.goclass.dataClass;

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